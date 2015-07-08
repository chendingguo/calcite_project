package com.airsupply.adapter.hbase.metadata.vo;

import java.io.Serializable;
/**
 * column vo
 * @author arisupply
 *
 */
public class Column implements Serializable{

	private static final long serialVersionUID = -1694821389447760134L;
	public String name;
	public String type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

}
