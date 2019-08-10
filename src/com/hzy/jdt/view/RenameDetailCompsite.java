package com.hzy.jdt.view;

import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.hzy.jdt.ast.OldNode;

//重命名的详细设计的布局
public class RenameDetailCompsite extends Composite {
	private static Tree tree;
	private CheckboxTreeViewer treeViewer;
	private Composite compositeOfTree;
	private Composite compositeDetail;
	private int TreeHeight;
	private Color backgroundColor,purple,blue;
	
	public RenameDetailCompsite(Composite parent, int style) {
		super(parent, style);
		Display display = Display.getCurrent();
		backgroundColor =  new Color(display,245,245,255);
		Color white = new Color(display, 255, 255, 255);// 白色
		blue = new Color(display, 230, 230, 255);// 颜色蓝
		purple = new Color(display, 250, 230, 255);// 颜色浅紫
		GridData gridData = new GridData(); //数据占格布局
		GridLayout gridLayout = new GridLayout();//布局方式
		//文字介绍
		Label titleTree = new Label(this, SWT.RIGHT);
		titleTree.setText("介绍");
		titleTree.setBackground(backgroundColor);
		FontData newFontData = titleTree.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(10);
		Font newFont = new Font(this.getShell().getDisplay(), newFontData);
		titleTree.setFont(newFont);
	    gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		titleTree.setLayoutData(gridData);
		//TreeHeight = 500;
		
		//介绍文本框 
		StyledText introText = new StyledText(this, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		introText.setBackground(white);
		gridData = new GridData();
		gridData.widthHint = 850;
		gridData.heightHint = 80;
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		introText.setLayoutData(gridData);
		introText.setText(getIntro()); //获得介绍文本
		introText.setEditable(false);//不能更改
		introText.pack();//自动换行
	    this.pack();
		compositeOfTree = new Composite(this, SWT.NONE);
		
	    gridLayout = new GridLayout();
		gridLayout.numColumns = 1; // 只有1列
		compositeOfTree.setLayout(gridLayout);
		compositeOfTree.setBackground(backgroundColor);
		//树高
		TreeHeight = 500;
		//xiangqing布局
		compositeDetail = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2; // 2列元素

		compositeDetail.setLayout(gridLayout);
		compositeDetail.setBackground(backgroundColor);
		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = TreeHeight;
		gridData.widthHint = 350;
		compositeDetail.setLayoutData(gridData); //加载数据布局
		// TODO Auto-generated constructor stub
	}

	// 选中或者取消子树
	@SuppressWarnings({ "rawtypes", "unchecked", "null" })
	private void treeSet(Itree item, boolean checked) {
		List<Itree> childs = item.getChildren();
		if (childs != null || childs.size() == 0) {
			for (Itree child : childs) {
				if (child.tag() != false) {
					if(child.getFlag() != checked) {
						treeViewer.setChecked(child, checked);
						child.setFlag(checked);
					}
				}
				treeSet(child, checked);
			}
		}
	}

	// 初始化显示选择所有选中
	@SuppressWarnings({ "static-access", "rawtypes" })
	private void setAllTreeChecked() {
		treeViewer.expandAll(); // 展开全部
		TreeItem[] items = this.tree.getItems();
		if (items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				TreeItem item = items[i];
				Itree node = (Itree) item.getData();
				if (node.isPreChecked() != false) {
					item.setChecked(true);
					//node.setChecked(checked);
				}
				if(node.getUnf() == true) //子节点需要选中
				   treeSet2(node);
			}
		}
		//treeViewer.collapseAll(); // 取消展开
	}
	// 初始化显示
		@SuppressWarnings({ "rawtypes", "unchecked", "null" })
		private void treeSet2(Itree item) {
			List<Itree> childs = item.getChildren();
			if (childs != null || childs.size() == 0) {
				for (Itree child : childs) {
					if (child.isPreChecked() != false) {
						treeViewer.setChecked(child, true);
						//child.setChecked(checked);
					}
					if(child.getUnf() == true)
				    	treeSet2(child);
				}
			}
		}
		
