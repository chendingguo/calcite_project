package com.airsupply.solr.adapter.test;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

import com.airsupply.adapter.solr.SolrClientFactory;

public class SolrClientTest {
	String ip = "192.168.202.129";
	int port = 8080;
	String coreName = "core3";

	@Test
	public void testQuery() {
		String connUrl = "http://" + ip + ":" + port + "/solr/" + coreName;
		SolrClient client = SolrClientFactory.getSolrClient(connUrl);
		try {
			SolrQuery solrQuery=new SolrQuery("*:*");
			solrQuery.setStart(0);
			solrQuery.setRows(10000);
			QueryResponse resp = client.query(solrQuery);

			SolrDocumentList docList = resp.getResults();
			
			for (SolrDocument doc : docList) {
				System.out.println(doc.getFieldValueMap());
				

			}
			System.out.println(docList.size());

		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}

	}

}
