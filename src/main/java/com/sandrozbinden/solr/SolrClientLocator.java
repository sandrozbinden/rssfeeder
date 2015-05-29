package com.sandrozbinden.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import com.sandrozbinden.rss.ConfigurationLoader;

public class SolrClientLocator {

    private SolrClient solrClient;

    public static final SolrClientLocator INSTANCE = new SolrClientLocator();

    public static synchronized SolrClient getSolrClient() {
        if (INSTANCE.solrClient == null) {
            INSTANCE.solrClient = new HttpSolrClient(ConfigurationLoader.getConfig().getSolrURL());
        }
        return INSTANCE.solrClient;
    }

    public static synchronized void setSolrClient(SolrClient solrClient) {
        INSTANCE.solrClient = solrClient;
    }

}
