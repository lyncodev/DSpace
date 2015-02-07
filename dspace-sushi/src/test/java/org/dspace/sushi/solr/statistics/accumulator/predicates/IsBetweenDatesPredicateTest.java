package org.dspace.sushi.solr.statistics.accumulator.predicates;

import org.dspace.sushi.solr.service.model.SolrRow;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class IsBetweenDatesPredicateTest {
    private IsBetweenDatesPredicate underTest;

    @Test
    public void endDateShouldBeExclusive() throws Exception {
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);
        underTest = new IsBetweenDatesPredicate(start, end);
        SolrRow solrRow = mock(SolrRow.class);
        given(solrRow.getTime()).willReturn(end.toDate());

        boolean result = underTest.apply(solrRow);

        assertThat(result, is(false));
    }

    @Test
    public void startDateShouldBeInclusive() throws Exception {
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);
        underTest = new IsBetweenDatesPredicate(start, end);
        SolrRow solrRow = mock(SolrRow.class);
        given(solrRow.getTime()).willReturn(start.toDate());

        boolean result = underTest.apply(solrRow);

        assertThat(result, is(true));
    }
}