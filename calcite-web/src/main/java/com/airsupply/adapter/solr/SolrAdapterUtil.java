package com.airsupply.adapter.solr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

/**
 * utils
 * 
 * @author arisupply
 *
 */
public class SolrAdapterUtil {
	/**
	 * get connection url of solr
	 * 
	 * @param configPath
	 * @return
	 */
	public static String getConnectUrl(Map<String, Object> connInfoMap) {

		String ip = (String) connInfoMap.get("ip");
		String port = (String) connInfoMap.get("port");
		String coreName = (String) connInfoMap.get("coreName");
		String connUrl = "http://" + ip + ":" + port + "/solr/" + coreName;
		return connUrl;

	}

	/**
	 * read file
	 * 
	 * @param Path
	 * @return
	 */
	public static String ReadFile(String Path) {
		BufferedReader reader = null;
		String laststr = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(
					fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return laststr;
	}

}
