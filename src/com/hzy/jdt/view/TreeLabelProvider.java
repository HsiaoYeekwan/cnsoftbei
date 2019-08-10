package com.hzy.jdt.view;

import com.hzy.jdt.view.Itree;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class TreeLabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider {
	@SuppressWarnings("rawtypes")
	public String getText(Object element) {
		Itree node = (Itree) element;
		return node.getName();
	}

	public Image getImage(Object element) {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getColumnText(Object arg0, int arg1) {
		Itree node = (Itree) arg0;
		switch (arg1) {
		case 0:
			return node.getName();
		case 1: {
			return node.getRename();
		}
		default:
			return "";
		}
	}

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Color getBackground(Object element) {
		Itree node = (Itree) element;
		Display display1 = Display.getDefault();
		Color blue = new Color(display1, 240, 240, 255);// 颜色浅蓝
		Color purple = new Color(display1, 250, 230, 255);// 颜色浅蓝
		if (node.tag() == true && node.getFlag() == true) {
			return purple;
		} 
		else if (node.getColor().equals("blue")){
			return blue;
		}
		else {
			return null;
		}
	}

	@Override
	public Color getForeground(Object arg0) {
		return null;
	}
}