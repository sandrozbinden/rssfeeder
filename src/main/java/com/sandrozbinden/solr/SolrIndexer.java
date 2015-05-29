package com.sandrozbinden.solr;

import java.io.File;
import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import com.sandrozbinden.rss.Feed;

public class SolrIndexer {

    public void index(Feed feed) throws SolrServerException, IOException {
        SolrClient client = SolrClientLocator.getSolrClient();
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField("id", feed.getId());
        solrInputDocument.addField("title", feed.getTitle());
        solrInputDocument.addField("text", feed.getText());
        solrInputDocument.addField("language", feed.getLanguage());
        solrInputDocument.addField("newspaper", feed.getNewspaper());
        solrInputDocument.addField("link", feed.getLink());
        solrInputDocument.addField("publishingDate", feed.getPublishingDate());
        client.add(solrInputDocument);
        client.commit();
    }

    public void indexFeedFiles(File indexInputDirectory, File indexProcessedDirectory, File indexErrorDirectory) {
        //
    }
}
