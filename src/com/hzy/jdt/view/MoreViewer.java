package com.hzy.jdt.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class MoreViewer extends Composite {
	private Composite compositeChoice;
	private int choice[];

	public MoreViewer(Composite parent, int style) {
		super(parent, style);
		choice = new int[] { 0, 0, 0, 0, 0 };
		// 新建布局
		compositeChoice = new Composite(this, SWT.NONE);
		GridData gridData = new GridData();
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		compositeChoice.setLayout(gridLayout);
		compositeChoice.setLayoutData(gridData);
	}

	public void loadViewer() {
		String[] txt = new String[] { "test", "普通for语句有三个控制部分，当第一部分和第三部分缺失或者仅第一部分缺失时将其重构为while\r(增强型for语句不进行重构)",
				"if-else语句块中的if、else if、else总数大于三条，且只涉及一个char或者int类型的变量时将其重构为switch-case",
				"switch-case的case、default少于等于三条，将其重构为if-else", "将嵌套的if语句合并为一个单if语句\n\r" };

		Label Title = new Label(compositeChoice, SWT.CENTER);
		Title.setText("请选择需要进行的操作：");
		// 设置字体
		FontData newFontData = Title.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(10);
		Font newFont = new Font(this.getShell().getDisplay(), newFontData);
		Title.setFont(newFont);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		// 组格式
		GridData gd = new GridData();
		gd.widthHint = 700;
		gd.heightHint = 80;
		gd.verticalAlignment = GridData.FILL;
		// 按钮格式
		GridData gdb = new GridData();
		gdb.widthHint = 180;
		gdb.verticalAlignment = GridData.FILL;
		// 解释格式
		GridData gdl = new GridData();
		gdl.widthHint = 480;
		gdl.verticalAlignment = GridData.FILL;

		Group groupforwhile = new Group(compositeChoice, SWT.CENTER);
		groupforwhile.setLayoutData(gd);
		groupforwhile.setLayout(gridLayout);
		Button forwhile = new Button(groupforwhile, SWT.CHECK);
		forwhile.setText("for转while");
		forwhile.setLayoutData(gdb);
		Label forwhileShow = new Label(groupforwhile, SWT.LEFT | SWT.HORIZONTAL | SWT.WRAP);
		forwhileShow.setText(txt[1]);
		forwhileShow.pack();
		forwhileShow.setLayoutData(gdl);
		groupforwhile.pack();
		forwhile.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 复选框, 选中取消时触发此事件.
				if (forwhile.getSelection())
					choice[1] = 1;
				else
					choice[1] = 0;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		Group groupifswitch = new Group(compositeChoice, SWT.CENTER);
		groupifswitch.setLayoutData(gd);
		gridLayout.numColumns = 2;
		groupifswitch.setLayout(gridLayout);
		Button ifswitch = new Button(groupifswitch, SWT.CHECK);
		ifswitch.setText("if-else转switch-case");
		ifswitch.setLayoutData(gdb);
		Label ifswitchShow = new Label(groupifswitch, SWT.LEFT | SWT.HORIZONTAL | SWT.WRAP);
		ifswitchShow.setText(txt[2]);
		ifswitchShow.pack();
		ifswitchShow.setLayoutData(gdl);
		groupifswitch.pack();
		ifswitch.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 复选框, 选中取消时触发此事件.
				if (ifswitch.getSelection())
					choice[2] = 1;
				else
					choice[2] = 0;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Group groupswitchif = new Group(compositeChoice, SWT.CENTER);
		groupswitchif.setLayoutData(gd);
		gridLayout.numColumns = 2;
		groupswitchif.setLayout(gridLayout);
		// 按钮
		Button switchif = new Button(groupswitchif, SWT.CHECK);
		switchif.setText("switch-case转if-else");
		switchif.setLayoutData(gdb);
		Label switchifShow = new Label(groupswitchif, SWT.LEFT | SWT.HORIZONTAL | SWT.WRAP);
		switchifShow.setText(txt[3]);
		switchifShow.pack();
		switchifShow.setLayoutData(gdl);
		groupswitchif.pack();
		switchif.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 复选框, 选中取消时触发此事件.
				if (switchif.getSelection())
					choice[3] = 1;
				else
					choice[3] = 0;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Group groupif = new Group(compositeChoice, SWT.CENTER);
		groupif.setLayoutData(gd);
		groupif.setLayout(gridLayout);
		Button ifuion = new Button(groupif, SWT.CHECK);
		ifuion.setText("if嵌套合并为单if");
		ifuion.setLayoutData(gdb);
		Label ifuionShow = new Label(groupif, SWT.LEFT | SWT.HORIZONTAL | SWT.WRAP);
		ifuionShow.setText(txt[4]);
		ifuionShow.pack();
		ifuionShow.setLayoutData(gdl);
		groupif.pack();
		ifuion.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 复选框, 选中取消时触发此事件.
				if (ifuion.getSelection())
					choice[4] = 1;
				else
					choice[4] = 0;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

	}

	public int getValue(int i) {
		return choice[i];
	}

}
