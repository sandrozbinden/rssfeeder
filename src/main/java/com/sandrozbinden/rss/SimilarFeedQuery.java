/* ============================================================================
 * Copyright (c) 2015 Imagic Bildverarbeitung AG, CH-8152 Glattbrugg.
 * All rights reserved.
 *
 * http://www.imagic.ch/
 * ============================================================================
 */
package com.sandrozbinden.rss;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.MoreLikeThisParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.io.Files;
import com.sandrozbinden.solr.SolrClientLocator;

/**
 *
 * @version $Revision$
 * @version $Date$
 * @author $Author$
 * @owner Sandro Mario Zbinden
 */
public class SimilarFeedQuery {

    private static final Logger logger = LoggerFactory.getLogger(SimilarFeedQuery.class);
    public static String newline = System.getProperty("line.separator");

    public void writeSimilarFeed(String documentId) throws SolrServerException, IOException {
        SolrClient solrClient = SolrClientLocator.getSolrClient();
        Configuration config = ConfigurationLoader.getConfig();
        SolrQuery query = getSolrQuery(documentId, config);
        QueryResponse response = solrClient.query(query);
        SolrDocumentList result = (SolrDocumentList) response.getResponse().get("response");
        List<String> interestingTerms = (List<String>) response.getResponse().get("interestingTerms");
        String info = "Found " + result.getNumFound() + " similar documents for document with id:" + documentId + " with the intersting terms: "
                + Joiner.on(",").join(interestingTerms);
        logger.info(info);
        File file = new File(config.getQueryResultDirectory(), getFilename(documentId));
        Files.append(info, file, Charsets.UTF_8);
        Files.append(newline + "Executeded query: " + config.getSolrURL() + "/mlt?" + decodeSolrQuery(query), file, Charsets.UTF_8);
        boolean first = true;
        String separator = config.getQueryResultFieldSeperator();
        for (SolrDocument solrDocument : result) {
            if (first) {
                Collection<String> fieldNames = solrDocument.getFieldNames();
                Files.append(newline + Joiner.on(separator).join(fieldNames), file, Charsets.UTF_8);
            }
            first = false;
            Files.append(newline, file, Charsets.UTF_8);
            String delimiter = "";
            for (String fieldName : solrDocument.getFieldNames()) {
                Files.append(delimiter + solrDocument.get(fieldName), file, Charsets.UTF_8);
                delimiter = separator;
            }
        }
    }

    private String getFilename(String documentId) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return df.format(new Date()) + "_" + documentId + ".txt";
    }

    private SolrQuery getSolrQuery(String documentId, Configuration config) throws UnsupportedEncodingException {
        SolrQuery query = new SolrQuery();
        query.set(CommonParams.QT, "/mlt");
        addBooleanParameter(config, query, MoreLikeThisParams.MATCH_INCLUDE);
        addBooleanParameter(config, query, MoreLikeThisParams.BOOST);
        addIntParameter(config, query, MoreLikeThisParams.MIN_DOC_FREQ);
        addIntParameter(config, query, MoreLikeThisParams.MAX_DOC_FREQ);
        addIntParameter(config, query, MoreLikeThisParams.MIN_TERM_FREQ);
        addIntParameter(config, query, MoreLikeThisParams.MIN_WORD_LEN);
        addIntParameter(config, query, MoreLikeThisParams.MAX_WORD_LEN);
        addParameter(config, query, MoreLikeThisParams.SIMILARITY_FIELDS);
        addParameter(config, query, CommonParams.FL);
        addParameter(config, query, CommonParams.ROWS);
        addParameter(config, query, CommonParams.START);
        query.setQuery("id:" + documentId);
        query.set(MoreLikeThisParams.INTERESTING_TERMS, "list");
        String decodedURL = decodeSolrQuery(query);
        logger.info("Execute solr query: " + decodedURL);
        return query;
    }

    private String decodeSolrQuery(SolrQuery query) throws UnsupportedEncodingException {
        return java.net.URLDecoder.decode(query.toString(), "UTF-8");
    }

    private void addBooleanParameter(Configuration config, SolrQuery query, String parameterName) {
        Optional<Boolean> parameter = config.getQueryBooleanParameter(parameterName);
        if (parameter.isPresent()) {
            query.set(parameterName, parameter.get());
        }
    }

    private void addParameter(Configuration config, SolrQuery query, String parameterName) {
        Optional<String> parameter = config.getQueryParameter(parameterName);
        if (parameter.isPresent()) {
            query.set(parameterName, parameter.get());
        }
    }

    private void addIntParameter(Configuration config, SolrQuery query, String parameterName) {
        Optional<Integer> parameter = config.getQueryIntParameter(parameterName);
        if (parameter.isPresent()) {
            query.set(parameterName, parameter.get());
        }
    }
}
