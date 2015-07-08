package com.airsupply.adapter.solr.metadata.service;

import com.airsupply.adapter.solr.metadata.vo.TableDesc;

public interface AdapterMetaDataService {
	/**
	 * get table descibe
	 * @param tableName
	 * @return
	 */
	public TableDesc getTableDesc(String tableName);
	

}
