package com.sandrozbinden.solr;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sandrozbinden.rss.Feed;
import com.sandrozbinden.rss.FeedParser;
import com.sandrozbinden.rss.exception.InvalidFeedInput;

public class SolrIndexer {

    private static final Logger logger = LoggerFactory.getLogger(SolrIndexer.class);

    public void indexFeedFiles(File indexInputDirectory, File indexProcessedDirectory, File indexErrorDirectory) throws IOException, SolrServerException {
        logger.info("Importing files from directory: " + indexInputDirectory.getCanonicalPath());
        FeedParser feedParser = new FeedParser();
        File[] textFiles = indexInputDirectory.listFiles();
        int fileCounter = 0;
        for (File textFile : textFiles) {
            logger.info("Processing file: " + textFile.getName() + " file " + fileCounter + " of " + textFiles.length);
            List<String> lines = Files.readLines(textFile, Charsets.UTF_8);
            int lineCounter = 0;
            for (String line : lines) {
                try {
                    index(feedParser.parse(line));
                } catch (InvalidFeedInput e) {
                    logger.warn("Can't add feed: " + lineCounter + " of file " + textFile.getName());
                    Files.append(line, new File(indexErrorDirectory, textFile.getName()), Charsets.UTF_8);
                }
                lineCounter = lineCounter + 1;
                if (lineCounter % 100 == 0) {
                    logger.info(" Processed " + lineCounter + " of " + lines.size() + " feeds");
                }
            }
            logger.info("Finished processing file: " + textFile.getName() + " imported " + lines.size() + " feeds");
            fileCounter = fileCounter + 1;
            Files.move(textFile, new File(indexProcessedDirectory, textFile.getName()));
        }
    }

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

}
