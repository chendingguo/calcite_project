package com.airsupply.adapter.solr;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

/**
 * solr client factory
 * 
 * @author arisupply
 *
 */
public class SolrClientFactory {
	static Map<String, SolrClient> sorlClientPool = new ConcurrentHashMap<String, SolrClient>();
	public static SolrClient solrClient = null;

	/**
	 * get solr client
	 * 
	 * @param connUrl
	 *            "http://" + ip + ":" + port + "/solr/" + coreName;
	 * @return solrClient
	 */
	public static SolrClient getSolrClient(String connUrl) {
		solrClient = sorlClientPool.get(connUrl);
		if (solrClient == null) {

			solrClient = new HttpSolrClient(connUrl);
			sorlClientPool.put(connUrl, solrClient);

		}
		return solrClient;
	}

}
