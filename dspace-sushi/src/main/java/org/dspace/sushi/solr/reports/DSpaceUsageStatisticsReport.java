package org.dspace.sushi.solr.reports;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.lyncode.sushicounter.events.model.ItemUsageData;
import org.dspace.sushi.solr.SolrItemUsageDataReport;
import org.dspace.sushi.solr.service.SolrQueryService;
import org.dspace.sushi.solr.service.model.SolrRow;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;

public class DSpaceUsageStatisticsReport implements SolrItemUsageDataReport {
    private final Logger log = LoggerFactory.getLogger(DSpaceUsageStatisticsReport.class);
    private final String reportName;
    private final String reportReleaseVersion;
    private final SolrQueryService solrQueryService;
    private final UsageStatisticsReportFactory reportFactory;

    public DSpaceUsageStatisticsReport(String reportName, String reportReleaseVersion, SolrQueryService solrQueryService, UsageStatisticsReportFactory reportFactory) {
        Preconditions.checkNotNull(reportName, "reportName argument cannot be null");
        Preconditions.checkNotNull(reportReleaseVersion, "reportReleaseVersion argument cannot be null");
        this.reportName = reportName.trim();
        this.reportReleaseVersion = reportReleaseVersion.trim();
        this.reportFactory = reportFactory;
        this.solrQueryService = solrQueryService;
    }

    @Override
    public boolean is(String reportName, String version) {
        return this.reportName.equals(reportName.trim()) &&
                this.reportReleaseVersion.equals(version.trim());
    }

    @Override
    public Collection<ItemUsageData> report(Optional<Date> startDate, Optional<Date> endDate) {
        UsageStatisticsReport report = reportFactory.create(
                LocalDate.fromDateFields(startDate.or(LocalDate.now().minusYears(1).toDate())),
                LocalDate.now());

        Iterable<SolrRow> iterable = solrQueryService.query(report.query());
        for (SolrRow solrRow : iterable) {
            report.consume(solrRow);
        }

        return report.get();
    }
}
