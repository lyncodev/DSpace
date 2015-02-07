package org.dspace.sushi.solr.statistics.accumulator;

import com.google.common.base.Predicate;
import org.dspace.sushi.solr.service.model.SolrRow;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class AccumulatorTest {
    private final Predicate condition = mock(Predicate.class);
    private final LocalDate start = LocalDate.now();
    private final LocalDate end = LocalDate.now().plusMonths(1);
    private Accumulator underTest = new Accumulator(start, end, condition);

    @Test
    public void ifConditionReturnsFalseAccumulatorShouldNotIncrement() throws Exception {
        SolrRow input = mock(SolrRow.class);
        given(input.getTime()).willReturn(betweenStartAndEndDate());
        given(condition.apply(input)).willReturn(false);

        underTest.consume(input);

        assertThat(underTest.total(), is(0L));
    }

    @Test
    public void ifConditionReturnsTrueAccumulatorShouldNotIncrement() throws Exception {
        SolrRow input = mock(SolrRow.class);
        given(input.getTime()).willReturn(betweenStartAndEndDate());
        given(condition.apply(input)).willReturn(true);

        underTest.consume(input);

        assertThat(underTest.total(), is(1L));
    }

    @Test
    public void ifEventOutsideOfRangeDoNotCount() throws Exception {
        SolrRow input = mock(SolrRow.class);
        given(input.getTime()).willReturn(dateOutsideOfBounds());
        given(condition.apply(input)).willReturn(true);

        underTest.consume(input);

        assertThat(underTest.total(), is(0L));
    }

    private Date dateOutsideOfBounds() {
        return end.plusDays(1).toDate();
    }


    private Date betweenStartAndEndDate() {
        return start.plusDays(1).toDate();
    }
}