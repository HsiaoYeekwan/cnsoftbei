package com.hzy.jdt.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineBackgroundEvent;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import com.hzy.jdt.ast.NewNode;

class Pair {
	Integer idx, length;

	Pair(Integer idx, Integer length) {
		this.idx = idx;
		this.length = length;
	}

	Pair() {

	}
}

public class ChangeViewer extends Dialog {
	private static Tree tree;
	private TreeViewer treeViewer;
	private Composite compositeOfTree;
	private Composite compositeDetail;
	private NewNode InputTree;

	protected ChangeViewer(Shell parentShell, NewNode root) {
		super(parentShell);
		InputTree = root;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("工程修改对比");
	}

	protected Control createDialogArea(Composite parent) {
		Composite topComp = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 900;
		gridData.widthHint = 1550;
		topComp.setLayout(gridLayout);
		topComp.setLayoutData(gridData);

		// 文件
		compositeOfTree = new Composite(topComp, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 1; // 只有1列
		compositeOfTree.setLayout(gridLayout);

		// 详情界面
		compositeDetail = new Composite(topComp, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2; // 2列元素
		compositeDetail.setLayout(gridLayout);

		// 文件目录
		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 850;
		gridData.widthHint = 270;
		compositeOfTree.setLayoutData(gridData);

		Label text = new Label(compositeOfTree, SWT.CENTER);
		text.setText("文件目录\n\r");
		FontData newFontData = text.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(9);
		Font newFont = new Font(this.getShell().getDisplay(), newFontData);
		text.setFont(newFont);

		Composite compositeTree = new Composite(compositeOfTree, SWT.NONE);
		compositeTree.setLayout(new FillLayout());
		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 750;
		gridData.widthHint = 250;
		compositeTree.setLayoutData(gridData);
		treeViewer = new TreeViewer(compositeTree, SWT.BORDER);

		tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		// 树型表头
		// 设定树的信息
		FileTreeLabelProvider labelProvider = new FileTreeLabelProvider();
		treeViewer.setLabelProvider(labelProvider);
		treeViewer.setContentProvider(new FileContentProvider());

		treeViewer.setInput(InputTree.getChildren());
		treeViewer.expandAll(); // 展开全部

		// 提示修改数目
		Label numlabel = new Label(compositeOfTree, SWT.NONE);
		ChangeFileCount changeFileCount = new ChangeFileCount(InputTree);

		numlabel.setText(
				"总计修改" + changeFileCount.getFileName() + "个文件，共计" + changeFileCount.getAllNum() + "处内容\n请单击上方文件目录查看");// 加入一个文本标签
		Display display1 = Display.getDefault();
		Color color = new Color(display1, 10, 10, 10);// 颜色黑色
		numlabel.setForeground(color);

		// 对比界面
		Label pretext = new Label(compositeDetail, SWT.CENTER);
		pretext.setText("原始代码\n\r");
		newFontData = pretext.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(9);
		newFont = new Font(this.getShell().getDisplay(), newFontData);
		pretext.setFont(newFont);
		//
		Label nowtext = new Label(compositeDetail, SWT.CENTER);
		nowtext.setText("修改后代码\n\r");
		newFontData = nowtext.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(9);
		newFont = new Font(this.getShell().getDisplay(), newFontData);
		nowtext.setFont(newFont);

		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 670;
		gridData.widthHint = 600;

		StyledText preCode = new StyledText(compositeDetail, SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		preCode.setText("...");
		preCode.setLayoutData(gridData);
		preCode.isEnabled();
		preCode.pack();
		preCode.setEditable(false);

		StyledText nowCode = new StyledText(compositeDetail, SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		nowCode.setText("");
		nowCode.setLayoutData(gridData);
		nowCode.pack();
		nowCode.isEnabled();
		nowCode.setEditable(false);
		// 添加选中事件
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@SuppressWarnings("rawtypes")
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				FileNode domain = (FileNode) selection.getFirstElement();
				NewNode t = (NewNode) domain;
				String x = new String();
				x = "";
				for (int i = 0; i < 30; i++)
					x += " ";
				x += "\n";
				for (int i = 0; i < 30; i++)
					x += "1 1";
				x += "\n";
				preCode.setText(x);
				String pre = t.getOldCode();
				String now = t.getNewCode();
				preCode.setText(pre);
				nowCode.setText(now);
				Map<Integer, Integer> diff = getDifferent(pre, now);

				nowCode.addLineBackgroundListener(new LineBackgroundListener() {

					@Override
					public void lineGetBackground(LineBackgroundEvent event) {
						int line = ((StyledText) event.widget).getLineAtOffset(event.lineOffset);
						Display display1 = Display.getDefault();
						if (diff.containsKey(line))
							event.lineBackground = display1.getSystemColor(SWT.COLOR_YELLOW);
						else
							event.lineBackground = display1.getSystemColor(SWT.COLOR_WHITE);
					}

				});
				preCode.setAlwaysShowScrollBars(true);
				nowCode.setAlwaysShowScrollBars(true);
				handleVerticalScrolling(preCode, nowCode);
				handleHorizontalScrolling(preCode, nowCode);
			}

		});

		compositeDetail.pack();
		return topComp;
	}

	private static void handleHorizontalScrolling(StyledText one, StyledText two) {
		ScrollBar hOne = one.getHorizontalBar();
		ScrollBar hTwo = two.getHorizontalBar();
		if (hOne == null || hTwo == null)
			return;
		hOne.addListener(SWT.Selection, e -> {
			int x = one.getHorizontalPixel();
			two.setHorizontalPixel(x);
		});
		hTwo.addListener(SWT.Selection, e -> {
			int x = two.getHorizontalPixel();
			one.setHorizontalPixel(x);
		});
	}

	private static void handleVerticalScrolling(StyledText one, StyledText two) {
		ScrollBar vOne = one.getVerticalBar();
		ScrollBar vTwo = two.getVerticalBar();
		if (vOne == null || vTwo == null)
			return;
		vOne.addListener(SWT.Selection, e -> {
			int y = one.getTopPixel();
			two.setTopPixel(y);
		});
		vTwo.addListener(SWT.Selection, e -> {
			int y = two.getTopPixel();
			one.setTopPixel(y);
		});
	}

	public Map<Integer, Integer> getDifferent(String pre, String now) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int idx = 0;
		String cntpre[] = pre.split("\n");
		String cntnow[] = now.split("\n");
		for (int i = 0; i < Math.min(cntpre.length, cntnow.length); i++) {
			if (!cntpre[i].equals(cntnow[i])) {
				map.put(idx, 1);
			}
			idx++;
		}
		return map;

	}
}
