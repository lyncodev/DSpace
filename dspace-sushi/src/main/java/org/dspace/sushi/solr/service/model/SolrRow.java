package org.dspace.sushi.solr.service.model;

import org.apache.solr.common.SolrDocument;

import java.util.Date;

public class SolrRow {
    private final SolrDocument document;

    public SolrRow(SolrDocument document) {
        this.document = document;
    }

    public int getId() {
        return (Integer) document.get("id");
    }

    public Date getTime() {
        return (Date) document.get("time");
    }
}
