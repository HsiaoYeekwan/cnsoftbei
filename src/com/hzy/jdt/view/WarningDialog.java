package com.hzy.jdt.view;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hzy.jdt.ast.OldNode;

public class WarningDialog extends Dialog {
	String error, warning;
	boolean hasError;
	boolean needOpen;

	protected WarningDialog(Shell parentShell) {
		super(parentShell);
		error = new String();
		warning = new String();
		hasError = false;
		needOpen = true;
	}

	protected WarningDialog(Shell parentShell, String error, String warning) {
		super(parentShell);
		this.error = error;
		this.warning = warning;
		hasError = false;
		needOpen = true;
	}

	protected Control createDialogArea(Composite parent) {
		// 应该叠加一个topComp面板，在此面板创建及布局
		Composite topComp = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 200;
		gridData.widthHint = 700;
		topComp.setLayout(gridLayout);
		if (error.length() != 0) {
			Label errorLabel = new Label(topComp, SWT.NONE);
			errorLabel.setText("请注意以下变量重命名后命名冲突,请修改后重构");// 加入一个文本标签
			Display display1 = Display.getDefault();
			Color color = new Color(display1, 255, 0, 0);
			errorLabel.setForeground(color);
			Text text = new Text(topComp, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);// 加入一个文本框
			text.setLayoutData(gridData);// 用RowData来设置文本框的长度
			text.setText(error);
			text.pack();
			hasError = true;
		}
		if (warning.length() != 0) {
			new Label(topComp, SWT.NONE).setText("以下变量重命名后命名冲突，可能导致错误:");// 加入一个文本标签
			Text text = new Text(topComp, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);// 加入一个文本框
			text.setLayoutData(gridData);// 用RowData来设置文本框的长度
			text.setText(warning);
			text.pack();

		}
		if (error.length() == 0 && warning.length() == 0) {
			new Label(topComp, SWT.CENTER).setText("无命名冲突，确定开始重构？");// 加入一个文本标签
		}
		topComp.pack();
		return topComp;
	}

	public boolean hasError() {
		return hasError;
	}

	public void loadData(OldNode root) {
		ErrorMessage message = new ErrorMessage();
		message.getMessage(root);
		error = message.getError();
		warning = message.getWarning();
		if (error.length() == 0 && warning.length() == 0) {
			needOpen = false;
		}
	}

	public boolean getNeed() {
		return needOpen;
	}
}
