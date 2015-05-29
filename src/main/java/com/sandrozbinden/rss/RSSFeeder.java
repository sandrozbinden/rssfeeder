package com.sandrozbinden.rss;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sandrozbinden.solr.SolrIndexer;

public class RSSFeeder {

    private Configuration configuration;
    private static final Logger logger = LoggerFactory.getLogger(RSSFeeder.class);

    public RSSFeeder() {
        configuration = ConfigurationLoader.getConfig();
    }

    private void process(String[] args) throws SolrServerException, IOException, ParseException {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        options.addOption("i", "index", false,
                "Imports the feeds files present in the indexfiles directory. After a file has been indexed it will be moved to the processed_indexfiles directoryj");
        options.addOption(
                "q",
                "query",
                true,
                "Queries a document according the id of the feed and generates a text file in the query_result direcoty with a document called by it's id and a history of similar documents");
        formatter.printHelp("rssfeeder", options);

        CommandLineParser parser = new PosixParser();
        CommandLine commandLine = parser.parse(options, args);
        if (commandLine.hasOption("index")) {
            SolrIndexer solrIndexer = new SolrIndexer();
            solrIndexer.indexFeedFiles(configuration.getIndexInputDirectory(), configuration.getIndexProcessedDirectory(),
                    configuration.getIndexErrorDirectory());
        } else if (commandLine.hasOption("query")) {
            SimilarFeedQuery similarFeedQuery = new SimilarFeedQuery();
            similarFeedQuery.writeSimilarFeed(commandLine.getOptionValue("query"));
        }
    }

    public static void main(String[] args) throws IOException {
        logger.info("Start running RSSFeeder. Root Directory: " + new File(".").getCanonicalPath());
        RSSFeeder feeder = new RSSFeeder();
        try {
            feeder.process(args);
        } catch (Exception e) {
            logger.error("Can't run rss feeder", e);
        }
    }
}
