package com.airsupply.adapter.hbase.metadata.service;

import com.airsupply.adapter.hbase.metadata.vo.TableDesc;




public interface AdapterMetaDataService {
	/**
	 * get table descibe
	 * @param tableName
	 * @return
	 */
	public TableDesc getTableDesc(String tableName);
	

}
