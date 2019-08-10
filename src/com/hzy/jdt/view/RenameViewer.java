package com.hzy.jdt.view;

import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.hzy.jdt.ast.OldNode;

public class RenameViewer extends Composite {
	private static Tree tree;
	private CheckboxTreeViewer treeViewer;
	private Composite compositeOfTree;
	private Composite compositeDetail;
	private int TreeHeight;
	private boolean isChoose;

	public RenameViewer(Composite parent, int style) {
		super(parent, style);
		isChoose = true;
		compositeOfTree = new Composite(this, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1; // 只有1列
		compositeOfTree.setLayout(gridLayout);

		TreeHeight = 600;
		// 详情界面
		compositeDetail = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2; // 2列元素

		compositeDetail.setLayout(gridLayout);
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = TreeHeight;
		gridData.widthHint = 300;
		compositeDetail.setLayoutData(gridData);
	}

	// 选中取消全部 遍历树
	@SuppressWarnings({ "rawtypes", "unchecked", "null" })
	private void treeSet(Itree item, boolean checked) {
		List<Itree> childs = item.getChildren();
		if (childs != null || childs.size() == 0) {
			for (Itree child : childs) {
				if (child.tag() != false) {
					if(child.getChecked() != checked) {
						treeViewer.setChecked(child, checked);
						child.setChecked(checked);
					}
				}
				treeSet(child, checked);
			}
		}
	}

	// 选中全部 遍历树
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
				treeSet2(node);
			}
		}
		//treeViewer.collapseAll(); // 取消展开
	}
	// 选中取消全部 遍历树
		@SuppressWarnings({ "rawtypes", "unchecked", "null" })
		private void treeSet2(Itree item) {
			List<Itree> childs = item.getChildren();
			if (childs != null || childs.size() == 0) {
				for (Itree child : childs) {
					if (child.isPreChecked() != false) {
						treeViewer.setChecked(child, true);
						//child.setChecked(checked);
					}
					treeSet2(child);
				}
			}
		}
	public void loadViewer(OldNode InputTree) {

		Label titleTree = new Label(compositeOfTree, SWT.RIGHT);
		titleTree.setText("工程结构");
		FontData newFontData = titleTree.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(10);
		Font newFont = new Font(this.getShell().getDisplay(), newFontData);
		titleTree.setFont(newFont);

		// 建立布局
		Composite compositeTree = new Composite(compositeOfTree, SWT.NONE);
		compositeTree.setLayout(new FillLayout());
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 500;
		compositeTree.setLayoutData(gridData);

		Label laa = new Label(compositeOfTree, SWT.NONE);
		laa.setText(" ");

		Composite compositeButton = new Composite(compositeOfTree, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.makeColumnsEqualWidth = true;
		compositeButton.setLayout(gridLayout);

		// 组件
		Label lab = new Label(compositeButton, SWT.NONE);
		lab.setText("进行重命名");
		newFontData = lab.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFont = new Font(this.getShell().getDisplay(), newFontData);
		lab.setFont(newFont);

		Button yes = new Button(compositeButton, SWT.RADIO);
		yes.setText("是");
		Button no = new Button(compositeButton, SWT.RADIO);
		no.setText("否");
		yes.setSelection(true);

		treeViewer = new CheckboxTreeViewer(compositeTree, SWT.BORDER | SWT.CHECK);

		tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		// 树型表头
		TreeColumn column = new TreeColumn(tree, SWT.CENTER);
		column.setText("原名称");
		column.setWidth(240);
		column = new TreeColumn(tree, SWT.CENTER);
		column.setText("新名称");
		column.setWidth(140);

		// 设定树的信息
		TreeLabelProvider labelProvider = new TreeLabelProvider();
		treeViewer.setLabelProvider(labelProvider);
		treeViewer.setContentProvider(new TreeContentProvider());
		treeViewer.setInput(InputTree.getChildren());
		setAllTreeChecked(); // 默认全部选中
		treeViewer.expandAll(); // 展开全部

		// 详情界面
		Label Title = new Label(compositeDetail, SWT.CENTER);
		Title.setText("详细信息");
		Title.setText("项目修改对比\n\r");
		newFontData = Title.getFont().getFontData()[0];
		newFontData.setStyle(SWT.BOLD);
		newFontData.setHeight(10);
		newFont = new Font(this.getShell().getDisplay(), newFontData);
		Title.setFont(newFont);

		titleTree.setFont(newFont);
		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		Title.setLayoutData(gridData);

		GridData gd = new GridData();
		gd.widthHint = 180;
		gd.heightHint = 50;
		gd.verticalAlignment = GridData.FILL;
		GridData gdl = new GridData();
		gdl.heightHint = 50;
		gdl.verticalAlignment = GridData.FILL;
		Label namel = new Label(compositeDetail, SWT.NONE);
		namel.setText("名字:");
		namel.setLayoutData(gdl);
		Label preNameShow = new Label(compositeDetail, SWT.LEFT | SWT.HORIZONTAL | SWT.WRAP);
		preNameShow.setText("null");
		preNameShow.setLayoutData(gd);
		preNameShow.pack();

		Label typel = new Label(compositeDetail, SWT.NONE);
		typel.setText("类型:");
		typel.setLayoutData(gdl);
		Label typeShow = new Label(compositeDetail, SWT.LEFT | SWT.HORIZONTAL | SWT.WRAP);
		typeShow.setText("null");
		typeShow.pack();
		typeShow.setLayoutData(gd);

		Label newNamel = new Label(compositeDetail, SWT.NONE);
		newNamel.setText("重命名：");
		newNamel.setLayoutData(gdl);
		Label newNameShow = new Label(compositeDetail, SWT.LEFT | SWT.HORIZONTAL | SWT.WRAP);
		newNameShow.setText("null");
		newNameShow.pack();
		gd.heightHint = 30;
		newNameShow.setLayoutData(gd);

		Label textRemained = new Label(compositeDetail, SWT.NONE);
		textRemained.setText("自定义重命名：");
		gdl = new GridData();
		gdl.heightHint = 20;
		gdl.horizontalSpan = 2;
		textRemained.setLayoutData(gdl);

		Composite compositebu = new Composite(compositeDetail, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.makeColumnsEqualWidth = false;
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.widthHint = 290;
		compositebu.setLayout(gridLayout);
		compositebu.setLayoutData(gridData);

		// 文本框
		Label lsas = new Label(compositebu, SWT.NONE);
		lsas.setText("           ");
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
		Display display1 = Display.getDefault();
		Color color = new Color(display1, 255, 0, 0);
		errorLabel.setForeground(color);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.widthHint = 250;
		gridData.heightHint = 50;
		errorLabel.setLayoutData(gridData);
		compositeDetail.pack();// 换行
		// 实现单选
		
		//取消子节点
		notChoose.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("rawtypes")
			public void widgetSelected(SelectionEvent e) {
				treeViewer.expandAll();
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				Itree domain = (Itree) selection.getFirstElement();
				treeSet(domain,false);
				treeViewer.refresh();
			}
		});
		//选中子节点
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
		// 添加选中事件
		treeViewer.addCheckStateListener(new ICheckStateListener() {
			@SuppressWarnings("rawtypes")
			public void checkStateChanged(CheckStateChangedEvent arg0) {
				CheckboxTreeViewer checkboxTreeViewer = (CheckboxTreeViewer) arg0.getSource();
				boolean checked = arg0.getChecked();

				TreeItem ti = (TreeItem) checkboxTreeViewer.testFindItem(arg0.getElement());
				ti.setChecked(checked);
				Itree cnt = (Itree) ti.getData();
				Display display1 = Display.getDefault();
				Color color = new Color(display1, 230, 230, 255);// 颜色绿色
				if (checked) {
					ti.setBackground(color);
				} else {
					ti.setBackground(null);
				}
				cnt.setChecked(checked);
				if (cnt.tag() == false) {
					ti.setChecked(false);
					cnt.setChecked(false);
					ti.setBackground(null);
				}
				treeViewer.refresh(cnt, true);
			}
		});
//		treeViewer.removeCheckStateListener(new ICheckStateListener() {
//			@SuppressWarnings("rawtypes")
//			@Override
//			public void checkStateChanged(CheckStateChangedEvent arg0) {
//				// TODO Auto-generated method stub
//				
//				CheckboxTreeViewer checkboxTreeViewer = (CheckboxTreeViewer) arg0.getSource();
//				boolean checked = arg0.getChecked();
//				TreeItem ti = (TreeItem) checkboxTreeViewer.testFindItem(arg0.getElement());
//				System.out.println("noew = "+checked);
//				Itree cnt = (Itree) ti.getData();
//				Display display1 = Display.getDefault();
//				Color color = new Color(display1, 230, 230, 255);// 颜色绿色
//				if (!checked && cnt.tag()) {
//					ti.setBackground(color);
//					cnt.setChecked(checked);
//					ti.setChecked(checked);
//				} else {
//					ti.setBackground(null);
//					ti.setChecked(checked);
//				}
//				
//			}
//			
//		});

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
					Itree domain = (Itree) selection.getFirstElement();
					//System.out.println(domain.getChecked()+" "+domain.tag());
					String value = domain.getName();
					nameToShow = value;
					value = domain.getType();
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
				String remain[] = new String[] { "for", "while", "if", "else", "public", "protected", "void", "int",
						"double", "true", "false", "void", "private", "switch", "case", "null", "package", "class",
						"extends", "implement", "boolean", "interface" };
				if (domain != null && setNewName.getText() != null && domain != null && domain.tag() == true) // 改成自定义输入
				{
					String cnt = setNewName.getText();
					boolean isOK = true;
					for (int i = 0; i < cnt.length(); i++) {
						if (Character.isLetterOrDigit(cnt.charAt(i)) || cnt.charAt(i) == '_' || cnt.charAt(i) == '.')
							continue;
						else {
							isOK = false;
							break;
						}
					}
					if (!isOK) {
						errorLabel.setText("输入非法字符,重命名失败！");

					} else {
						int kind = 3;
						for (int i = 0; i < remain.length; i++) {
							if (cnt.equals(remain[i])) {
								kind = 0;
								break;
							}
						}
						if (kind == 0) {
							errorLabel.setText("新名称为java保留字！");

						} else if (kind == 2) {
							errorLabel.setText("自定义命名冲突,重命名失败！");
						} else if (kind == 1) {
							OldNode node = (OldNode) domain;
							domain.setRename(setNewName.getText());
							node.setRename(setNewName.getText());
							newNameShow.setText(setNewName.getText());
							treeViewer.refresh(domain, true);
							errorLabel.setText("重命名成功，但同一作用域内存在命名重复，可能导致错误。");
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

	public boolean getChoose() {
		return isChoose;
	}
}
