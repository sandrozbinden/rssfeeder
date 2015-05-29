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

import com.sandrozbinden.solr.SolrIndexer;

public class RSSFeeder {

    private Configuration configuration;

    public RSSFeeder() {
        configuration = ConfigurationLoader.getConfig();
    }

    private void process(String[] args) throws SolrServerException, IOException, ParseException {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        options.addOption("i", "index", false, "Imports the feeds files present in the indexfiles directory. After a file has been indexed it will be moved to the processed_indexfiles directoryj");
        options.addOption("q", "query", true, "Queries a document according the id of the feed and generates a text file in the query_result direcoty with a document called by it's id and a history of similar documents");
        formatter.printHelp("rssfeeder", options);

        CommandLineParser parser = new PosixParser();
        CommandLine commandLine = parser.parse(options, args);
        if (commandLine.hasOption("index")) {
            SolrIndexer solrIndexer = new SolrIndexer();
            solrIndexer.indexFeedFiles(configuration.getIndexInputDirectory(), configuration.getIndexProcessedDirectory(), configuration.getIndexErrorDirectory());
        }
    }

    public static void main(String[] args) throws IOException {
        System.err.println("Start running RSSFeeder. Root Directory: " + new File(".").getCanonicalPath());
        RSSFeeder feeder = new RSSFeeder();
        try {
            feeder.process(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't run rss feeder program due to: " + e.getMessage());
        }
    }

}
