package org.dspace.sushi.solr;

import com.lyncode.sushicounter.events.model.ItemUsageData;
import com.lyncode.sushicounter.events.repository.EventRepositoryQueryRequest;
import org.dspace.sushi.exceptions.SushiServiceException;

import java.util.Collection;

public interface SolrItemUsageDataResolver {
    Collection<ItemUsageData> resolve (EventRepositoryQueryRequest request) throws SushiServiceException;
}
