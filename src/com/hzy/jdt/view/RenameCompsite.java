package com.hzy.jdt.view;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.hzy.jdt.ast.OldNode;

public class RenameCompsite extends Composite {
	private boolean isChoose;
	Shell shell;
	public RenameCompsite(Composite parent, int style) {
		super(parent, style);
	}
	public RenameCompsite(Composite parent, int style,OldNode inputTree) {
		super(parent, style);
		this.shell = shell;
		isChoose = true;
		Color color = new Color(Display.getCurrent(),245,245,255);//浅蓝色
		Color white = new Color(Display.getCurrent(),255,255,255);//bai
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBackground(color);
		GridData gridData = new GridData();
		//gridData.widthHint = 700;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		composite.setLayoutData(gridData);
		
		Label laIntro = new Label(composite, SWT.NONE);
		laIntro.setText("介绍");
		FontData newFontData = laIntro.getFont().getFontData()[0];//设置label字体
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(10);
		Font newFont = new Font(this.getShell().getDisplay(), newFontData);
		laIntro.setFont(newFont);
		laIntro.setBackground(color);
		StyledText introductionText = new StyledText(composite, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		GridData gd = new GridData();
		gd.widthHint = 700;
		gd.heightHint = 80;
		gd.verticalAlignment = GridData.FILL;
		introductionText.setLayoutData(gd);
		introductionText.setText(getIntroText());
		introductionText.setEditable(false);
		introductionText.setBackground(white);
		introductionText.pack();
		
		Label la = new Label(composite, SWT.NONE);
		la.setText("规范规则");
		newFontData = la.getFont().getFontData()[0];//设置label字体
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(10);
		newFont = new Font(this.getShell().getDisplay(), newFontData);
		la.setFont(newFont);
		la.setBackground(color);
		// 显示框
		StyledText ruleText = new StyledText(composite, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
	    gd = new GridData();
		gd.widthHint = 700;
		gd.heightHint = 350;
		gd.verticalAlignment = GridData.FILL;
		ruleText.setLayoutData(gd);
		ruleText.setText(getRuleText());
		ruleText.setBackground(white);
		ruleText.setEditable(false);
		ruleText.pack();
		composite.pack();
		Label laa = new Label(composite, SWT.NONE);
		laa.setText(" ");
		// 按钮布局
		Composite compositeChoose = new Composite(composite, SWT.NONE);
		compositeChoose.setBackground(color);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		compositeChoose.setLayout(gridLayout);
		// 组件
		Label lab = new Label(compositeChoose, SWT.NONE);
		lab.setText("进行重命名重构：");
		newFontData = lab.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFont = new Font(this.getShell().getDisplay(), newFontData);
		lab.setFont(newFont);
		Button yes = new Button(compositeChoose, SWT.RADIO);
		yes.setText("是");
		yes.setBackground(color);;
		Button no = new Button(compositeChoose, SWT.RADIO);
		no.setText("否");
		no.setBackground(color);
		yes.setSelection(true);
		// 只能选择一个
		yes.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				no.setSelection(true);
				yes.setSelection(false);
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				yes.setSelection(true);
				no.setSelection(false);
				isChoose = true;
			}
		});
		no.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				yes.setSelection(true);
				no.setSelection(false);
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				no.setSelection(true);
				yes.setSelection(false);
				isChoose = false;
			}
		});
		Button btRename = new Button(composite, SWT.PUSH | SWT.CENTER);
		btRename.setText("详细设置");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gridData.horizontalSpan = 2;
		btRename.setLayoutData(gridData);
		btRename.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
//				Shell pshell = new Shell(Display.getCurrent(), SWT.CLOSE | SWT.MIN);
//				pshell.setSize(760, 760);
//				pshell.setText("代码重构");
//				pshell.setLayout(new FormLayout());
				//setCenter(Display.getCurrent(), shell);
//				MessageDialog messageViewer = new MessageDialog(pshell,"正在进行打开重命名详细设置界面");
//				messageViewer.open();
				RenameDetailDialog detailViewer = new RenameDetailDialog(getShell(),inputTree);
//				messageViewer.dispose();
//				pshell.dispose();
				detailViewer.open();
				
			}
		});
		
	}
	public String getIntroText() {
		String x = new String("欢迎使用命名重构功能，选择此项功能后，我们将按照下述重构规则对您的代码进行命名重构，使您的代码命名格式更加规范，具体配置请点击\"详细设置\"。\r\n");
		return x;
	}
	public String getRuleText() {
		// 根据相对路径加载codeForm.txt
		String x = new String("（1）包名重构：\r\n" + 
				"    包名应全部用小写字母，通过”.”将各级连在一起，不应该使用下划线。\r\n" + 
				"（2）类名重构：\r\n" + 
				"    类名采用采用以大写字母开头的大小写字符间隔的方式（UpperCamelCase）。\r\n" + 
				"（3）函数名重构：\r\n" + 
				"    函数名采用以小写字母开头的大小写字符间隔的方式（lowerCamelCase）。\r\n" + 
				"（4）常量名重构：\r\n" + 
				"    常量名全部使用大写字符，词与词之间用下划线隔开（CONSTANCE_CASE）。\r\n" + 
				"（5）非常量的成员变量重构：\r\n" + 
				"    成员变量名采用以小写字母开头的大小写字符间隔的方式（lowerCamelCase）\r\n" + 
				"（6）参数名重构：\r\n" + 
				"    参数名采用以小写字母开头的大小写字符间隔的方式（lowerCamelCase）\r\n" + 
				"（7）局部变量名重构：\r\n" + 
				"    局部变量名采用以小写字母开头的大小写字符间隔的方式（lowerCamelCase）\r\n" + 
				"（8）泛型类重构：\r\n" + 
				"    若泛型类名是一个单独的字母，则将其转为大写；否则按照一般类名重构方式进行重构，并在最后面加一个”T”。");
		return x;
	}

	public boolean getChoose() {
		return isChoose;
	}
}
