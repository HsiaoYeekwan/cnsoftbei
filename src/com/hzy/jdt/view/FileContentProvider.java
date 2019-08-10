package com.hzy.jdt.view;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class FileContentProvider implements IStructuredContentProvider, ITreeContentProvider {

	@SuppressWarnings("rawtypes")
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			List input = (List) inputElement;
			return input.toArray();
		}
		return new Object[0];
	}

	@SuppressWarnings("rawtypes")
	public Object[] getChildren(Object parentElement) {
		FileNode node = (FileNode) parentElement;
		List list = node.getChildren();
		if (list == null) {
			return new Object[0];
		}
		return list.toArray();
	}

	@SuppressWarnings("rawtypes")
	public boolean hasChildren(Object element) {
		FileNode node = (FileNode) element;
		List list = node.getChildren();
		return !(list == null || list.isEmpty());
	}

	//
	public Object getParent(Object element) {
		return null;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	public void dispose() {
	}
}
