package com.airsupply.hbase.adapter.test;

import java.io.PrintStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.calcite.linq4j.function.Function1;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.airsupply.calcite.web.util.CalciteUtil;

/**
 * Unit test of the Calcite adapter for HBase.
 */
public class HbaseAdapterTest {
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

	public static String toLinux(String s) {
		return s.replaceAll("/r/n", "/n");
	}

	private void checkSql(String model, String sql) throws SQLException {
		
		checkSql(sql, model, output());
	}

	private Function1<ResultSet, Void> output() {
		return new Function1<ResultSet, Void>() {
			public Void apply(ResultSet resultSet) {
				try {
					output(resultSet, System.out);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		};
	}

	@SuppressWarnings("unused")
	private void checkSql(String model, String sql, final String... expected)
			throws SQLException {
		checkSql(sql, model, expect(expected));
	}

	/**
	 * Returns a function that checks the contents of a result set against an
	 * expected string.
	 */
	private static Function1<ResultSet, Void> expect(final String... expected) {
		return new Function1<ResultSet, Void>() {
			public Void apply(ResultSet resultSet) {
				try {
					final List<String> lines = new ArrayList<String>();
					HbaseAdapterTest.collect(lines, resultSet);
					Assert.assertEquals(Arrays.asList(expected), lines);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		};
	}

	private void checkSql(String sql, String model,
			Function1<ResultSet, Void> fn) throws SQLException {
		// try to connect
		Connection connection = null;
		Statement statement = null;
		try {
			Properties info = new Properties();
			info.put("model", CalciteUtil.jsonPath(model));
			connection = DriverManager.getConnection("jdbc:calcite:", info);
			statement = connection.createStatement();
			final ResultSet resultSet = statement.executeQuery(sql);
			System.out.println("\n" + sql);
			fn.apply(resultSet);
		} finally {
			close(connection, statement);
		}
	}

	

	private static void collect(List<String> result, ResultSet resultSet)
			throws SQLException {
		final StringBuilder buf = new StringBuilder();
		while (resultSet.next()) {
			buf.setLength(0);
			int n = resultSet.getMetaData().getColumnCount();
			String sep = "";
			for (int i = 1; i <= n; i++) {
				buf.append(sep)
						.append(resultSet.getMetaData().getColumnLabel(i))
						.append("=").append(resultSet.getString(i));
				sep = "; ";
			}
			result.add(toLinux(buf.toString()));
		}
	}

	private void output(ResultSet resultSet, PrintStream out)
			throws SQLException {
		final ResultSetMetaData metaData = resultSet.getMetaData();
		final int columnCount = metaData.getColumnCount();
		while (resultSet.next()) {
			for (int i = 1;; i++) {
				out.print(resultSet.getString(i));
				if (i < columnCount) {
					out.print(", ");
				} else {
					out.println();
					break;
				}
			}
		}
	}

	@Before
	public void setConfigPath() {
		
		
		String configPath = "D:\\olapworkspace\\calcite-web\\calcite_models";
		System.out.println("config path:" + configPath);
		System.out.println("config path:" + configPath);
		System.setProperty("calcite.model.path", configPath);
		System.setProperty("hadoop.home.dir", "D:/develope/hadoop-2.4.0");
	}

	/**
	 * get the sql excute result
	 */
	@Test
	public void testSelect() throws SQLException {

		long start = System.currentTimeMillis();
		
		String sql_blog_test = "select *  from BLOG";
		//System.out.println("\n" + sql_blog_test);
		checkSql("hbase_model", sql_blog_test);

		long end = System.currentTimeMillis();
		long usedTime = end - start;
		System.out.println("--|Search used " + usedTime + " ms");

		String sql_metadata_test = "select * from KYLIN_METADATA where ROW_KEY='/project/learn_kylin.json'";
	//	System.out.println("\n" + sql_metadata_test);
		checkSql("hbase_model", sql_metadata_test);
	}
}
