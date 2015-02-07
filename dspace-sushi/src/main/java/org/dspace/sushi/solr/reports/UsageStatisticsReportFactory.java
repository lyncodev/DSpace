package org.dspace.sushi.solr.reports;

import org.joda.time.LocalDate;

public interface UsageStatisticsReportFactory {
    UsageStatisticsReport create (LocalDate start, LocalDate end);
}
