package org.dspace.sushi.solr.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.dspace.sushi.exceptions.InternalSushiServiceException;
import org.dspace.sushi.solr.service.model.SolrRow;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.Collection;
import java.util.Iterator;

public class SolrRowIterable implements Iterable<SolrRow> {
    private final static int PAGE_SIZE = 1000;

    private final SolrServer solrServer;
    private final SolrQuery query;

    public SolrRowIterable(SolrServer solrServer, SolrQuery query) {
        this.solrServer = solrServer;
        this.query = query;
    }

    @Override
    public Iterator<SolrRow> iterator() {
        return new SolrRowIterator(solrServer, query);
    }

    private static class SolrRowIterator implements Iterator<SolrRow> {
        private final SolrServer solrServer;
        private final SolrQuery query;
        private QueryResponse currentResponse;
        private int offset = 0;
        private int localOffset = 0;
        private long total = 0;

        public SolrRowIterator(SolrServer solrServer, SolrQuery query) {
            this.solrServer = solrServer;
            this.query = query;
            query.setRows(PAGE_SIZE);
            this.currentResponse = queryServer();
        }

        private QueryResponse queryServer() {
            try {
                query.setStart(offset);
                QueryResponse response = solrServer.query(query);
                offset += PAGE_SIZE;
                localOffset = 0;
                return response;
            } catch (SolrServerException e) {
                throw new InternalSushiServiceException(e);
            }
        }

        @Override
        public boolean hasNext() {
            return total < currentResponse.getResults().getNumFound();
        }

        @Override
        public SolrRow next() {
            if (hasNext()) {
                if (localOffset < currentResponse.getResults().size()) {
                    total++;
                    return new SolrRow(currentResponse.getResults().get(localOffset++));
                } else {
                    currentResponse = queryServer();
                    return next();
                }
            } else throw new InvalidStateException("No next row available");
        }
    }
}
