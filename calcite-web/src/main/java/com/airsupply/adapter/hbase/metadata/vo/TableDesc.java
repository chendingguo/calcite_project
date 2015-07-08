package com.airsupply.adapter.hbase.metadata.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * table meta data describe
 * @author arisupply
 *
 */
public class TableDesc {
	public Map<String,String> connInfo=new HashMap<String,String>();
	public Map<String,String> queryFilter=new HashMap<String,String>();
	public List<Family> families=new ArrayList<Family>();
	public String rowKeyName="ROWKEY";
	public Map<String, String> getConnInfo() {
		return connInfo;
	}
	public void setConnInfo(Map<String, String> connInfo) {
		this.connInfo = connInfo;
	}
	
	
	public List<Family> getFamilies() {
		return families;
	}
	public void setFamilies(List<Family> families) {
		this.families = families;
	}
	public Map<String, String> getQueryFilter() {
		return queryFilter;
	}
	public void setQueryFilter(Map<String, String> queryFilter) {
		this.queryFilter = queryFilter;
	}
	public String getRowKeyName() {
		return rowKeyName;
	}
	public void setRowKeyName(String rowKeyName) {
		this.rowKeyName = rowKeyName;
	}
	
	
	
	

}
