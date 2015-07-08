package com.airsupply.calcite.web.service;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.airsupply.calcite.web.constants.CalciteConstants;
import com.airsupply.calcite.web.model.TreeModel;
import com.airsupply.calcite.web.util.CalciteUtil;
@Service("calciteSearchService")
public class CalciteSearchService {

	private void close(Connection connection, Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// ignore
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// ignore
			}
		}
	}

	public Map<String,Object> select(String modelName, String sql) {
		System.setProperty("hadoop.home.dir", "D:/develope/hadoop-2.4.0");
		Connection connection = null;
		Statement statement = null;
		Map<String,Object> resultMap=new HashMap<String,Object>();
		List<Map<String,String>> rows = new ArrayList<Map<String,String>>();
		Properties info = new Properties();
		info.put("model", CalciteUtil.jsonPath(modelName));
		String driver = "org.apache.calcite.jdbc.Driver";
		try {
			Class.forName(driver);
			
			connection = DriverManager.getConnection("jdbc:calcite:", info);
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			final ResultSetMetaData metaData = resultSet.getMetaData();
			final int columnCount = metaData.getColumnCount();
			while (resultSet.next()) {
				Map<String,String> row = new LinkedHashMap<String,String>();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnName(i);
					String value = "";
					if (resultSet.getObject(i) != null) {
						value = String.valueOf(resultSet.getObject(i));
					}
					row.put(columnName, value);
				}
				rows.add(row);
			}

			resultMap.put(CalciteConstants.RESULT_DATA, rows);
			//resultMap.put(CalciteConstants.RESULT_TOTAL, rows.size());
			//resultMap.put(CalciteConstants.RESULT_PAGES, 10);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(connection, statement);
		}
		return resultMap;

	}
	
	public TreeModel getConfigFileTree(){
		String configPath= System.getProperty(CalciteConstants.CALCITE_MODEL_PATH);
		File f = new File(configPath);
		CalciteUtil.getFileTree(f);
		return CalciteUtil.treeModel;
	}
	
	

}
