package org.dspace.sushi.solr;

import com.google.common.base.Optional;
import com.lyncode.sushicounter.events.model.ItemUsageData;

import java.util.Collection;
import java.util.Date;

public interface SolrItemUsageDataReport {
    boolean is (String reportName, String version);
    Collection<ItemUsageData> report (Optional<Date> startDate, Optional<Date> endDate);
}
