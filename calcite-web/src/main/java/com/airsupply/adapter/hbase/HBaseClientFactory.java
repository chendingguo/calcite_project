package com.airsupply.adapter.hbase;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * hbase client factory
 * 
 * @author arisupply
 *
 */
public class HBaseClientFactory {
	static Map<String, HTable> htablePool = new ConcurrentHashMap<String, HTable>();

	/**
	 * get htable object
	 * 
	 * @param connInfoMap
	 * @return
	 */
	public static HTable getHTable(Map<String, String> connInfoMap) {
		String tableName = connInfoMap.get(ConnInfo.tableName.toString());
		HTable htable = htablePool.get(tableName);
		String ip = connInfoMap.get(ConnInfo.ip.toString());
		String port = connInfoMap.get(ConnInfo.port.toString());
		if (htable == null) {

			Configuration conf = HBaseConfiguration.create();
			conf.set("hbase.zookeeper.quorum", ip);
			conf.set("hbase.zookeeper.property.clientPort", port);
			try {
				htable = new HTable(conf, Bytes.toBytes(tableName));
				htablePool.put(tableName, htable);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return htable;
	}

	public enum ConnInfo {
		ip, port, tableName
	}

}
