package org.dspace.sushi.solr.statistics.accumulator;

import org.dspace.sushi.solr.service.model.SolrRow;

import java.util.Collection;

public interface AccumulatorFactory {
    Collection<Accumulator> create (SolrRow item);
}
