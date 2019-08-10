package com.hzy.jdt.statementchange;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ICompilationUnit;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;

import com.hzy.antlr.visitor.*;

/***************************************************
 * 利用antlr实现：
 * 1：for转while
 * 2：if-else转switch-case
 * 3：switch-case转if-else
 * 4：if嵌套合并为单if
 ***************************************************/
public class StatementChange {
	IWorkspaceRoot myworkspaceroot;

	//获取工作空间
	public StatementChange(IWorkspaceRoot workspaceroot) {
		myworkspaceroot = workspaceroot;
	}

	public void start(int flag) throws CoreException, JavaModelException {
		//访问工程
		IProject[] myprojects = myworkspaceroot.getProjects();
		for (IProject myproject : myprojects) {
			//判断是否为java工程
			if(!myproject.isOpen()) continue;
			if (!myproject.isNatureEnabled("org.eclipse.jdt.core.javanature")) continue;
			IJavaProject myjavaproject = JavaCore.create(myproject);
			//访问包
			IPackageFragment[] mypackages = myjavaproject.getPackageFragments();
			for (IPackageFragment mypackage : mypackages) {
				//判断是否为源文件
				if (mypackage.getKind() != IPackageFragmentRoot.K_SOURCE) continue;
				//访问编译单元
				for (ICompilationUnit myunit : mypackage.getCompilationUnits()) {
					//使用antlr的visitor模式进行等价语句转换
					String source = myunit.getSource();
					switch (flag) {
					case 1:
						source = ChangeForToWhile(source);
						break;
					case 2:
						source = ChangeIfToSwitch(source);
						break;
					case 3:
						source = ChangeSwitchToIf(source);
						break;
					default:
						source = ChangeIfNesting(source);
					}
					String code = source;
					//System.out.println("!!!!!!!!!!!!!!!!!-------!!!"+source);
				    IDocument doc = new Document(code);
				    try {
						myunit.getBuffer().setContents(doc.get());
				    } catch (MalformedTreeException e) {
						e.printStackTrace();
//						while(true) {
//							;
//						}
					}
				}
			}
		}
	}
	
	String ChangeForToWhile(String s) {
		ANTLRInputStream input = new ANTLRInputStream(s);
		JavaLexer lexer = new JavaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JavaParser parser = new JavaParser(tokens);
		ParseTree tree = parser.compilationUnit();
		ChangeForToWhile visitor = new ChangeForToWhile(tokens);
		visitor.visit(tree);
		return visitor.rewriter.getText();
	}
	
	String ChangeIfToSwitch(String s) {
		ANTLRInputStream input = new ANTLRInputStream(s);
		JavaLexer lexer = new JavaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JavaParser parser = new JavaParser(tokens);
		ParseTree tree = parser.compilationUnit();
		ChangeIfToSwitch visitor = new ChangeIfToSwitch(tokens);
		visitor.visit(tree);
		return visitor.rewriter.getText();
	}
	
	String ChangeSwitchToIf(String s) {
		ANTLRInputStream input = new ANTLRInputStream(s);
		JavaLexer lexer = new JavaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JavaParser parser = new JavaParser(tokens);
		ParseTree tree = parser.compilationUnit();
		ChangeSwitchToIf visitor = new ChangeSwitchToIf(tokens);
		visitor.visit(tree);
		return visitor.rewriter.getText();
	}
	
	String ChangeIfNesting(String s) {
		ANTLRInputStream input = new ANTLRInputStream(s);
		JavaLexer lexer = new JavaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JavaParser parser = new JavaParser(tokens);
		ParseTree tree = parser.compilationUnit();
		ChangeIfNesting visitor = new ChangeIfNesting(tokens);
		visitor.visit(tree);
		return visitor.rewriter.getText();
	}
}
