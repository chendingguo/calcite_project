package com.airsupply.adapter.solr.metadata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.airsupply.adapter.solr.metadata.service.AdapterMetaDataService;
import com.airsupply.adapter.solr.metadata.service.impl.SolrAdapterMetaDataServiceImpl;
import com.airsupply.adapter.solr.metadata.vo.TableDesc;

public class MetaDataManager {

	public static Map<String, TableDesc> tableDescs = new ConcurrentHashMap<String, TableDesc>();

	public static TableDesc getTableDesc(String tableName) {
		if (tableDescs.get(tableName) == null) {
			putTableDesc(tableName);
		}
		return tableDescs.get(tableName);

	}

	public static void putTableDesc(String tableName) {
		AdapterMetaDataService metaDataService = SolrAdapterMetaDataServiceImpl.INSTANCE;
		TableDesc tableDesc=metaDataService.getTableDesc(tableName);
		tableDescs.put(tableName, tableDesc);

	}

}