	//加载界面布局
	public void loadViewer(OldNode InputTree) {
        //题目
		Color color = backgroundColor;
		Label titleTree = new Label(compositeOfTree, SWT.RIGHT);
		titleTree.setText("工程结构");
	    titleTree.setBackground(backgroundColor);
		FontData newFontData = titleTree.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(10);
		Font newFont = new Font(this.getShell().getDisplay(), newFontData);
		titleTree.setFont(newFont);

		// 建立目录树布局
		Composite compositeTree = new Composite(compositeOfTree, SWT.NONE);
		compositeTree.setLayout(new FillLayout());
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 500;
		compositeTree.setLayoutData(gridData);

		Label laa = new Label(compositeOfTree, SWT.NONE);
		laa.setText(" ");
        //是否按钮布局
		Composite compositeButton = new Composite(compositeOfTree, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.makeColumnsEqualWidth = true;
		compositeButton.setLayout(gridLayout);

		
		//树形目录
		treeViewer = new CheckboxTreeViewer(compositeTree, SWT.BORDER | SWT.CHECK);
		tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		// 树型表头
		TreeColumn column = new TreeColumn(tree, SWT.CENTER);
		column.setText("原名称");
		column.setWidth(250); //设置宽度
		column = new TreeColumn(tree, SWT.CENTER);
		column.setText("新名称");
		column.setWidth(200);

		// 设定树的信息
		TreeLabelProvider labelProvider = new TreeLabelProvider();
		treeViewer.setLabelProvider(labelProvider);
		treeViewer.setContentProvider(new TreeContentProvider());
		treeViewer.setInput(InputTree.getChildren());
		setAllTreeChecked(); // 默认全部选中
		treeViewer.expandAll(); // 展开全部

		// 节点详情界面
		Label title = new Label(compositeDetail, SWT.CENTER);
		title.setText("详细信息");
		title.setBackground(backgroundColor);
		//Title.setText("项目修改对比\n\r");
		newFontData = title.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(10);
		newFont = new Font(this.getShell().getDisplay(), newFontData);
		title.setFont(newFont);

		titleTree.setFont(newFont);
		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		title.setLayoutData(gridData);
       
		//显示信息的宽度
		GridData gd = new GridData();
		gd.widthHint = 180;
		gd.heightHint = 70;
		gd.verticalAlignment = GridData.FILL;
		GridData gdl = new GridData();
		gdl.heightHint = 70;
		gdl.verticalAlignment = GridData.FILL;
		Label namel = new Label(compositeDetail, SWT.NONE);
		namel.setBackground(backgroundColor);
		namel.setText("名字:");
		namel.setLayoutData(gdl);
		Label preNameShow = new Label(compositeDetail, SWT.LEFT | SWT.HORIZONTAL | SWT.WRAP);
		preNameShow.setBackground(backgroundColor);
		preNameShow.setText("null");
		preNameShow.setLayoutData(gd);
		preNameShow.pack();

		Label typel = new Label(compositeDetail, SWT.NONE);
		typel.setBackground(backgroundColor);
		typel.setText("类型:");
		typel.setLayoutData(gdl);
		Label typeShow = new Label(compositeDetail, SWT.LEFT | SWT.HORIZONTAL | SWT.WRAP);
		typeShow.setBackground(backgroundColor);
		typeShow.setText("null");
		typeShow.pack();
		typeShow.setLayoutData(gd);
		//提示修改信息
		Label messagel = new Label(compositeDetail, SWT.NONE);
		messagel.setBackground(backgroundColor);
		messagel.setText("修改原因:");
		messagel.setLayoutData(gdl);
		Label messageShow = new Label(compositeDetail, SWT.LEFT | SWT.HORIZONTAL | SWT.WRAP);
		messageShow.setBackground(backgroundColor);
		messageShow.setText("null");
		messageShow.setLayoutData(gd);
		messageShow.pack();

		Label newNamel = new Label(compositeDetail, SWT.NONE);
		newNamel.setBackground(backgroundColor);
		newNamel.setText("重命名：");
		newNamel.setLayoutData(gdl);
		Label newNameShow = new Label(compositeDetail, SWT.LEFT | SWT.HORIZONTAL | SWT.WRAP);
		newNameShow.setText("null");
		newNameShow.setBackground(backgroundColor);
		newNameShow.pack();
		gd.heightHint = 30;
		newNameShow.setLayoutData(gd);

		Label textRemained = new Label(compositeDetail, SWT.NONE);
		textRemained.setBackground(color);
		textRemained.setText("自定义重命名：");
		gdl = new GridData();
		gdl.heightHint = 20;
		gdl.horizontalSpan = 2;
		textRemained.setLayoutData(gdl);

		//按钮
		Composite compositebu = new Composite(compositeDetail, SWT.NONE);
		compositebu.setBackground(color);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.makeColumnsEqualWidth = false;
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.widthHint = 290;
		compositebu.setLayout(gridLayout);
		compositebu.setLayoutData(gridData);

		// 初入重命名文本框
		Label lsas = new Label(compositebu, SWT.NONE);
		lsas.setText("           ");
		lsas.setBackground(color);
		Text setNewName = new Text(compositebu, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL);
		gd = new GridData();
		gd.widthHint = 130;
		gd.heightHint = 25;
		setNewName.setLayoutData(gd);
		// 命名按钮
		Button btSetNewName = new Button(compositebu, SWT.PUSH | SWT.CENTER);
		btSetNewName.setText("确定");
		gridData = new GridData();
		btSetNewName.setLayoutData(gridData);
		Button notChoose = new Button(compositeDetail, SWT.PUSH | SWT.CENTER);
		notChoose.setText("取消其子节点命名");
		gridData = new GridData();
		notChoose.setLayoutData(gridData);
		Button yesChoose = new Button(compositeDetail, SWT.PUSH | SWT.CENTER);
		yesChoose.setText("选中其子节点命名");
		gridData = new GridData();
		yesChoose.setLayoutData(gridData);

		// 显示ERROR内容
		Label errorLabel = new Label(compositeDetail, SWT.LEFT | SWT.HORIZONTAL | SWT.WRAP);
		errorLabel.setText("");
		errorLabel.setBackground(color);
		Display display1 = Display.getDefault();
		Color Redcolor = new Color(display1, 255, 0, 0);
		errorLabel.setForeground(Redcolor);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.widthHint = 250;
		gridData.heightHint = 50;
		errorLabel.setLayoutData(gridData);
		compositeDetail.pack();// 换行
		// 实现单选
		
		//取消子节点事件
		notChoose.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("rawtypes")
			public void widgetSelected(SelectionEvent e) {
				MessageDialog messageViewer = new MessageDialog(getShell(),"正在取消子节点...");
				messageViewer.open();
				messageViewer.close();
				treeViewer.expandAll();
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				Itree domain = (Itree) selection.getFirstElement();
				treeSet(domain,false);
				treeViewer.refresh();
			}
		});
		//选中子节点事件
		yesChoose.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("rawtypes")
			public void widgetSelected(SelectionEvent e) {
				treeViewer.expandAll();
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				Itree domain = (Itree) selection.getFirstElement();
				treeSet(domain,true);
				treeViewer.refresh();
			}
		});
		
		
		// 添加选中事件
		treeViewer.addCheckStateListener(new ICheckStateListener() {
			@SuppressWarnings("rawtypes")
			public void checkStateChanged(CheckStateChangedEvent arg0) {
				CheckboxTreeViewer checkboxTreeViewer = (CheckboxTreeViewer) arg0.getSource();
				boolean checked = arg0.getChecked();

				TreeItem ti = (TreeItem) checkboxTreeViewer.testFindItem(arg0.getElement());
				ti.setChecked(checked);
				Itree cnt = (Itree) ti.getData();
				//Display display1 = Display.getDefault();
				//Color color = new Color(display1, 230, 230, 255);// 浅蓝色
				if (checked) {
					ti.setBackground(purple);
				} else {
					ti.setBackground(null);
				}
				cnt.setFlag(checked);
				if (cnt.tag() == false) {
					ti.setChecked(false);
					cnt.setFlag(false);
					ti.setBackground(null);
				}
				treeViewer.refresh(cnt, true);
			}
		});

		// 选中监听事件，修改detail显示
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@SuppressWarnings("rawtypes")
			public void selectionChanged(SelectionChangedEvent event) {
				setNewName.setText("");
				errorLabel.setText("");
				
				if (event.getSelection().isEmpty()) {

					return;
				}
				if (event.getSelection() instanceof IStructuredSelection) {

					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					String nameToShow = new String();
					String typeToShow = new String();
					String newNameToShow = new String();
					String messageToShow = new String();
					Itree domain = (Itree) selection.getFirstElement();
					//System.out.println(domain.getChecked()+" "+domain.tag());
					String value = domain.getName();
					nameToShow = value;
					value = domain.getType();
					messageToShow = domain.getMessage();
					typeToShow = getType(value);
					if (domain.tag() == false) {
						newNameToShow = "不能重命名！";
					} else {
						value = domain.getRename();
						newNameToShow = value;
					}
					if (nameToShow != null)
						preNameShow.setText(nameToShow);
					else
						preNameShow.setText("null");
					if (typeToShow != null)
						typeShow.setText(typeToShow);
					else
						typeShow.setText("null");
					if (newNameToShow != null)
						newNameShow.setText(newNameToShow);
					else
						newNameShow.setText("null");
                    if(messageToShow!=null) {
                    	messageShow.setText(messageToShow);
                    }else {
                    	messageShow.setText("null");
                    }
				}
			}
		});

		// 设置按钮事件
		btSetNewName.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("rawtypes")
			public void widgetSelected(SelectionEvent e) {
				// 获得当前树选中元素
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				Itree domain = (Itree) selection.getFirstElement();
				//System.out.println(domain.getChecked()+" "+domain.tag());
				
				if (domain != null && setNewName.getText() != null && domain != null && domain.tag() == true) // 改成自定义输入
				{
					String cnt = setNewName.getText();
					boolean isOK = isCorrect(cnt);
					if (!isOK) {
						errorLabel.setText("输入不合法,更新重命名失败！");

					} else {
							//domain.setChecked(true);
							//(TreeItem)selection.getFirstElement();
							OldNode node = (OldNode) domain;
							domain.setRename(setNewName.getText());
							node.setRename(setNewName.getText());
							newNameShow.setText(setNewName.getText());
							//System.out.println(domain.getChecked()+" "+domain.tag());
							treeViewer.refresh(domain, true);
							errorLabel.setText("自定义命名成功！");
							//System.out.println(domain.getChecked()+" "+domain.tag());
						
					}
				}
				setNewName.setText("");
			}
		});
		// 输入框有输入时删除错误提示
		setNewName.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				errorLabel.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {

			}

		});

	}
	public String getIntro()
	{
		String x = new String("左边为工程结构的树状图，右边为每个节点的详细信息。\n其中新名称为“#”的节点无法重构，以紫色为背景的是需要重构的节点，以蓝色为背景的是命名格式不规范需要用户主动修改的节点。\r\n");
	     return x;
	}
	//输入字符是否合法
    private boolean isCorrect(String cnt)
    {
    
    	String remain[] = new String[] { "for", "while", "if", "else", "public", "protected", "void", "int",
				"double", "true", "false", "void", "private", "switch", "case", "null", "package", "class",
				"extends", "implement", "boolean", "interface" };
    	for(int i=0; i< remain.length; i++)
    	{
    		if(remain[i].equals(cnt))
    			return false;
    	}
    	for (int i = 0; i < cnt.length(); i++) {
			if (Character.isLetterOrDigit(cnt.charAt(i)) || cnt.charAt(i) == '_' || cnt.charAt(i) == '.')
				continue;
			else {
				return false;
			}
		}
    	return true;
    }
	public String getType(String type) {
		if (type.equals("type"))
			return "类";
		else if (type.equals("project"))
			return "项目";
		else if (type.equals("package"))
			return "包";
		else if (type.equals("compilationunit"))
			return "源文件";
		else if (type.equals("field"))
			return "成员变量";
		else if (type.equals("method"))
			return "函数";
		else if (type.equals("parameter"))
			return "参数";
		else if (type.equals("localvariable"))
			return "局部变量";
		else
			return type;
	}

	
}
