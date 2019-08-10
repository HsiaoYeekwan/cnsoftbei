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


public class EqualDetailDialog extends Dialog{
	private boolean choice[];
	protected EqualDetailDialog(Shell parentShell,boolean []choices) {
		super(parentShell);
		choice = new boolean[5];
		for(int i=0; i<=4;i++) {
			choice[i] = choices[i];
		}
	}
	//窗口标题
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("等价语句转换详细设置");
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
		
		Group groupforwhile = new Group(topComp, SWT.CENTER);
		groupforwhile.setBackground(color);;
		groupforwhile.setLayoutData(gd);
		groupforwhile.setLayout(gridLayout);
		Button forwhile = new Button(groupforwhile, SWT.CHECK);
		forwhile.setBackground(color);
		forwhile.setText("for转while");
		forwhile.setLayoutData(gdb);
	    forwhile.setSelection(choice[1]);//初始化为选1
			
		forwhile.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 复选框, 选中取消时触发此事件.
				choice[1] = forwhile.getSelection();
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		Group groupifswitch = new Group(topComp, SWT.CENTER);
		groupifswitch.setBackground(color);
		groupifswitch.setLayoutData(gd);
		gridLayout.numColumns = 2;
		groupifswitch.setLayout(gridLayout);
		Button ifswitch = new Button(groupifswitch, SWT.CHECK);
		ifswitch.setBackground(color);
		ifswitch.setText("if-else转switch-case");
		ifswitch.setLayoutData(gdb);
		ifswitch.setSelection(choice[2]);//初始化为选中1
		ifswitch.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 复选框, 选中取消时触发此事件.
				choice[2] = ifswitch.getSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Group groupswitchif = new Group(topComp, SWT.CENTER);
		groupswitchif.setBackground(color);
		groupswitchif.setLayoutData(gd);
		gridLayout.numColumns = 2;
		groupswitchif.setLayout(gridLayout);
		// 按钮
		Button switchif = new Button(groupswitchif, SWT.CHECK);
		switchif.setText("switch-case转if-else");
		switchif.setBackground(color);
		switchif.setLayoutData(gdb);
		switchif.setSelection(choice[3]);//初始化为选中
		switchif.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 复选框, 选中取消时触发此事件.
			    choice[3] = switchif.getSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		
		Group groupif = new Group(topComp, SWT.CENTER);
		groupif.setBackground(color);
		groupif.setLayoutData(gd);
		groupif.setLayout(gridLayout);
		Button ifuion = new Button(groupif, SWT.CHECK);
		ifuion.setText("if嵌套合并为单if");
		ifuion.setBackground(color);
		ifuion.setLayoutData(gdb);
		ifuion.setSelection(choice[4]);//初始化为选中
		ifuion.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 复选框, 选中取消时触发此事件
				choice[4] = ifuion.getSelection();
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
	public boolean getValue(int i) {
		return choice[i];
	}

}
