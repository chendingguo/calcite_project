package com.airsupply.calcite.web.model;

import java.util.List;

public class TreeModel {
	public String id;
	public String text;
	public String value;
	public boolean showcheck;
	public boolean isexpand;
	public int checkstate;
	public boolean hasChildren;
	public List<TreeModel> ChildNodes;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isShowcheck() {
		return showcheck;
	}
	public void setShowcheck(boolean showcheck) {
		this.showcheck = showcheck;
	}
	public boolean isIsexpand() {
		return isexpand;
	}
	public void setIsexpand(boolean isexpand) {
		this.isexpand = isexpand;
	}
	public int getCheckstate() {
		return checkstate;
	}
	public void setCheckstate(int checkstate) {
		this.checkstate = checkstate;
	}
	public boolean isHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	public List<TreeModel> getChildNodes() {
		return ChildNodes;
	}
	public void setChildNodes(List<TreeModel> childNodes) {
		ChildNodes = childNodes;
	}
	
	
}
