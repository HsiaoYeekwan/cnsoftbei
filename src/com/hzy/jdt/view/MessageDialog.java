package com.hzy.jdt.view;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class MessageDialog extends Dialog {
    String text;
    
    private Color color = new Color(Display.getCurrent(), 245, 245, 255);// 浅蓝色

	private Shell shell;
	protected MessageDialog(Shell parentShell) {
		super(parentShell);
		
		// TODO Auto-generated constructor stub
	}
	protected MessageDialog(Shell parentShell,String text) {
		super(parentShell);
		this.text = text;
		this.shell = new Shell(parentShell);
		// TODO Auto-generated constructor stub
	}
    public void dispose() {
    	this.shell.dispose();
    }
	// 创建按钮
	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		parent.setBackground(color);
		return null;
	}

	protected void initializeBounds() {
		// 我们可以利用原有的ID创建按钮,也可以用自定义的ID创建按钮
		// 但是调用的都是父类的createButton方法.
		//super.createButton((Composite) getButtonBar(), IDialogConstants.OK_ID, "确定", false);
		getButtonBar().setBackground(color);
		// 下面这个方法一定要加上,并且要放在创建按钮之后,否则就不会创建按钮
		super.initializeBounds();
	}

	// 窗口标题
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("提示信息");
		shell.setBackground(color);
	}

	protected Control createDialogArea(Composite parent) { // 设置页面布局
		Composite topComp = new RenameDetailCompsite(parent, SWT.NONE);
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 100;
		gridData.widthHint = 150;
		topComp.setLayoutData(gridData);
		Label typel = new Label(topComp, SWT.NONE);
		typel.setBackground(color);
		typel.setText(text);
		return topComp;
	}

}
