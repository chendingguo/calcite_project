package com.airsupply.adapter.hbase.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.airsupply.adapter.hbase.metadata.service.AdapterMetaDataService;
import com.airsupply.adapter.hbase.metadata.service.impl.HbaseAdapterMetaDataServiceImpl;
import com.airsupply.adapter.hbase.metadata.vo.Column;
import com.airsupply.adapter.hbase.metadata.vo.Family;
import com.airsupply.adapter.hbase.metadata.vo.TableDesc;



public class MetaDataManager {

	public static Map<String, TableDesc> tableDescs = new ConcurrentHashMap<String, TableDesc>();

	public static TableDesc getTableDesc(String tableName) {
		if (tableDescs.get(tableName) == null) {
			putTableDesc(tableName);
		}
		return tableDescs.get(tableName);

	}

	public static void putTableDesc(String tableName) {
		AdapterMetaDataService metaDataService = HbaseAdapterMetaDataServiceImpl.INSTANCE;
		TableDesc tableDesc=metaDataService.getTableDesc(tableName);
		tableDescs.put(tableName, tableDesc);

	}
	
	public static List<Column> getColumns(String tableName){
		List<Column> columns=new ArrayList<Column>();
		
		
		TableDesc tableDesc=getTableDesc(tableName);
		//row key generate
		//-------------------------------------------
		String rowKeyName=tableDesc.getRowKeyName();
		Column rowKeyCol=new Column();
		rowKeyCol.setName(rowKeyName);
		rowKeyCol.setType("string");
		columns.add(rowKeyCol);
		//-------------------------------------------
		List<Family> families=tableDesc.getFamilies();
		for(Family family:families){
			String familyName=family.getName();
		     List<Column> cols=family.getColumns();
		     for(Column col:cols){
		    	 Column column=new Column();
		    	 String distinctName=familyName+"_"+col.getName();
		    	 column.setName(distinctName.toUpperCase());
		    	 column.setType(col.getType());
		    	 columns.add(column);
		     }
		}
		return columns;
		
	}

}
