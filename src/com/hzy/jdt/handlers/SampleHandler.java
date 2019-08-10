package com.hzy.jdt.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hzy.jdt.ast.*;
import com.hzy.jdt.ast.MyVisitor;
import com.hzy.jdt.view.MainViewer;

public class SampleHandler extends AbstractHandler {
	@Override
	//相当于插件的main函数
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//System.out.println("--------------------START--------------------");
		//获取工作空间
		IWorkspaceRoot myworkspaceroot = ResourcesPlugin.getWorkspace().getRoot();
		
		try {
			MyVisitor myvisitor = new MyVisitor();
			OldNode oldRoot = myvisitor.loadOldCode(myworkspaceroot);
			MessageDialog.openInformation(null, "", "初始化完成，所有工程已备份到该工程所在目录下，命名格式为\"工程名+备份\"");
			MainViewer mainViewer = new MainViewer();
			mainViewer.run(oldRoot,myvisitor);
		} catch (CoreException e2) {
			e2.printStackTrace();
		}
		
		//System.out.println("---------------------END---------------------");
		return null;
	}
	
}