package org.dspace.sushi.solr.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.dspace.core.ConfigurationManager;

public class SolrServerResolver {
    private static Logger log = LogManager.getLogger(SolrServerResolver.class);
    private static SolrServer server = null;

    public SolrServer getServer() throws SolrServerException
    {
        if (server == null)
        {
            try
            {
                server = new HttpSolrServer(ConfigurationManager.getProperty("sushi", "solr.url"));
                log.debug("Solr Server Initialized");
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
        }
        return server;
    }
}
