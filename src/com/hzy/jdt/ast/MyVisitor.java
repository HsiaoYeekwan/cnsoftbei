package com.hzy.jdt.ast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class MyVisitor {
	private MyWordHandler myWordHandler;    //文本处理器
	private List<OldNode> stack1;           //模拟栈，传递父节点信息
	private List<String> stack2;            //模拟栈，传递路径信息
	private String unitName;                //unitName作用：重构源文件名后禁止重构其下的public类
	private Map<String, OldNode> map;       //建立新路径与老节点之间的映射关系
	
	public MyVisitor() {
		myWordHandler = new MyWordHandler();//加载文本处理器
		map = new HashMap<String, OldNode> ();
	}
	//文件夹备份
	void copyFolder(String oldPath, String newPath) {
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();
		}
	}
	//获取原始工程结构
	public OldNode loadOldCode(IWorkspaceRoot myworkspaceroot) throws CoreException, JavaModelException {
		stack1 = new ArrayList<OldNode> ();
		stack2 = new ArrayList<String> ();
		unitName = new String();
		OldNode root = new OldNode("workspace", null, null);//建立根节点
		root.setRename("#");//workspace不能重构
		root.setMessage("workspace不能重构");
		//遍历所有工程
		IProject[] myprojects = myworkspaceroot.getProjects();
		for (IProject myproject : myprojects) {
			//只遍历已经打开的java工程
			if (!myproject.isOpen()) continue;
			if (!myproject.isNatureEnabled("org.eclipse.jdt.core.javanature")) continue;
			IJavaProject myjavaproject = JavaCore.create(myproject);
			//存储工程
			copyFolder(myproject.getLocation().toString(), myproject.getLocation().toString()+"备份");
			stack1.add(root); stack2.add("");
			visitProject(myjavaproject);//遍历每个java工程
			stack1.remove(stack1.size()-1); stack2.remove(stack2.size()-1);
		}
		dfs(root);//更新节点的unf值，为前端服务
		return root;
	}

	//遍历每个java工程
	void visitProject(IJavaProject myjavaproject) throws CoreException, JavaModelException {
		OldNode father = stack1.get(stack1.size()-1);
		String path = stack2.get(stack2.size()-1);
		OldNode node = new OldNode("project", myjavaproject, father);
		node.setRename("#");//工程名不能重构
		node.setMessage("工程名不能重构");
		father.addEdge(node);
		node.setPath(path + "/" + node.getName());
		//遍历所有包
		IPackageFragment[] mypackages = myjavaproject.getPackageFragments();
		for (IPackageFragment mypackage : mypackages) {
			//只遍历SOURCE类型的包
			if (mypackage.getKind() != IPackageFragmentRoot.K_SOURCE) continue;
			stack1.add(node); stack2.add(path + "/" + node.getName());
			visitPackage(mypackage);//遍历每个包
			stack1.remove(stack1.size()-1);	stack2.remove(stack2.size()-1);		
		}
	}

	//遍历每个包
	void visitPackage(IPackageFragment mypackage) throws CoreException, JavaModelException {
		OldNode father = stack1.get(stack1.size()-1);
		String path = stack2.get(stack2.size()-1);
		OldNode node = new OldNode("package", mypackage, father);
		if (mypackage.getElementName().equals("")) {
			node.setRename("#");//默认包不能重构
			node.setMessage("默认包名不能重构");
		}
		else {
			String [] str = myWordHandler.renamePackage(node.getName());
			node.setRename(str[0]);
			node.setMessage(str[1]);
			node.setCode(str[2]);
		}
		father.addEdge(node);
		node.setPath(path + "/" + node.getName());
		//遍历所有源文件
		ICompilationUnit[] myunits = mypackage.getCompilationUnits();
		for (ICompilationUnit myunit : myunits) {
			stack1.add(node); stack2.add(path + "/" + node.getName());
			visitCompilationUnit(myunit);//遍历每个源文件
			stack1.remove(stack1.size()-1);	stack2.remove(stack2.size()-1);
		}
	}
	
	//遍历每个源文件
	void visitCompilationUnit(ICompilationUnit myunit) {
		OldNode father = stack1.get(stack1.size()-1);
		String path = stack2.get(stack2.size()-1);
		OldNode node = new OldNode("compilationunit", myunit, father);
		String [] str = myWordHandler.renameCompilationUnit(node.getName());
		node.setRename(str[0]);
		node.setMessage(str[1]);
		node.setColor(str[2]);
		father.addEdge(node);
		node.setPath(path + "/" + node.getName());
		//获取源码
		try {
			node.setCode(myunit.getSource());
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		map.put(path + "/" + node.getName(), node);
		unitName = node.getName();//获取源文件名
		//生成该源文件的AST
		ASTParser parser = ASTParser.newParser(AST.JLS9);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(myunit);
		parser.setResolveBindings(true);
		CompilationUnit myparser = (CompilationUnit) parser.createAST(null);
	    stack1.add(node); stack2.add(path + "/" + node.getName());
		myparser.accept(new VisitType());//使用visitor模式来访问AST
		stack1.remove(stack1.size()-1); stack2.remove(stack2.size()-1);
	}
	
	//使用visitor模式来访问AST
	class VisitType extends ASTVisitor {
		//访问类型，包括枚类、接口、枚举
		//访问类、接口
		@Override public boolean visit(TypeDeclaration type) {
			OldNode father = stack1.get(stack1.size()-1);
			String path = stack2.get(stack2.size()-1);
			IType mytype = (IType) type.resolveBinding().getJavaElement();
			OldNode node = new OldNode("type", mytype, father);
			try {
				//当前源文件下的public类不能重构
				if (node.getName().equals(unitName)) {
					node.setRename("#");
					node.setMessage("源文件下的public类不能重构");
				}
				else {
					//泛型要特殊重构
					if (mytype.getTypeParameters().length == 0) {
						String [] str = myWordHandler.renameNormalType(node.getName());
						node.setRename(str[0]);
						node.setMessage(str[1]);
						node.setColor(str[2]);
					}
					else {
						String [] str = myWordHandler.renameSpecialType(node.getName());
						node.setRename(str[0]);
						node.setMessage(str[1]);
						node.setColor(str[2]);
					}
				}
				father.addEdge(node);
				node.setPath(path + "/" + node.getName());
				//访问所有成员变量
				IField[] myfields = mytype.getFields();
				for (IField myfield : myfields) {
					stack1.add(node); stack2.add(path + "/" + node.getName());
					VisitField(myfield);//访问每个成员变量
					stack1.remove(stack1.size()-1); stack2.remove(stack2.size()-1);
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
			stack1.add(node);
			stack2.add(path + "/" + node.getName());
			return super.visit(type);
		}
		@Override public void endVisit(TypeDeclaration type) {
			stack1.remove(stack1.size()-1);
			stack2.remove(stack2.size()-1);
		}
		
		//访问枚举
		@Override public boolean visit(EnumDeclaration type) {
			OldNode father = stack1.get(stack1.size()-1);
			String path = stack2.get(stack2.size()-1);
			IType mytype = (IType) type.resolveBinding().getJavaElement();
			OldNode node = new OldNode("type", mytype, father);
			try {
				if (node.getName().equals(unitName)) {
					node.setRename("#");
					node.setMessage("源文件下的public类不能重构");
				}
				else {
					if (mytype.getTypeParameters().length == 0) {
						String [] str = myWordHandler.renameNormalType(node.getName());
						node.setRename(str[0]);
						node.setMessage(str[1]);
						node.setColor(str[2]);
					}
					else {
						String [] str = myWordHandler.renameSpecialType(node.getName());
						node.setRename(str[0]);
						node.setMessage(str[1]);
						node.setColor(str[2]);
					}
				}
				father.addEdge(node);
				node.setPath(path + "/" + node.getName());
				//访问所有成员变量
				IField[] myfields = mytype.getFields();
				for (IField myfield : myfields) {
					stack1.add(node); stack2.add(path + "/" + node.getName());
					VisitField(myfield);//访问每个成员变量
					stack1.remove(stack1.size()-1); stack2.remove(stack2.size()-1);
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
			stack1.add(node);
			stack2.add(path + "/" + node.getName());
			return super.visit(type);
		}
		@Override public void endVisit(EnumDeclaration type) {
			stack1.remove(stack1.size()-1);
			stack2.remove(stack2.size()-1);
		}
		
		//访问每个成员变量
		void VisitField(IField myfield) {
			OldNode father = stack1.get(stack1.size()-1);
			String path = stack2.get(stack2.size()-1);
			OldNode node = new OldNode("field", myfield, father);
			try {
				//判断是否为常量
				int flag = myfield.getFlags();
				if ((Flags.isStatic(flag) && Flags.isFinal(flag)) || Flags.isEnum(flag)) {
					String [] str = myWordHandler.renameConstance(node.getName());
					node.setRename(str[0]);
					node.setMessage(str[1]);
					node.setColor(str[2]);
				} else {
					String [] str = myWordHandler.renameField(node.getName());
					node.setRename(str[0]);
					node.setMessage(str[1]);
					node.setColor(str[2]);
				}
				father.addEdge(node);
				node.setPath(path + "/" + node.getName());
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		
		//访问函数
		@Override public boolean visit(MethodDeclaration method) {
			OldNode father = stack1.get(stack1.size()-1);
			String path = stack2.get(stack2.size()-1);
			IMethod mymethod = (IMethod) method.resolveBinding().getJavaElement();
			OldNode node = new OldNode("method", mymethod, father);
			try {
				//main函数不能重构
				if (mymethod.getElementName().equals("main") || mymethod.isConstructor()) {
					node.setRename("#");
					node.setMessage("main函数不能重构");
				}
				else {
					String [] str = myWordHandler.renameMethod(node.getName());
					node.setRename(str[0]);
					node.setMessage(str[1]);
					node.setColor(str[2]);
				}
				father.addEdge(node);
				node.setPath(path + "/" + node.getName());
				
				//访问所有参数
				ILocalVariable[] myparameters = mymethod.getParameters();
				for (ILocalVariable myparameter : myparameters) {
					stack1.add(node); stack2.add(path + "/" + node.getName());
					visitParameter(myparameter);//访问每个参数
					stack1.remove(stack1.size()-1); stack2.remove(stack2.size()-1);
				}
				
				//访问局部变量
				stack1.add(node);
				stack2.add(path + "/" + node.getName());
				method.accept(new ASTVisitor() {
					@Override public boolean visit(VariableDeclarationFragment localvariable) {
						OldNode fa = stack1.get(stack1.size()-1);
						String pa = stack2.get(stack2.size()-1);
						ILocalVariable mylocalvariable = (ILocalVariable)localvariable.resolveBinding().getJavaElement();
						OldNode no = new OldNode("localvariable", mylocalvariable, fa);
						String [] str = myWordHandler.renameLocalVariable(no.getName());
						no.setRename(str[0]);
						no.setMessage(str[1]);
						no.setColor(str[2]);
						fa.addEdge(no);
						no.setPath(pa + "/" + no.getName());
						return false;
					}
				});
				stack1.remove(stack1.size()-1);
				stack2.remove(stack2.size()-1);
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		//访问每个参数
		void visitParameter(ILocalVariable myparameter) {
			OldNode father = stack1.get(stack1.size()-1);
			String path = stack2.get(stack2.size()-1);
			OldNode node = new OldNode("parameter", myparameter, father);
			String [] str = myWordHandler.renameParameter(node.getName());
			node.setRename(str[0]);
			node.setMessage(str[1]);
			node.setColor(str[2]);
			father.addEdge(node);
			node.setPath(path + "/" + node.getName());
		}
	}
	
	//更新路径、映射表
	public void update(OldNode node, String path) {
		String oldPath = node.getPath();
		if (!oldPath.equals(path)) {
			if (node.getType().equals("compilationunit")) {
				map.remove(oldPath);
				map.put(path, node);
				node.setPath(path);
			}
			else {
				node.setPath(path);
			}
		}
		List<OldNode> nodeList = node.getEdges();
		for (OldNode child : nodeList) {
			update(child, path + "/" + child.getName());
		}
	}
	
	//加载更新后的工程结构
	public NewNode loadNewCode(IWorkspaceRoot myworkspaceroot) throws CoreException, JavaModelException {
		NewNode root = new NewNode("workspace", "");// 创建根节点
		// 访问工作空间下的所有工程
		IProject[] myprojects = myworkspaceroot.getProjects();
		for (IProject myproject : myprojects) {
			// 判断是否为java工程
			if (!myproject.isOpen())
				continue;
			if (!myproject.isNatureEnabled("org.eclipse.jdt.core.javanature"))
				continue;
			IJavaProject myjavaproject = JavaCore.create(myproject);

			NewNode projectNode = new NewNode("project", myjavaproject.getElementName());
			root.addChildren(projectNode);
			// 访问工程下的所有包
			IPackageFragment[] mypackages = myjavaproject.getPackageFragments();
			for (IPackageFragment mypackage : mypackages) {
				// 判断是否为source类型
				if (mypackage.getKind() != IPackageFragmentRoot.K_SOURCE)
					continue;
               String pname = mypackage.getElementName();
               NewNode packageNode;
               if(pname.equals(""))
            	   packageNode = new NewNode("package", "default");
               else
				   packageNode = new NewNode("package", mypackage.getElementName());
				projectNode.addChildren(packageNode);
				// 访问包下的所有源文件
				for (ICompilationUnit myunit : mypackage.getCompilationUnits()) {

					NewNode unitNode = new NewNode("compilationunit", myunit.getElementName());
					packageNode.addChildren(unitNode);
					String path = "/" + projectNode.getName() + "/" + packageNode.getName() + "/" + unitNode.getName();
					unitNode.setOldCode(map.get(path).getCode());
					unitNode.setNewCode(myunit.getSource());
				}
			}
		}
		return root;
	}
	
	boolean dfs(OldNode node) {
		boolean ok = false;
		List<OldNode> nodeList = node.getEdges();
		for (OldNode child : nodeList) {
			if (dfs(child)) {
				ok = true;
			}
		}
		node.setUnf(ok);
		if(node.getFlag()) {
			ok = true;
		}
		return ok;
	}
}