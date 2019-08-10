package com.hzy.jdt.view;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import com.hzy.jdt.ast.OldNode;
//窗口
public class RenameDetailDialog extends Dialog {
	OldNode InputTree;
	protected RenameDetailDialog(Shell parentShell) {
		super(parentShell);
		
	}
	protected RenameDetailDialog(Shell parentShell,OldNode InputTree) {
		super(parentShell);
		parentShell.setBackground(new Color(Display.getCurrent(),245,245,255));
		this.InputTree = InputTree;
		
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
		getButtonBar().setBackground(new Color(Display.getCurrent(),245,245,255));
		//下面这个方法一定要加上,并且要放在创建按钮之后,否则就不会创建按钮 
		super.initializeBounds(); 
	} 
	//窗口标题
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("重命名重构详细设置");
		shell.setBackground(new Color(Display.getCurrent(),245,245,255));
	}
	protected Control createDialogArea(Composite parent) { //设置页面布局
		// 应该叠加一个topComp面板，在此面板创建及布局 
		RenameDetailCompsite topComp = new RenameDetailCompsite(parent, SWT.NONE);
		topComp.setBackground(new Color(Display.getCurrent(),245,245,255));
		parent.setBackground(new Color(Display.getCurrent(),245,245,255));
		topComp.loadViewer(InputTree);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.horizontalSpacing = 1;
		topComp.setLayout(gridLayout);
		//gridData.verticalAlignment = GridData.FILL;
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.widthHint = 860;
		gridData.heightHint = 750;
		topComp.setLayoutData(gridData);
		return topComp;
	}

}
