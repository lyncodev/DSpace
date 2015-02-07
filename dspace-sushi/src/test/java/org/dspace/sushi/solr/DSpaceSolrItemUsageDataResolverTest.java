package org.dspace.sushi.solr;

import com.google.common.base.Optional;
import com.lyncode.sushicounter.events.model.ItemUsageData;
import com.lyncode.sushicounter.events.repository.EventRepositoryQueryRequest;
import org.dspace.sushi.exceptions.SushiServiceException;
import org.junit.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

public class DSpaceSolrItemUsageDataResolverTest {
    private final SolrItemUsageDataReport usageDataReport = mock(SolrItemUsageDataReport.class);
    private DSpaceSolrItemUsageDataResolver underTest = new DSpaceSolrItemUsageDataResolver(asList(usageDataReport));

    @Test(expected = SushiServiceException.class)
    public void shouldThrowExceptionIfReportTypeNotPresent() throws Exception {
        underTest.resolve(new EventRepositoryQueryRequest.Builder()
                .build());
    }

    @Test(expected = SushiServiceException.class)
    public void shouldThrowExceptionIfReportVersionNotSet() throws Exception {
        underTest.resolve(new EventRepositoryQueryRequest.Builder()
                .reportType("BR1")
                .build());
    }

    @Test(expected = SushiServiceException.class)
    public void shouldThrowExceptionIfReportNotSupported() throws Exception {
        underTest.resolve(new EventRepositoryQueryRequest.Builder()
                .reportType("BR1")
                .reportVersion("4")
                .build());
    }

    @Test
    public void delegatesToTheFirstMatchingReport() throws Exception {
        given(usageDataReport.is(eq("BR1"), eq("4"))).willReturn(true);
        Collection<ItemUsageData> list = asList();
        given(usageDataReport.report(any(Optional.class), any(Optional.class))).willReturn(list);

        Collection<ItemUsageData> result = underTest.resolve(new EventRepositoryQueryRequest.Builder()
                .reportType("BR1")
                .reportVersion("4")
                .build());

        assertTrue(result == list);
    }
}