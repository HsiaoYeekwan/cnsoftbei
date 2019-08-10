package com.hzy.jdt.view;

import java.util.List;

public interface FileNode<T> {

	public String getName();

	public String getType();

	public String getNewCode();

	public String getOldCode();

	public List<T> getChildren();

}
