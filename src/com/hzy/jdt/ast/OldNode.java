package com.hzy.jdt.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;

import com.hzy.jdt.view.Itree;

public class OldNode implements Itree<OldNode> {
	private boolean flag;          //由用户决定是否重构
	private String type;           //需要重构的element的类型
	private IJavaElement element;  //需要重构的element
	private String name;           //重构前的名字
	private String rename;         //重构后的名字，不能重构则设置为#
	private String message;        //存储命名不规范的警告信息
	private String color;          //存储前端要显示的颜色
	private boolean unf;           //当前节点的子节点在前端中是否需要展开
	private List<OldNode> edges;   //存储边
	private OldNode parent;        //存储每个节点的父节点
	private String path;           //存储每个节点的路径
	private String code;           //对于每个源文件存储原始代码

	public OldNode (String type0, IJavaElement element0, OldNode parent0) {
		flag = true;
		type = type0;
		element = element0;
		if (element0 != null) {
			name = element0.getElementName();		
			if (type0.equals("compilationunit")) {
				name = name.substring(0, name.length()-5);//获取源文件名的函数要特殊处理一下
			}
		}
		else {
			name = new String();
		}
		rename = new String();
		message = new String();
		color = "default";
		unf = true;
		edges = new ArrayList<OldNode> ();
		this.parent = parent0;
		path = new String();
		code = new String();
	}
	public boolean getFlag() {
		return flag;
	}
	public void setFlag(boolean x) {
		flag = x;
	}
	public String getType() {
		  return type;
	}
	public IJavaElement getElement() {
		return element;
	}
	public void setName(String s) {
		name = s;
	}
	public String getName() {
		return name;
	}
	public String getRename() {
		return rename;
	}
	public void setRename(String s) {
		if (name.equals(s)) {
			flag = false;
		}
		rename = s;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String s) {
		message = s;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String s) {
		color = s;
	}
	public boolean getUnf() {
		return unf;
	}
	public void setUnf(boolean x) {
		unf = x;
	}
	public List<OldNode> getEdges() {
		return edges;
	}
	public void addEdge(OldNode x) {
		edges.add(x);
	}
	public OldNode getParent() {
		return parent;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String s) {
		code = s;
	}
	@Override
	public String getPath() {
		return path;
	}
	public void setPath(String s) {
		path = s;
	}
	@Override
	public boolean tag() {
		if(element != null && !rename.equals("#") ) {
			return true;
		}
		return false;
	}
	@Override
	public boolean isPreChecked() { //不需要选择
		if(element != null && !rename.equals("#") && flag) {
			return true;
		}
		return false;
		
	}
	@Override
	public List<OldNode> getChildren() {
		return edges;
	}
    public int haveSameName(String newname) {
    	int have = 0;
    	if(parent!=null) {
    		for(OldNode x:parent.getEdges()) {
    			String xname = x.getName();
    			if(x.tag() == true && x.getFlag() == true)
    	    		 xname = x.getRename();
    			if(xname.equals(newname)) {
    				if(x.type.equals("localvariable")||x.type.equals("method"))
    				  have = 1;
    				else
    					return 2;
    				break;
    			}
    		}
    	}
    	if(have == 0) {
    		return 0;//可以修改
    	}
    	if(type.equals("localvariable")||type.equals("method")) {
    		return 1;//可以修改，有重复
    	}
    	return 2;//不能改
    }
}
