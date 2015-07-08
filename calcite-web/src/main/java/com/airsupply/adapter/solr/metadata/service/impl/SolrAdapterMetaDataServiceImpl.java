package com.airsupply.adapter.solr.metadata.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.airsupply.adapter.solr.SolrAdapterUtil;
import com.airsupply.adapter.solr.metadata.service.AdapterMetaDataService;
import com.airsupply.adapter.solr.metadata.vo.ColumnVO;
import com.airsupply.adapter.solr.metadata.vo.TableDesc;
import com.airsupply.calcite.web.constants.CalciteConstants;

public class SolrAdapterMetaDataServiceImpl implements AdapterMetaDataService {
	public static SolrAdapterMetaDataServiceImpl INSTANCE = new SolrAdapterMetaDataServiceImpl();

	@Override
	public TableDesc getTableDesc(String tableName) {
		String configStr = getConfigString(tableName);
		JSONObject js = JSONObject.fromObject(configStr);
		Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
		classMap.put("tableColumns", ColumnVO.class);
		TableDesc tableDesc = (TableDesc) JSONObject.toBean(js,
				TableDesc.class, classMap);
		return tableDesc;
	}

	/**
	 * get solr config path
	 * 
	 * @param tableName
	 * @return
	 */
	public String getConfigString(String tableName) {
		String baseConfigFolder = System
				.getProperty(CalciteConstants.CALCITE_MODEL_PATH);
		if (baseConfigFolder == null) {
			System.err.println("--|calcite.model.path is not set !");
		}
		String solrPath = CalciteConstants.SOLR_PATH;
		String tableDescPath = baseConfigFolder + File.separator + solrPath
				+ File.separator + tableName + ".json";
		String str = SolrAdapterUtil.ReadFile(tableDescPath);
		return str;

	}

}
