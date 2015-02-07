package org.dspace.sushi.solr.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.dspace.sushi.solr.service.model.SolrRow;

public interface SolrQueryService {
    Iterable<SolrRow> query (SolrQuery query);
}
