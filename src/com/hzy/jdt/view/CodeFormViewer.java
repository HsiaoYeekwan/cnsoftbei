package com.hzy.jdt.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class CodeFormViewer extends Composite {
	boolean isChoose;

	public CodeFormViewer(Composite parent, int style) {
		super(parent, style);
		isChoose = true;
		Composite composite = new Composite(this, SWT.NONE);
		GridData gridData = new GridData();
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		composite.setLayoutData(gridData);

		Label la = new Label(composite, SWT.NONE);
		la.setText("代码规范说明");
		FontData newFontData = la.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(10);
		Font newFont = new Font(this.getShell().getDisplay(), newFontData);
		la.setFont(newFont);
		// 显示框
		StyledText text = new StyledText(composite, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		GridData gd = new GridData();
		gd.widthHint = 700;
		gd.heightHint = 450;
		gd.verticalAlignment = GridData.FILL;
		text.setLayoutData(gd);
		text.setText(getMyText());
		text.pack();
		composite.pack();
		Label laa = new Label(composite, SWT.NONE);
		laa.setText(" ");
		// 按钮布局
		Composite compositeChoose = new Composite(composite, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		compositeChoose.setLayout(gridLayout);
		// 组件
		Label lab = new Label(compositeChoose, SWT.NONE);
		lab.setText("进行代码风格规范");
		newFontData = lab.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFont = new Font(this.getShell().getDisplay(), newFontData);
		lab.setFont(newFont);
		Button yes = new Button(compositeChoose, SWT.RADIO);
		yes.setText("是");
		Button no = new Button(compositeChoose, SWT.RADIO);
		no.setText("否");
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
	}

	public String getMyText() {
		// 根据相对路径加载codeForm.txt
		String x = new String("我们按照google对java的编码规范进行重构，包括：\r\n" + 
				"1）大括号\r\n" + 
				"大括号用在if，else，for，do，和while等语句，甚至当其为空或者只有一句话时也要使用。\r\n" + 
				"对于非空语句块，遵循开头大括号前没有换行、开头大括号后换行、结束大括号前换行等风格，如果右大括号结束一个语句块、函数体、构造函数体或者有命名的类体，则需要换行；如果右大括号后面接else或者逗号，则不应该换行。\r\n" + 
				"对于空的语句块，可以在大括号开始之后直接结束大括号，中间不需要空格或换行。但如果是一个由几个语句块联合组成的语句块，则需要换行。（例如：if/else 或try/catch/finally）\r\n" + 
				"2）语句块的缩进：4空格\r\n" + 
				"每当一个新的语句块产生，就增加一个缩进。当这个语句块结束时，恢复到上一层的缩进。缩进要求对整个语句块中的代码和注释都适用。\r\n" + 
				"3）一行最多只有一句代码\r\n" + 
				"每句代码的结束都需要换行。\r\n" + 
				"4）行长度限制：100\r\n" + 
				"一般情况下Java代码的单行限制长度为100个字符。\r\n" + 
				"5）空白空间\r\n" + 
				"垂直空白：\r\n" + 
				"单行空行在以下情况使用：\r\n" + 
				"类成员间需要空行隔开：例如成员变量、构造函数、成员函数、内部类、静态初始化语句块（static initializers）、实例初始化语句块（instance initializers）。 \r\n" + 
				"例外：成员变量之间的空白行不是必需的。一般多个成员变量中间的空行，是为了对成员变量做逻辑上的分组。\r\n" + 
				"水平空白：\r\n" + 
				"除了语法、其他规则、词语分隔、注释和javadoc外，水平的ASCII空格只在以下情况出现：\r\n" + 
				"所有保留的关键字与紧接它之后的位于同一行的左括号之间需要用空格隔开。（例如if、for、catch）\r\n" + 
				"所有保留的关键字与在它之前的右花括号之间需要空格隔开。（例如else、catch）\r\n" + 
				"在左花括号之前都需要空格隔开。只有两种例外：@SomeAnnotation({a, b})和String[][] x = {{\"foo\"}};\r\n" + 
				"所有的二元运算符和三元运算符的两边，都需要空格隔开。\r\n" + 
				"逗号、冒号、分号和右括号之后，需要空格隔开。\r\n" + 
				"// 双斜线开始一行注释时。双斜线两边都应该用空格隔开。并且可使用多个空格，但是不做强制要求。\r\n" + 
				" 变量声明时，变量类型和变量名之间需要用空格隔开。\r\n" + 
				" 初始化一个数组时，花括号之间可以用空格隔开，也可以不使用。（例如：new int[] {5, 6} 和 new int[] { 5, 6 } 都可以）\r\n" + 
				"注意：这一原则不影响一行开始或者结束时的空格。只针对行内部字符之间的隔开。");
		return x;
	}

}
