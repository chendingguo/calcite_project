package com.airsupply.hbase.adapter.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class Hbase {
	// 声明静态配置
	static Configuration conf = null;

	/*
	 * 创建表
	 * 
	 * @tableName 表名
	 * 
	 * @family 列族列表
	 */
	public static void creatTable(String tableName, String[] family)
			throws Exception {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "192.168.202.129");
		@SuppressWarnings("resource")
		HBaseAdmin admin = new HBaseAdmin(conf);
		HTableDescriptor desc = new HTableDescriptor(
				TableName.valueOf(tableName));
		for (int i = 0; i < family.length; i++) {
			desc.addFamily(new HColumnDescriptor(family[i]));
		}
		if (admin.tableExists(tableName)) {
			System.out.println("table Exists!");
			System.exit(0);
		} else {
			admin.createTable(desc);
			System.out.println("create table Success!");
		}
	}

	/*
	 * 为表添加数据（适合知道有多少列族的固定表）
	 * 
	 * @rowKey rowKey
	 * 
	 * @tableName 表名
	 * 
	 * @column1 第一个列族列表
	 * 
	 * @value1 第一个列的值的列表
	 * 
	 * @column2 第二个列族列表
	 * 
	 * @value2 第二个列的值的列表
	 */
	public static void addData(String rowKey, String tableName,
			String[] column1, String[] value1, String[] column2, String[] value2)
			throws IOException {
		Put put = new Put(Bytes.toBytes(rowKey));// 设置rowkey
		HTable table = new HTable(conf, Bytes.toBytes(tableName));// HTabel负责跟记录相关的操作如增删改查等//
																	// 获取表
		HColumnDescriptor[] columnFamilies = table.getTableDescriptor() // 获取所有的列族
				.getColumnFamilies();

		for (int i = 0; i < columnFamilies.length; i++) {
			String familyName = columnFamilies[i].getNameAsString(); // 获取列族名
			if (familyName.equals("article")) { // article列族put数据
				for (int j = 0; j < column1.length; j++) {
					put.add(Bytes.toBytes(familyName),
							Bytes.toBytes(column1[j]), Bytes.toBytes(value1[j]));
				}
			}
			if (familyName.equals("author")) { // author列族put数据
				for (int j = 0; j < column2.length; j++) {
					put.add(Bytes.toBytes(familyName),
							Bytes.toBytes(column2[j]), Bytes.toBytes(value2[j]));
				}
			}
		}
		table.put(put);
		System.out.println("add data Success!");
	}

	/*
	 * 根据rwokey查询
	 * 
	 * @rowKey rowKey
	 * 
	 * @tableName 表名
	 */
	public static Result getResult(String tableName, String rowKey)
			throws IOException {
		Get get = new Get(Bytes.toBytes(rowKey));
		HTable table = new HTable(conf, Bytes.toBytes(tableName));// 获取表
		Result result = table.get(get);
		for (KeyValue kv : result.list()) {
			System.out.println("family:" + Bytes.toString(kv.getFamily()));
			System.out
					.println("qualifier:" + Bytes.toString(kv.getQualifier()));
			System.out.println("value:" + Bytes.toString(kv.getValue()));
			System.out.println("Timestamp:" + kv.getTimestamp());
			System.out.println("-------------------------------------------");
		}
		return result;
	}

	/*
	 * 遍历查询hbase表
	 * 
	 * @tableName 表名
	 */
	public static void getResultScann(String tableName) throws IOException {
		Scan scan = new Scan();
		ResultScanner rs = null;
		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		try {
			rs = table.getScanner(scan);
			for (Result r : rs) {
				for (KeyValue kv : r.list()) {
					System.out.println("row:" + Bytes.toString(kv.getRow()));
					System.out.println("family:"
							+ Bytes.toString(kv.getFamily()));
					System.out.println("qualifier:"
							+ Bytes.toString(kv.getQualifier()));
					System.out
							.println("value:" + Bytes.toString(kv.getValue()));
					System.out.println("timestamp:" + kv.getTimestamp());
					System.out
							.println("-------------------------------------------");
				}
			}
		} finally {
			rs.close();
		}
	}

	/*
	 * 查询表中的某一列
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 */
	public static void getResultByColumn(String tableName, String rowKey,
			String familyName, String columnName) throws IOException {
		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		Get get = new Get(Bytes.toBytes(rowKey));
		get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName)); // 获取指定列族和列修饰符对应的列
		Result result = table.get(get);
		for (KeyValue kv : result.list()) {
			System.out.println("family:" + Bytes.toString(kv.getFamily()));
			System.out
					.println("qualifier:" + Bytes.toString(kv.getQualifier()));
			System.out.println("value:" + Bytes.toString(kv.getValue()));
			System.out.println("Timestamp:" + kv.getTimestamp());
			System.out.println("-------------------------------------------");
		}
	}

	/*
	 * 更新表中的某一列
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 * 
	 * @familyName 列族名
	 * 
	 * @columnName 列名
	 * 
	 * @value 更新后的值
	 */
	public static void updateTable(String tableName, String rowKey,
			String familyName, String columnName, String value)
			throws IOException {
		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		Put put = new Put(Bytes.toBytes(rowKey));
		put.add(Bytes.toBytes(familyName), Bytes.toBytes(columnName),
				Bytes.toBytes(value));
		table.put(put);
		System.out.println("update table Success!");
	}

	/*
	 * 查询某列数据的多个版本
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 * 
	 * @familyName 列族名
	 * 
	 * @columnName 列名
	 */
	public static void getResultByVersion(String tableName, String rowKey,
			String familyName, String columnName) throws IOException {
		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		Get get = new Get(Bytes.toBytes(rowKey));
		get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
		get.setMaxVersions(5);
		Result result = table.get(get);
		for (KeyValue kv : result.list()) {
			System.out.println("family:" + Bytes.toString(kv.getFamily()));
			System.out
					.println("qualifier:" + Bytes.toString(kv.getQualifier()));
			System.out.println("value:" + Bytes.toString(kv.getValue()));
			System.out.println("Timestamp:" + kv.getTimestamp());
			System.out.println("-------------------------------------------");
		}
		/*
		 * List<?> results = table.get(get).list(); Iterator<?> it =
		 * results.iterator(); while (it.hasNext()) {
		 * System.out.println(it.next().toString()); }
		 */
	}

	/*
	 * 删除指定的列
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 * 
	 * @familyName 列族名
	 * 
	 * @columnName 列名
	 */
	public static void deleteColumn(String tableName, String rowKey,
			String falilyName, String columnName) throws IOException {
		@SuppressWarnings("resource")
		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		Delete deleteColumn = new Delete(Bytes.toBytes(rowKey));
		deleteColumn.deleteColumns(Bytes.toBytes(falilyName),
				Bytes.toBytes(columnName));
		table.delete(deleteColumn);
		System.out.println(falilyName + ":" + columnName + "is deleted!");
	}

	/*
	 * 删除指定的列
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 */
	public static void deleteAllColumn(String tableName, String rowKey)
			throws IOException {
		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		Delete deleteAll = new Delete(Bytes.toBytes(rowKey));
		table.delete(deleteAll);
		System.out.println("all columns are deleted!");
	}

	/*
	 * 删除表
	 * 
	 * @tableName 表名
	 */
	public static void deleteTable(String tableName) throws IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		admin.disableTable(tableName);
		admin.deleteTable(tableName);
		System.out.println(tableName + "is deleted!");
	}

	/**
	 * init config
	 */

	public static void setConfig() {
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "192.168.202.129");
	}

	public static void queryTable(String tableName) {

		Scan scan = new Scan();

		ResultScanner rs = null;

		try {
			HConnection conn = HConnectionManager.createConnection(conf);
			HTableInterface htable = conn.getTable(tableName);

			rs = htable.getScanner(scan);

			for (Result r : rs) {
				List<Cell> ceList = r.listCells();
				Map<String, Object> map = new TreeMap<String, Object>();
				if (ceList != null && ceList.size() > 0) {
					for (Cell cell : ceList) {

						String rowKey = Bytes.toString(cell.getRowArray(),
								cell.getRowOffset(), cell.getRowLength());
						// if (rowKey.endsWith(".json")) {
						map.put(rowKey
								+ ":"
								+ Bytes.toString(cell.getFamilyArray(),
										cell.getFamilyOffset(),
										cell.getFamilyLength())
								+ "_"
								+ Bytes.toString(cell.getQualifierArray(),
										cell.getQualifierOffset(),
										cell.getQualifierLength()),
								Bytes.toString(cell.getValueArray(),
										cell.getValueOffset(),
										cell.getValueLength()));
						System.out.println("--------------------");
						System.out.println(rowKey);

						String familyName = Bytes.toString(
								cell.getFamilyArray(), cell.getFamilyOffset(),
								cell.getFamilyLength());
						System.out.println(familyName);

						String qualifier = Bytes.toString(
								cell.getQualifierArray(),
								cell.getQualifierOffset(),
								cell.getQualifierLength());
						System.out.println(qualifier);

						System.out.println(Bytes.toString(cell.getValueArray(),
								cell.getValueOffset(), cell.getValueLength()));

						// }

					}
					// System.out.println(map);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createTable(String tableName) {

		try {
			String[] family = { "article", "author" };
			creatTable(tableName, family);
			String[] column1 = { "title", "content", "tag" };
			String[] column2 = { "name", "nickname" };

			for (int i = 1; i < 3; i++) {
				String rowKey = "rowKey" + i;
				String[] valueOfArticle = { "title" + i, "content" + i,
						"tag" + i };
				String[] valueOfAuthor = { "name" + i, "nickname" + i };
				addData(rowKey, tableName, column1, valueOfArticle, column2,
						valueOfAuthor);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getTableDesc() {
		try {
			@SuppressWarnings("resource")
			HBaseAdmin hbaseAdmin = new HBaseAdmin(conf);

			HTableDescriptor[] descriptors = hbaseAdmin.listTables();
			for (HTableDescriptor tableDesc : descriptors) {

				System.out.println(tableDesc.getNameAsString());
				HColumnDescriptor[] columnDescs = tableDesc.getColumnFamilies();
				for (HColumnDescriptor columnDesc : columnDescs) {
					System.out.println("--" + columnDesc.getNameAsString());
					System.out.println("----|" + columnDesc.getConfiguration());

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {
		System.setProperty("hadoop.home.dir", "D:/develope/hadoop-2.4.0");
		setConfig();
		String tableName = "blog";
		// // createTable(tableName);
		queryTable(tableName);

		//getTableDesc();
		// // 遍历查询
		// getResultScann("blog2", "rowkey1", "rowkey2");
		// // 根据row key范围遍历查询
		// getResultScann("blog2", "rowkey4", "rowkey5");
		//
		// // 查询
		// getResult("blog2", "rowkey1");
		//
		// // 查询某一列的值
		// getResultByColumn("blog2", "rowkey1", "author", "name");
		//
		// // 更新列
		// updateTable("blog2", "rowkey1", "author", "name", "bin");
		//
		// // 查询某一列的值
		// getResultByColumn("blog2", "rowkey1", "author", "name");
		//
		// // 查询某列的多版本
		// getResultByVersion("blog2", "rowkey1", "author", "name");
		//
		// // 删除一列
		// deleteColumn("blog2", "rowkey1", "author", "nickname");
		//
		// // 删除所有列
		// deleteAllColumn("blog2", "rowkey1");
		//
		// // 删除表
		// deleteTable("blog2");

	}
}