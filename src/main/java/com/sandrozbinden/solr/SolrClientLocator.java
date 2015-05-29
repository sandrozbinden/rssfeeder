package com.sandrozbinden.solr;

import org.apache.solr.client.solrj.SolrClient;

public class SolrClientLocator {

    private SolrClient solrClient;

    public static final SolrClientLocator INSTANCE = new SolrClientLocator();

    public static synchronized SolrClient getSolrClient() {
        return INSTANCE.solrClient;
    }

    public static synchronized void setSolrClient(SolrClient solrClient) {
        INSTANCE.solrClient = solrClient;
    }

}
