package org.dspace.sushi.solr.service;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.dspace.sushi.solr.service.model.SolrRow;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SolrRowIterableTest {
    private final SolrServer solrServer = mock(SolrServer.class);
    private final SolrQuery solrQuery = mock(SolrQuery.class);
    private final SolrRowIterable underTest = new SolrRowIterable(solrServer, solrQuery);

    @Test
    public void solrDocumentsZeroIteratorShouldBeEmpty() throws Exception {
        QueryResponse queryResponse = mock(QueryResponse.class);
        SolrDocumentList solrDocuments = mock(SolrDocumentList.class);

        given(solrServer.query(solrQuery)).willReturn(queryResponse);
        given(queryResponse.getResults()).willReturn(solrDocuments);
        given(solrDocuments.getNumFound()).willReturn(0L);


        assertThat(underTest.iterator().hasNext(), is(false));
        assertThat(sizeOf(underTest), is(0));
    }

    @Test
    public void solrPaginationShouldWork() throws Exception {
        SolrDocument document = mock(SolrDocument.class);
        QueryResponse firstQueryResponse = mock(QueryResponse.class);
        QueryResponse secondQueryResponse = mock(QueryResponse.class);
        SolrDocumentList firstSolrDocuments = mock(SolrDocumentList.class);
        SolrDocumentList secondSolrDocuments = mock(SolrDocumentList.class);

        given(solrServer.query(solrQuery)).willReturn(firstQueryResponse, secondQueryResponse);
        given(firstQueryResponse.getResults()).willReturn(firstSolrDocuments);
        given(secondQueryResponse.getResults()).willReturn(secondSolrDocuments);

        given(firstSolrDocuments.getNumFound()).willReturn(1010L);
        given(firstSolrDocuments.size()).willReturn(1000);
        given(firstSolrDocuments.get(anyInt())).willReturn(document);
        given(secondSolrDocuments.getNumFound()).willReturn(1010L);
        given(secondSolrDocuments.size()).willReturn(10);
        given(secondSolrDocuments.get(anyInt())).willReturn(document);


        assertThat(sizeOf(underTest), is(1010));
    }

    private int sizeOf(SolrRowIterable underTest) {
        int count = 0;
        for (SolrRow solrRow : underTest) {
            count++;
        }
        return count;
    }
}