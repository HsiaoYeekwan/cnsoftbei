package com.hzy.jdt.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class CodeFormCompsite extends Composite {
	private boolean isChoose;
    private boolean blockAdd;
	public CodeFormCompsite(Composite parent, int style) {
		super(parent, style);
		isChoose = false; //进行格式化
		blockAdd = false; //补全大括号
		Color color = new Color(Display.getCurrent(), 245, 245, 255);// 浅蓝色
		Color white = new Color(Display.getCurrent(), 255, 255, 255);// 浅蓝色
		Composite composite = new Composite(this, SWT.NONE);

		composite.setBackground(color);
		GridData gridData = new GridData();
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		composite.setLayoutData(gridData);
		Label laIntro = new Label(composite, SWT.NONE);
		laIntro.setText("介绍");
		FontData newFontData = laIntro.getFont().getFontData()[0];// 设置label字体
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(10);
		Font newFont = new Font(this.getShell().getDisplay(), newFontData);
		laIntro.setFont(newFont);
		// 介绍文本框
		StyledText introductionText = new StyledText(composite, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		introductionText.setBackground(white);
		GridData gd = new GridData();
		gd.widthHint = 700;
		gd.heightHint = 80;
		gd.verticalAlignment = GridData.FILL;
		introductionText.setLayoutData(gd);
		introductionText.setText(getIntroText());
		introductionText.pack();//
		introductionText.setEditable(false);// 设置为不可编辑
		// Display display = Display.getDefault();
		// introductionText.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		Label la = new Label(composite, SWT.NONE);
		la.setText("规范规则");
		la.setBackground(color);
		newFontData = la.getFont().getFontData()[0];// 设置label字体
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(10);
		newFont = new Font(this.getShell().getDisplay(), newFontData);
		la.setFont(newFont);
		// 显示框
		StyledText ruleText = new StyledText(composite, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		gd = new GridData();
		gd.widthHint = 700;
		gd.heightHint = 350;
		gd.verticalAlignment = GridData.FILL;
		ruleText.setLayoutData(gd);
		ruleText.setText(getRuleText());
		ruleText.pack();
		ruleText.setEditable(false);
		ruleText.setBackground(white);
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
		lab.setText("进行代码风格规范：");
		lab.setBackground(color);
		newFontData = lab.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFont = new Font(this.getShell().getDisplay(), newFontData);
		lab.setFont(newFont);
		Button yes = new Button(compositeChoose, SWT.RADIO);
		yes.setText("是");
		yes.setBackground(color);
		;
		Button no = new Button(compositeChoose, SWT.RADIO);
		no.setText("否");
		no.setBackground(color);
		no.setSelection(true);
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
		// 详细设置按钮
		Button btRename = new Button(composite, SWT.PUSH | SWT.CENTER);
		btRename.setText("详细设置");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gridData.horizontalSpan = 2;
		btRename.setLayoutData(gridData);
		btRename.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				CodeFormDetailDialog detailViewer = new CodeFormDetailDialog(getShell(),blockAdd);
				int ok = detailViewer.open();
				if (ok == 0 ) {	
				    blockAdd = detailViewer.getChoose();
				}
			}
		});
	}
	public int getBlockAdd() {
		if(blockAdd)
			return 1;
		else
			return 0;
	}

	public String getIntroText() {
		String x = new String("欢迎使用格式重构功能，选择此项功能后，我们将按照下述重构规则对您的代码进行格式重构，使您的代码更加整洁，具体配置请点击\"详细设置\"。\r\n");
		return x;
	}

	public String getRuleText() {
		// 根据相对路径加载codeForm.txt
		String x = new String(
				"1.大括号\r\n" + 
				"（1）大括号用在if，else，for，do，和while等语句后，当其为空或者只有一句话时也要使用。\r\n" + 
				"（2）对于非空语句块，开头的大括号前应没有换行，开头大括号后应有换行，结束大括号前应有换行。\r\n" + 
				"    如果右大括号结束一个语句块、函数体、构造函数体或者有命名的类体，则应该换行；\r\n" + 
				"    如果右大括号后面接else或者逗号，则不应该换行。\r\n" + 
				"（3）对于空的语句块，可以在大括号开始之后直接结束大括号，中间不需要空格或换行。\r\n" + 
				"    如果是一个由几个语句块联合组成的语句块，则需要换行。例如：if/else 或try/catch/finally。\r\n" + 
				"2.语句块的缩进：4空格\r\n" + 
				"    当一个新的语句块产生时增加一个缩进，当这个语句块结束时恢复到上一层的缩进。缩进要求对整个语句块中的代码和注释都适用。\r\n" + 
				"3.一行最多只有一句代码\r\n" + 
				"    每句代码结束时都需要换行。\r\n" + 
				"4.行长度限制：100\r\n" + 
				"    一般情况下Java代码的单行限制长度为100个字符。\r\n" + 
				"5.空白\r\n" + 
				"（1）垂直空白：\r\n" + 
				"    1）类成员间需要用空行隔开\r\n" + 
				"    例如成员变量、构造函数、成员函数、内部类、静态初始化语句块、实例初始化语句块。 \r\n" + 
				"    例外：成员变量之间的空行不是必要的。一般多个成员变量间的空行是为了对成员变量做逻辑上的分组。\r\n" + 
				"    2）在函数内部，根据代码逻辑分组的需要，设置空行作为间隔。\r\n" + 
				"    3）类的第一个成员开始之前，或者最后一个成员结束之后，用空行间隔。\r\n" + 
				"（2）水平空白：\r\n" + 
				"    1）所有保留的关键字与紧接它之后的、位于同一行的左括号之间需要用空格隔开。例如if、for、catch。\r\n" + 
				"    2）所有保留的关键字与它之前的右花括号之间需要用空格隔开。例如else、catch。\r\n" + 
				"    3）在左花括号之前都需要空格隔开。只有两种例外：\r\n" + 
				"    @SomeAnnotation({a, b})\r\n" + 
				"    String[][] x = {{\"foo\"}};\r\n" + 
				"    4）所有的二元运算符和三元运算符的两边都需要空格隔开。\r\n" + 
				"    5）逗号、冒号、分号和右括号之后需要空格隔开。\r\n" + 
				"    6）双斜线开始一行注释时。双斜线两边都应该用空格隔开。\r\n" + 
				"    7）变量声明时，变量类型和变量名之间需要用空格隔开。\r\n" + 
				"    8）初始化一个数组时，花括号之间可以用空格隔开。\r\n" + 
				"");
		return x;
	}

	public boolean getChoose() {
		return isChoose;
	}
}
