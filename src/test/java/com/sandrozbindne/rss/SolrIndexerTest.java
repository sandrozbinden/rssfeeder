package com.sandrozbindne.rss;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.sandrozbinden.rss.Feed;
import com.sandrozbinden.rss.FeedParser;
import com.sandrozbinden.rss.exception.InvalidFeedInput;
import com.sandrozbinden.solr.SolrClientLocator;
import com.sandrozbinden.solr.SolrIndexer;

public class SolrIndexerTest {

    @Before
    public void initServer() {
        SolrClientLocator.setSolrClient(new HttpSolrClient("http://localhost:8984/solr/rss"));
    }

    @Test
    @Ignore
    public void indexTest() throws SolrServerException, IOException {
        Feed feed = new Feed();
        feed.setId("3");
        feed.setText("mein garten ist gross");
        new SolrIndexer().index(feed);
    }

    @Test
    @Ignore
    public void indexFiles() throws IOException, SolrServerException {
        SolrIndexer solrIndexer = new SolrIndexer();
        File rootDirectory = new File("src/test/resources/data");
        System.out.println("Importing files from directory: " + rootDirectory.getCanonicalPath());
        FeedParser feedParser = new FeedParser();
        File[] textFiles = rootDirectory.listFiles();
        int fileCounter = 0;
        for (File textFile : textFiles) {
            System.out.println("Processing file: " + textFile.getName() + " file " + fileCounter + " of " + textFiles.length);
            List<String> lines = Files.readAllLines(textFile.toPath(), Charsets.UTF_8);
            int lineCounter = 0;
            for (String line : lines) {
                try {
                    solrIndexer.index(feedParser.parse(line));
                } catch (InvalidFeedInput e) {
                    System.out.println("Can't add feed: " + lineCounter + " of file " + textFile.getName());
                }
                lineCounter = lineCounter + 1;
                if (lineCounter % 100 == 0) {
                    System.out.println(" Processed " + lineCounter + " of " + lines.size() + " feeds");
                }
            }
            System.out.println("Finished processing file: " + textFile.getName() + " imported " + lines.size() + "feeds");
            fileCounter = fileCounter + 1;
        }
    }
}
