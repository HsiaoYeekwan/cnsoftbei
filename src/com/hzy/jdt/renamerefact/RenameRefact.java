package com.hzy.jdt.renamerefact;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ILocalVariable;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.hzy.jdt.ast.OldNode;

import org.eclipse.jdt.ui.refactoring.RenameSupport;

public class RenameRefact {
	
	public void start(OldNode u) {
		List<OldNode> edges = u.getEdges();
		for (OldNode v : edges) {
			start(v);
		}
		rename(u);//一定要在回溯的时候修改，因为先修改父亲的话孩子的重命名会出错。
	}
	
	void rename(OldNode node) {
		if (!node.getFlag()) return; //flag为false表示用户不希望对此element重命名
		if (node.getElement() == null) return; //根节点的element为空，特判
		String type = node.getType();
		IJavaElement element = node.getElement();
		String name = node.getName();
		String rename = node.getRename();
		if (rename.equals("#")) return; //重命名为#表示禁止对此element重命名
		if (name.equals(rename)) return; //重构后没有变化则不需要进行重命名
		
		//根据type来进行相应的重命名
		if (type.equals("package")) {
			renamePackage((IPackageFragment)element, rename, node);
		}
		else if (type.equals("compilationunit")) {
			renameCompilationUnit((ICompilationUnit)element, rename, node);
		}
		else if (type.equals("type")) {
			renameType((IType)element, rename, node);
		}
		else if (type.equals("field")) {
			renameField((IField)element, rename, node);
		}
		else if (type.equals("method")) {
			renameMethod((IMethod)element, rename, node);
		}
		else if (type.equals("parameter") || type.equals("localvariable")) {
			renameLocalVariable((ILocalVariable)element, rename, node);
		}
	}
	
	void renamePackage(IPackageFragment mypackage, String rename, OldNode node) {
		try {
			RenameSupport renameSupport = RenameSupport.create(mypackage, rename, RenameSupport.UPDATE_REFERENCES);
			IWorkbench workBench = PlatformUI.getWorkbench();
			renameSupport.perform(workBench.getActiveWorkbenchWindow().getShell(), workBench.getProgressService());
			node.setName(rename);
			node.setRename("");
		} catch (CoreException | InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void renameCompilationUnit(ICompilationUnit myunit, String rename, OldNode node) {
		try {
			RenameSupport renameSupport = RenameSupport.create(myunit, rename, RenameSupport.UPDATE_REFERENCES);
			IWorkbench workBench = PlatformUI.getWorkbench();
			renameSupport.perform(workBench.getActiveWorkbenchWindow().getShell(), workBench.getProgressService());
			node.setName(rename);
			node.setRename("");
		} catch (CoreException | InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void renameType(IType mytype, String rename, OldNode node) {
		try {
			RenameSupport renameSupport = RenameSupport.create(mytype, rename, RenameSupport.UPDATE_REFERENCES);
			IWorkbench workBench = PlatformUI.getWorkbench();
			renameSupport.perform(workBench.getActiveWorkbenchWindow().getShell(), workBench.getProgressService());
			node.setName(rename);
			node.setRename("");
		} catch (CoreException | InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void renameField(IField myfield, String rename, OldNode node) {
		try {
			RenameSupport renameSupport = RenameSupport.create(myfield, rename, RenameSupport.UPDATE_REFERENCES);
			IWorkbench workBench = PlatformUI.getWorkbench();
			renameSupport.perform(workBench.getActiveWorkbenchWindow().getShell(), workBench.getProgressService());
			node.setName(rename);
			node.setRename("");
		} catch (CoreException | InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void renameMethod(IMethod mymethod, String rename, OldNode node) {
		try {
			RenameSupport renameSupport = RenameSupport.create(mymethod, rename, RenameSupport.UPDATE_REFERENCES);
			IWorkbench workBench = PlatformUI.getWorkbench();
			renameSupport.perform(workBench.getActiveWorkbenchWindow().getShell(), workBench.getProgressService());
			node.setName(rename);
			node.setRename("");
		} catch (CoreException | InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void renameLocalVariable(ILocalVariable mylocalvariable, String rename, OldNode node) {
		try {
			RenameSupport renameSupport = RenameSupport.create(mylocalvariable, rename, RenameSupport.UPDATE_REFERENCES);
			IWorkbench workBench = PlatformUI.getWorkbench();
			renameSupport.perform(workBench.getActiveWorkbenchWindow().getShell(), workBench.getProgressService());
			node.setName(rename);
			node.setRename("");
		} catch (CoreException | InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
