package com.airsupply.adapter.solr.metadata.vo;

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
	public Map<String,Object> connInfo=new HashMap<String,Object>();
	public Map<String,String> queryFilter=new HashMap<String,String>();
	public List<ColumnVO>tableColumns=new ArrayList<ColumnVO>();
	public Map<String, Object> getConnInfo() {
		return connInfo;
	}
	public void setConnInfo(Map<String, Object> connInfo) {
		this.connInfo = connInfo;
	}
	public List<ColumnVO> getTableColumns() {
		return tableColumns;
	}
	public void setTableColumns(List<ColumnVO> tableColumns) {
		this.tableColumns = tableColumns;
	}
	public Map<String, String> getQueryFilter() {
		return queryFilter;
	}
	public void setQueryFilter(Map<String, String> queryFilter) {
		this.queryFilter = queryFilter;
	}
	
	
	

}
