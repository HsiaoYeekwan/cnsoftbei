package com.hzy.jdt.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import com.hzy.jdt.ast.NewNode;
import com.hzy.jdt.view.TextDiff.Diff;
import com.hzy.jdt.view.TextDiff.LinesToCharsResult;

class Pair {
	Integer idx, length;

	Pair(Integer idx, Integer length) {
		this.idx = idx;
		this.length = length;
	}

	Pair() {

	}
}

public class ChangeDialog extends Dialog {
	private static Tree tree;
	private TreeViewer treeViewer;
	private Composite compositeOfTree;
	private Composite compositeDetail;
	private NewNode InputTree;
    private StyledText preCode,nowCode;
    private Color color = new Color(Display.getCurrent(), 245, 245, 255);// 浅蓝色
    private Color white = new Color(Display.getCurrent(), 255, 255, 255);// 白色
	protected ChangeDialog(Shell parentShell, NewNode root) {
		super(parentShell);
		InputTree = root;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("工程修改对比");
		//shell.setBackground(color);
	}

	protected Control createDialogArea(Composite parent) {
		parent.setBackground(color);
		Composite topComp = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 780;
		gridData.widthHint = 1550;
		topComp.setLayout(gridLayout);
		topComp.setLayoutData(gridData);
		topComp.setBackground(color);

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
		compositeDetail.setBackground(color);

		// 文件目录
		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 700;
		gridData.widthHint = 270;
		compositeOfTree.setLayoutData(gridData);
		compositeOfTree.setBackground(color);

		Label text = new Label(compositeOfTree, SWT.CENTER);
		text.setText("文件目录\n\r");
		text.setBackground(color);
		FontData newFontData = text.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(9);
		Font newFont = new Font(this.getShell().getDisplay(), newFontData);
		text.setFont(newFont);

		Composite compositeTree = new Composite(compositeOfTree, SWT.NONE);
		
		compositeTree.setLayout(new FillLayout());
		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 700;
		gridData.widthHint = 250;
		compositeTree.setLayoutData(gridData);
		compositeTree.setBackground(color);
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

//		// 提示修改数目
//		Label numlabel = new Label(compositeOfTree, SWT.NONE);
//		ChangeFileCount changeFileCount = new ChangeFileCount(InputTree);
//
//		numlabel.setText(
//				"总计修改" + changeFileCount.getFileName() + "个文件，共计" + changeFileCount.getAllNum() + "处内容\n请单击上方文件目录查看");// 加入一个文本标签
//		Display display1 = Display.getDefault();
//		Color color = new Color(display1, 10, 10, 10);// 颜色黑色
//		numlabel.setForeground(color);

		// 对比界面
		Label pretext = new Label(compositeDetail, SWT.CENTER);
		pretext.setText("原始代码\n\r");
		pretext.setBackground(color);
		newFontData = pretext.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(9);
		newFont = new Font(this.getShell().getDisplay(), newFontData);
		pretext.setFont(newFont);
		//
		Label nowtext = new Label(compositeDetail, SWT.CENTER);
		nowtext.setBackground(color);
		nowtext.setText("修改后代码\n\r");
		newFontData = nowtext.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(9);
		newFont = new Font(this.getShell().getDisplay(), newFontData);
		nowtext.setFont(newFont);

		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 700;
		gridData.widthHint = 600;

		//初始界面文本框
		preCode = new StyledText(compositeDetail, SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		preCode.setText("...");
		preCode.setLayoutData(gridData);
		preCode.isEnabled();
		preCode.pack();
		preCode.setEditable(false);
		preCode.setBackground(white);
		//新界面文本框
		nowCode = new StyledText(compositeDetail, SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		nowCode.setText("...");
		nowCode.setLayoutData(gridData);
		nowCode.pack();
		nowCode.isEnabled();
		nowCode.setEditable(false);
		nowCode.setBackground(white);
		// 添加选中事件
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@SuppressWarnings("rawtypes")
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				FileNode domain = (FileNode) selection.getFirstElement();
				NewNode t = (NewNode) domain;
				
				String pre = t.getOldCode();
				String now = t.getNewCode();
				String cmpString = getDiff(pre,now); //获得比对后文本
				
				//preCode.setText(pre);
				//nowCode.setText(now);
				Map<Integer, Integer> diff = getDifferent(cmpString);

				nowCode.addLineBackgroundListener(new LineBackgroundListener() {

					@Override
					public void lineGetBackground(LineBackgroundEvent event) {
						int line = ((StyledText) event.widget).getLineAtOffset(event.lineOffset);
						Display display1 = Display.getDefault();
						Color color = new Color(display1, 230, 230, 255);// 颜色浅黄
						if (diff.containsKey(line))
							event.lineBackground = color;//display1.getSystemColor(SWT.COLOR_YELLOW);
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
	//修改按钮
	@Override 
	protected Button createButton(Composite parent, int id, String	label,boolean defaultButton) { 
		parent.setBackground(new Color(Display.getCurrent(),245,245,255));
	   return null; 
	} 
	protected void initializeBounds() { 
		//我们可以利用原有的ID创建按钮,也可以用自定义的ID创建按钮 
		//但是调用的都是父类的createButton方法. 
		super.createButton((Composite)getButtonBar(), IDialogConstants.OK_ID, "确定", false); 
		getButtonBar().setBackground(color);
		//下面这个方法一定要加上,并且要放在创建按钮之后,否则就不会创建按钮 
		super.initializeBounds(); 
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
    public String getDiff(String pre,String now)
    {
    	TextDiff td = new TextDiff();
		LinesToCharsResult a = td.diff_linesToChars(pre, now);
		String lineText1 = a.getChars1();
		String lineText2 = a.getChars2();
		List<String> lineArray = a.getLineArray();
		LinkedList<Diff> diffs = td.diff_main(lineText1, lineText2, false);
		td.diff_charsToLines(diffs, lineArray);
		//td.diff_cleanupSemantic(diffs);
		String ans = td.diff_prettyHtml(diffs);
		return ans;
    }
     //滚动同步
	private  void handleVerticalScrolling(StyledText one, StyledText two) { 
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
	   //获得比对行
   public Map<Integer, Integer> getDifferent(String cnt) {
	   //System.out.println(cnt);
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			int idx = 0;
			String line[] = cnt.split("\n");
			String pre = new String();
			String now = new String();
			for (int i=0; i<line.length; i++) {
				String cntLine = line[i];	
				if(cntLine.length() == 0)
				{
					now += "\n";
					pre += "\n";
					idx++;
				}
				else if(cntLine.charAt(0)=='$') {
					String flag = cntLine.substring(0, 5);// $del$  $ins$  

					if(flag.equals("$del$")) {
						String del = cntLine.substring(5)+"\n";
						String ins = new String();
						ins = "";
						int nowidx = i+1;
						int delnum = 1; //记录删除行
						while(line[nowidx].length()==0 || line[nowidx].charAt(0) != '$')
						{
							del += line[nowidx]+"\n";
							nowidx++;
							delnum++;
						}
						nowidx++;//跳过$del$
						flag = line[nowidx].substring(0,5);
						if(flag.equals("$ins$")) //替换区域
						{
							int insnum = 1;
							ins = line[nowidx].substring(5)+"\n";
							nowidx ++;
							while(line[nowidx].length()==0 || line[nowidx].charAt(0) != '$')  //找添加段
							{
								ins += line[nowidx]+"\n";
								nowidx++;
								insnum++;
							}
							nowidx++; //跳过$$
							pre += del;
							now += ins;
						    for(int j=0; j<insnum; j++) {
						    	map.put(idx, 1);
						    	idx++;
						    }
							if(insnum < delnum) {//删除的多，now补空行
								for(int j=0; j< delnum - insnum; j++) {
									now += "\n";
									map.put(idx, 1);
									idx++;
									
								}
							}
							else if(insnum > delnum){//添加的多，pre补空行
								for(int j=0; j< insnum - delnum; j++) {
									pre += "\n";
									
								}
							}
						}
						else
						{
							pre += del;
							for(int j=0; j<delnum; j++) {
								now += "\n";
								map.put(idx, 1);
								idx ++;
								
							}
						}
						i = nowidx-1;
					}
				    else //$ins$
					{
				    	String ins = cntLine.substring(5)+"\n";
						int nowidx = i+1;
						int insnum = 1; //记录删除行
						while(line[nowidx].length()==0 || line[nowidx].charAt(0) != '$')
						{
							ins += line[nowidx]+"\n";
							nowidx++;
						    insnum++;
						}
						//nowidx++;//跳过$ins$
						i = nowidx;
						now += ins;
						for(int j=0; j<insnum; j++) {
							pre += "\n";
							map.put(idx, 1);
							idx ++;
						}
					}
				}
				else
				{
				    pre += cntLine+"\n";
				    now += cntLine+"\n";
				    idx++;
				}
				
			}
			preCode.setText(pre);
		    nowCode.setText(now);
			return map;

		}

}
