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
import org.eclipse.swt.widgets.Label;

public class EqualChangeCompsite extends Composite {
	private boolean choice[];
    private boolean isChoose;
	public EqualChangeCompsite(Composite parent, int style) {
		super(parent, style);
		choice = new boolean[] { true, false, false, false, false };
		isChoose = false;
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
		//介绍框
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
		la.setBackground(color);
		newFontData = la.getFont().getFontData()[0];//设置label字体
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
		ruleText.setEditable(false);
		ruleText.setBackground(white);
		ruleText.pack();
		composite.pack();
		Label laa = new Label(composite, SWT.NONE);
		laa.setText(" ");
		// 按钮布局
		Composite compositeChoose = new Composite(composite, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		compositeChoose.setLayout(gridLayout);
		compositeChoose.setBackground(color);
		// 组件
		Label lab = new Label(compositeChoose, SWT.NONE);
		lab.setText("进行等价语句转换：");
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
		//详细设置按钮
		Button btRename = new Button(composite, SWT.PUSH | SWT.CENTER);
		btRename.setText("详细设置");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gridData.horizontalSpan = 2;
		btRename.setLayoutData(gridData);
		btRename.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				EqualDetailDialog detailViewer = new EqualDetailDialog(getShell(),choice);
				int ok = detailViewer.open();
				//System.out.println(ok);
				if(ok == 0)
				{
					for(int i=1; i<=4; i++)
					{
						choice[i] = detailViewer.getValue(i);
					}
				}
			}
		});
	}
	public String getIntroText() {
		String txt = new String("欢迎使用等价语句转换功能，选择此项功能后，我们将会按照下述规则对您的代码进行等价语句转换，使您的代码更加条理，具体配置请点击\"详细设置\"。");
		return txt;
	}
	public String getRuleText() {
		String txt = new String("（1）for转while：\r\n" + 
				"    我们针对普通for而非增强型for进行重构。\r\n" + 
				"    for有三个控制部分，当第一部分和第三部分同时缺失，或仅第一部分缺失时重构为while。\r\n" + 
				"（2）if-else转switch-case：\r\n" + 
				"    当if-else语句块只涉及一个int或char类型变量，且if、else if、else的总数严格大于三条时，将if-else重构为switch-case。\r\n" + 
				"（3）switch-case转if-else：\r\n" + 
				"    当switch-case涉及的case、default的总数小于等于三条时，将switch-case重构为if-else。\r\n" + 
				"（4）if嵌套转单if语句\r\n" + 
				"    当if语句嵌套使用（没有else）、且每个if仅有一个if作为其子节点时，将嵌套的if语句块合并。\r\n" + 
				"    每个if的条件表达式用括号和”&&”合并成一个条件表达式，条件表达式长度大于100时会在适当位置进行换行。");
		 return txt;
	}
	public boolean getChoose() {
		return isChoose;
	}
	public boolean getValue(int i) {
		return choice[i];
	}

}
