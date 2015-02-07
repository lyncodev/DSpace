package org.dspace.sushi.solr.statistics.accumulator;

import org.dspace.sushi.solr.service.model.SolrRow;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.mock;

public class MonthlyItemAccumulatorFactoryTest {
    private MonthlyItemAccumulatorFactory underTest;

    @Test
    public void oneMonthIntervalGeneratesOneAccumulator() throws Exception {
        SolrRow solrRow = mock(SolrRow.class);
        LocalDate start = new LocalDate(2000, 1, 1);
        LocalDate end = start.plusMonths(1);
        underTest = new MonthlyItemAccumulatorFactory(start, end);

        Collection<Accumulator> accumulators = underTest.create(solrRow);

        assertThat(accumulators, hasSize(1));
    }

    @Test
    public void oneMonthAndSomeDaysGeneratesTwoAccumulators() throws Exception {
        SolrRow solrRow = mock(SolrRow.class);
        LocalDate start = new LocalDate(2000, 1, 1);
        LocalDate end = start.plusMonths(1).plusDays(3);
        underTest = new MonthlyItemAccumulatorFactory(start, end);

        Collection<Accumulator> accumulators = underTest.create(solrRow);

        assertThat(accumulators, hasSize(2));
    }
}