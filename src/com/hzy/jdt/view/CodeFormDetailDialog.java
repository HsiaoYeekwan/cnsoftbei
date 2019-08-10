package com.hzy.jdt.view;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class CodeFormDetailDialog extends Dialog{
	private boolean isChoose;
	protected CodeFormDetailDialog(Shell parentShell,boolean isChoose) {
		super(parentShell);
		this.isChoose = isChoose;
	}
	//窗口标题
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("代码格式化详细设置");
	}
	protected Control createDialogArea(Composite parent) { //设置页面布局
		// 应该叠加一个topComp面板，在此面板创建及布局
		//getShell().setText("等价语句转换详细设置");  
	   	Composite topComp = new Composite(parent, SWT.NONE);
	   	Color color  = new Color(Display.getCurrent(),245,245,255);
	   	parent.setBackground(color);
	   	topComp.setBackground(color);
	   	topComp.setForeground(color);
	   	GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.horizontalSpacing = 1;
		topComp.setLayout(gridLayout);
		//gridData.verticalAlignment = GridData.FILL;
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.widthHint = 450;
		gridData.heightHint = 480;
		topComp.setLayoutData(gridData);
		
		Label Title = new Label(topComp, SWT.CENTER);
		Title.setText("请选择需要进行的操作：");
		Title.setBackground(color);
		// 设置字体
		FontData newFontData = Title.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(10);
		Font newFont = new Font(this.getShell().getDisplay(), newFontData);
		Title.setFont(newFont);

		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		// 组格式
		GridData gd = new GridData();
		gd.widthHint = 430;
		gd.heightHint = 70;
		gd.verticalAlignment = GridData.FILL;		
		// 按钮格式
		GridData gdb = new GridData();
		gdb.widthHint = 180;
		gdb.verticalAlignment = GridData.FILL;
		// 解释格式
		GridData gdl = new GridData();
		gdl.widthHint = 480;
		gdl.verticalAlignment = GridData.FILL;	
		
		Group groupblockAdd = new Group(topComp, SWT.CENTER);
		groupblockAdd.setBackground(color);;
		groupblockAdd.setLayoutData(gd);
		groupblockAdd.setLayout(gridLayout);
		Button blockAdd = new Button(groupblockAdd, SWT.CHECK);
		blockAdd.setBackground(color);
		blockAdd.setText("大括号补全");
		blockAdd.setLayoutData(gdb);
		blockAdd.setSelection(isChoose);//初始化为选中
		
		Label blockAddShow = new Label(groupblockAdd, SWT.LEFT | SWT.HORIZONTAL | SWT.WRAP);
		blockAddShow.setBackground(color);
		blockAddShow.setText("当缺少大括号时对大括号进行补全");
		blockAddShow.pack();
		blockAddShow.setLayoutData(gdl);
		blockAddShow.pack();
		blockAdd.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 复选框, 选中取消时触发此事件.
				if (blockAdd.getSelection())
					isChoose = true;
				else
					isChoose = false;
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		return topComp;
	}
	//创建按钮
	@Override 
	protected Button createButton(Composite parent, int id, String	label,boolean defaultButton) { 
		parent.setBackground(new Color(Display.getCurrent(),245,245,255));
	   return null; 
	} 
	protected void initializeBounds() { 
		//我们可以利用原有的ID创建按钮,也可以用自定义的ID创建按钮 
		//但是调用的都是父类的createButton方法. 
		super.createButton((Composite)getButtonBar(), IDialogConstants.OK_ID, "确定", false);
		super.createButton((Composite)getButtonBar(), IDialogConstants.CANCEL_ID, "取消", false); 
		getButtonBar().setBackground(new Color(Display.getCurrent(),245,245,255));
		//下面这个方法一定要加上,并且要放在创建按钮之后,否则就不会创建按钮 
		super.initializeBounds();
	}
	public boolean getChoose() {
		return isChoose;
	}

}
