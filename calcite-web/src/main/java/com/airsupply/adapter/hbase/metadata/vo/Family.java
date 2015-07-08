package com.airsupply.adapter.hbase.metadata.vo;

import java.util.List;

public class Family {

	public String name;
	public List<Column> columns;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

}
