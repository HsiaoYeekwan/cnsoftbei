package com.hzy.jdt.codeformat;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ICompilationUnit;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import com.hzy.antlr.listener.*;

/***************************************************
 * 使用jdt进行基本的代码格式化
 * 使用antlr补全省略的大括号
 ***************************************************/
public class CodeFormat {
	private IWorkspaceRoot myworkspaceroot;

	//获取工作空间
	public CodeFormat(IWorkspaceRoot workspaceroot) {
		myworkspaceroot = workspaceroot;
	}

	//调用start函数进行代码格式化
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
					//从编译单元中获取源码
					String code = myunit.getSource();
					//利用antlr进行大括号补全
					if (flag != 0) {
						ANTLRInputStream input = new ANTLRInputStream(code);
						JavaLexer lexer = new JavaLexer(input);
						CommonTokenStream tokens = new CommonTokenStream(lexer);
						JavaParser parser = new JavaParser(tokens);
						ParseTree tree = parser.compilationUnit();
						ParseTreeWalker walker = new ParseTreeWalker();
						AddBrace listener = new AddBrace(tokens);
						walker.walk(listener, tree);
						code = listener.rewriter.getText();//获取补全大括号后的代码
					}
					//用jdt进行代码格式化
					CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(null);
				    TextEdit textEdit = codeFormatter.format(CodeFormatter.K_UNKNOWN,code,0,code.length(),0,null);
				    IDocument doc = new Document(code);
				    try {
						textEdit.apply(doc);
						myunit.getBuffer().setContents(doc.get());
				    } catch (MalformedTreeException | BadLocationException e) {
						e.printStackTrace();
						
					}
				}
			}
		}
	}
}

