package com.hzy.jdt.view;

import java.util.List;

public interface Itree<T> {
	public String getName();

	public String getType();

	public String getRename();

	public String getPath();

	public boolean tag();

	public void setRename(String rename);

	public boolean getFlag();
	public boolean getUnf();
	public String getColor();
	public String getMessage();

	public boolean isPreChecked();

	public void setFlag(boolean checked);

	public List<T> getChildren();
}
