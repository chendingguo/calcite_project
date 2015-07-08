package com.airsupply.adapter.hbase.metadata.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.airsupply.adapter.hbase.HbaseAdapterUtil;
import com.airsupply.adapter.hbase.metadata.MetaDataManager;
import com.airsupply.adapter.hbase.metadata.service.AdapterMetaDataService;
import com.airsupply.adapter.hbase.metadata.vo.Column;
import com.airsupply.adapter.hbase.metadata.vo.Family;
import com.airsupply.adapter.hbase.metadata.vo.TableDesc;
import com.airsupply.calcite.web.constants.CalciteConstants;

public class HbaseAdapterMetaDataServiceImpl implements AdapterMetaDataService {
	public static HbaseAdapterMetaDataServiceImpl INSTANCE = new HbaseAdapterMetaDataServiceImpl();

	@Override
	public TableDesc getTableDesc(String tableName) {
		String configStr = getConfigString(tableName);
		JSONObject js = JSONObject.fromObject(configStr);
		Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
		classMap.put("families", Family.class);
		classMap.put("columns", Column.class);
		TableDesc tableDesc = (TableDesc) JSONObject.toBean(js,
				TableDesc.class, classMap);
		return tableDesc;
	}

	public String getConfigString(String tableName) {
		String baseConfigFolder = System
				.getProperty(CalciteConstants.CALCITE_MODEL_PATH);
		if (baseConfigFolder == null) {
			System.err.println("--|calcite.model.path is not set !");
		}
		String hbasePath = CalciteConstants.HBASE_PATH;
		String tableDescPath = baseConfigFolder + File.separator + hbasePath
				+ File.separator + tableName + ".json";
		String str = HbaseAdapterUtil.ReadFile(tableDescPath);
		return str;

	}

	public static void main(String[] args) {
		System.setProperty("calcite.config.hbase.path",
				"E:/mend_fold/calcite-test/src/test/resources/hbase");

		List<Column> columns = MetaDataManager.getColumns("BLOG");

		for (Column column : columns) {
			System.out.println(column.getName() + " " + column.getType());
		}
	}

}