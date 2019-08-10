package com.hzy.jdt.ast;

import java.util.ArrayList;
import java.util.List;

import com.hzy.jdt.view.FileNode;


public class NewNode implements FileNode<NewNode>{
	private String type;
	private String name;
	private String oldCode;
	private String newCode;
	private List<NewNode> children;

	public NewNode(String type, String name) {
		this.type = type;
		this.name = name;
		if (type.equals("compilationunit")) {
			this.name = name.substring(0, name.length()-5);
		}
		oldCode = new String();
		newCode = new String();
		children = new ArrayList<NewNode>();
        newCode = "";
        oldCode ="";
	}

	public String getType() {
		
		return type;
	}

	public String getName() {
		return name;
	}

	public String getOldCode() {
		return oldCode;
	}

	public void setOldCode(String s) {
		oldCode = s;
	}

	public String getNewCode() {
		return newCode;
	}

	public void setNewCode(String s) {
		newCode = s;
	}

	public List<NewNode> getChildren() {
		return children;
	}

	public void addChildren(NewNode node) {
		children.add(node);
	}
}
