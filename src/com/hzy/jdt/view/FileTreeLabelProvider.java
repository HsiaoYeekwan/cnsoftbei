package com.hzy.jdt.view;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.hzy.jdt.ast.NewNode;

public class FileTreeLabelProvider extends LabelProvider implements ILabelProvider, IColorProvider {
	public FileTreeLabelProvider() {

	}

	@SuppressWarnings("rawtypes")
	public String getText(Object element) {
		FileNode node = (FileNode) element;
		return node.getName();
	}

	@Override
	public Color getBackground(Object arg0) {
		NewNode node = (NewNode) arg0;
		Display display1 = Display.getDefault();
		Color color = new Color(display1, 230, 230, 255);// 颜色浅黄
		if (node.getNewCode().length() != 0 && !node.getNewCode().equals(node.getOldCode()))
			return color;
		else
			return null;
	}

	@Override
	public Color getForeground(Object arg0) {
		return null;
	}

}
