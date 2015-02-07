package org.dspace.sushi.solr.reports;

import com.google.common.base.Optional;
import com.lyncode.sushicounter.events.model.ItemUsageData;
import org.apache.solr.client.solrj.SolrQuery;
import org.dspace.sushi.solr.service.SolrQueryService;
import org.dspace.sushi.solr.service.model.SolrRow;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DSpaceUsageStatisticsReportTest {
    private static final String REPORT_NAME = "BR1";
    private static final String RELEASE_VERSION = "4";
    private final UsageStatisticsReportFactory reportFactory = mock(UsageStatisticsReportFactory.class);
    private final SolrQueryService solrQueryService = mock(SolrQueryService.class);
    private DSpaceUsageStatisticsReport underTest = new DSpaceUsageStatisticsReport(REPORT_NAME, RELEASE_VERSION, solrQueryService, reportFactory);

    @Test
    public void isShouldMatchReportNameAndReleaseVersion() throws Exception {
        boolean result = underTest.is(REPORT_NAME, RELEASE_VERSION);

        assertThat(result, is(true));
    }

    @Test
    public void isShouldNotMatchIfReportNameIsDifferent() throws Exception {
        boolean result = underTest.is("a", RELEASE_VERSION);

        assertThat(result, is(false));
    }

    @Test
    public void isShouldNotMatchIfReleaseVersionIsDifferent() throws Exception {
        boolean result = underTest.is(REPORT_NAME, "a");

        assertThat(result, is(false));
    }

    @Test
    public void reportShouldConsumeAllRowsReturnedByTheSolrQueryService() throws Exception {
        SolrRow solrRow = mock(SolrRow.class);
        UsageStatisticsReport statisticsReport = mock(UsageStatisticsReport.class);
        given(reportFactory.create((LocalDate) argThat(notNullValue()), (LocalDate) argThat(notNullValue())))
                .willReturn(statisticsReport);
        given(solrQueryService.query(any(SolrQuery.class))).willReturn(asList(solrRow));

        underTest.report(Optional.<Date>absent(), Optional.<Date>absent());

        verify(statisticsReport).consume(solrRow);
    }

    @Test
    public void generatedResultShouldBeTheSameAsReturnedByTheReport() throws Exception {
        ArrayList<ItemUsageData> itemUsageData = new ArrayList<>();
        UsageStatisticsReport statisticsReport = mock(UsageStatisticsReport.class);
        given(reportFactory.create((LocalDate) argThat(notNullValue()), (LocalDate) argThat(notNullValue()))).willReturn(statisticsReport);
        given(solrQueryService.query(any(SolrQuery.class))).willReturn(Arrays.<SolrRow>asList());
        given(statisticsReport.get()).willReturn(itemUsageData);

        Collection<ItemUsageData> result = underTest.report(Optional.<Date>absent(), Optional.<Date>absent());

        assertTrue(result == itemUsageData);
    }
}