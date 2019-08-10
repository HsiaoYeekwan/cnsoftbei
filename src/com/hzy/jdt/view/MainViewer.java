package com.hzy.jdt.view;


import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.hzy.jdt.ast.MyVisitor;
import com.hzy.jdt.ast.NewNode;
import com.hzy.jdt.ast.OldNode;
import com.hzy.jdt.codeformat.CodeFormat;
import com.hzy.jdt.renamerefact.*;
import com.hzy.jdt.statementchange.StatementChange;

public class MainViewer {
	protected Shell shell;
	final Display display = Display.getDefault();
	public MainViewer() {

	}

	public void run(OldNode inputTree, MyVisitor myvisitor) {

		
		shell = new Shell(display, SWT.CLOSE | SWT.MIN);
		shell.setSize(760, 760);
		shell.setText("代码重构");
		shell.setLayout(new FormLayout());
		setCenter(display, shell);
	
		//shell.setForeground(new Color(Display.getCurrent(),0,0,0));
		//shell.setBackground(new Color(Display.getCurrent(),0,0,0));
		createContents(inputTree, myvisitor);

		shell.open();
		shell.setLayout(new FillLayout());
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public static void setCenter(Display display, Shell shell) {
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
	}

	protected void createContents(OldNode inputTree, MyVisitor myvisitor) {
		GridLayout gridLayout = new GridLayout();
		GridData gridData = new GridData();
		Composite ALL = new Composite(shell, SWT.CENTER);
		ALL.setBackground(new Color(Display.getCurrent(),255,255,255));
		//shell.setBackground(new Color(Display.getCurrent(),0,0,0)););
		/*
		 * ALL tabFolder + Button tabItem1 + tabItem2 composite compositeOfTree +
		 * compositeDetail titleTree+compositeTree+compositeButton+
		 */
		ALL.setLayout(gridLayout);

		TabFolder tabFolder = new TabFolder(ALL, SWT.BORDER);
		tabFolder.setForeground(new Color(Display.getCurrent(),255,255,255));//白色边框
		
		Color backgroundColor = new Color(Display.getCurrent(),245,245,255);//背景色 浅蓝
		tabFolder.setBackground(backgroundColor);//设置背景色
		TabItem tabItem0 = new TabItem(tabFolder, SWT.NONE);
		tabItem0.setText("代码格式化");
		
		TabItem tabItem1 = new TabItem(tabFolder, SWT.NONE);
		tabItem1.setText("重命名重构");
		TabItem tabItem2 = new TabItem(tabFolder, SWT.NONE);
		tabItem2.setText("等价语句转换");
		gridLayout = new GridLayout();
		gridData = new GridData();
		gridLayout.numColumns = 2;
		gridData.verticalAlignment = GridData.FILL;
		gridData.widthHint = 735;
		gridData.heightHint = 630;
		tabFolder.setLayoutData(gridData);
		gridLayout.makeColumnsEqualWidth = false;
		tabFolder.setLayout(gridLayout);

		// 标准化
		CodeFormCompsite codeform = new CodeFormCompsite(tabFolder, SWT.NONE);
		codeform.setBackground(backgroundColor);
		tabItem0.setControl(codeform);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.horizontalSpacing = 1;
		codeform.setLayout(gridLayout);
		gridData.verticalAlignment = GridData.FILL;
		gridData.widthHint = 735;
		codeform.setLayoutData(gridData);
		
		// 重命名界面
		RenameCompsite renameViewer = new RenameCompsite(tabFolder, SWT.NONE,inputTree);
		renameViewer.setBackground(backgroundColor);
	//	renameViewer.loadViewer(InputTree);
		tabItem1.setControl(renameViewer);
		renameViewer.setLayout(gridLayout);
		renameViewer.setLayoutData(gridData);

		// 重构界面
		EqualChangeCompsite moreViewer = new EqualChangeCompsite(tabFolder, SWT.NONE);
		moreViewer.setBackground(backgroundColor);
		tabItem2.setControl(moreViewer);
		moreViewer.setLayout(gridLayout);
		moreViewer.setLayoutData(gridData);
		//按钮
		Composite btComposite = new Composite(ALL, SWT.CENTER);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		btComposite.setBackground(new Color(Display.getCurrent(),255,255,255));
		btComposite.setLayoutData(gridData);
		gridLayout.numColumns = 2;
		btComposite.setLayout(gridLayout);
		Button btRename = new Button(btComposite, SWT.PUSH | SWT.CENTER);
		btRename.setText("确定重构");
		btRename.setBackground(backgroundColor);
		//btRename.setLayoutData(gridData);
		//取消按钮
		Button btreleaseRename = new Button(btComposite, SWT.PUSH | SWT.CENTER);
		btreleaseRename.setBackground(backgroundColor);
		btreleaseRename.setText("取消重构");
		//btreleaseRename.setLayoutData(gridData);
		btreleaseRename.addSelectionListener(new SelectionAdapter() { //取消事件
			public void widgetSelected(SelectionEvent e) {
		      shell.dispose();
			}
		});
		btRename.addSelectionListener(new SelectionAdapter() { //选择事件
			public void widgetSelected(SelectionEvent e) {
				boolean needChange = false;
				IWorkspaceRoot myworkspaceroot = ResourcesPlugin.getWorkspace().getRoot();
				
				//等价转换
				if (moreViewer.getChoose() == true) { 
//					MessageDialog messageViewer = new MessageDialog(shell,"正在进行等价语句转换...");
//					messageViewer.open();
					StatementChange statementChange = new StatementChange(myworkspaceroot);
					try {
						for (int i = 1; i <= 4; i++) {
							if (moreViewer.getValue(i)) {
								statementChange.start(i);
								needChange = true;//需要显示对比
							}
						}
//						messageViewer.close();
					} catch (CoreException e1) {
						e1.printStackTrace();
					}
					
				}
				//需要格式化
				if (codeform.getChoose() == true) {
//					MessageDialog messageViewer = new MessageDialog(shell,"正在进行代码格式化...");
//					messageViewer.open();
					CodeFormat codeFormat = new CodeFormat(myworkspaceroot);
					try {
						codeFormat.start(codeform.getBlockAdd());
						needChange = true;//需要显示对比
//						messageViewer.close();
					} catch (CoreException e1) {
						e1.printStackTrace();
					}
				}
				//需要重命名
				if (renameViewer.getChoose() == true) { 
						needChange = true; //需要显示对比
						//开始重命名
						
						RenameRefact myrefactor = new RenameRefact();
						myrefactor.start(inputTree);
				}
				shell.dispose();
				//显示对比
				try {
					if(needChange) {
						//MessageDialog messageViewer = new MessageDialog(shell,"正在生产对比界面...");
						//messageViewer. open();
						//获取新结构
						myvisitor.update(inputTree, "");
						//获取树
						NewNode newRoot = myvisitor.loadNewCode(myworkspaceroot);
						//显示变化
						shell = new Shell(display, SWT.CLOSE | SWT.MIN);
						shell.setSize(760, 760);
						shell.setText("代码重构");
						shell.setLayout(new FormLayout());
						setCenter(display, shell);
						ChangeDialog changeViewer = new ChangeDialog(shell, newRoot);
					//	messageViewer.dispose();
						changeViewer.open();
					 }
				   } catch (CoreException e1) {
					e1.printStackTrace();
				 }
				shell.dispose();
			}
		});
	}
}
