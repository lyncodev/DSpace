package org.dspace.sushi.solr.reports;

import com.google.common.base.Supplier;
import com.lyncode.sushicounter.events.model.ItemUsageData;
import org.apache.solr.client.solrj.SolrQuery;
import org.dspace.sushi.consumer.Consumer;
import org.dspace.sushi.solr.service.model.SolrRow;

import java.util.Collection;

public interface UsageStatisticsReport extends Supplier<Collection<ItemUsageData>>, Consumer<SolrRow> {
    SolrQuery query ();
}
