package org.dspace.sushi.solr.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.dspace.sushi.solr.service.model.SolrRow;

public class DSpaceSolrQueryService implements SolrQueryService {
    private final SolrServer solrServer;

    public DSpaceSolrQueryService(SolrServer solrServer) {
        this.solrServer = solrServer;
    }

    @Override
    public Iterable<SolrRow> query(SolrQuery query) {
        return new SolrRowIterable(solrServer, query);
    }
}
