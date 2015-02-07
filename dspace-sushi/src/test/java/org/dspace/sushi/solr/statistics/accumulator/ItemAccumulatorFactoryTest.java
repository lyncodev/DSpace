package org.dspace.sushi.solr.statistics.accumulator;

import org.dspace.sushi.solr.service.model.SolrRow;
import org.junit.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ItemAccumulatorFactoryTest {
    private final AccumulatorFactory accumulatorFactory = mock(AccumulatorFactory.class);
    private ItemAccumulatorFactory underTest = new ItemAccumulatorFactory(accumulatorFactory);

    @Test
    public void innerAccumulatorShouldOnlyBeCalledOncePerItem() throws Exception {
        SolrRow item = mock(SolrRow.class);
        given(item.getId()).willReturn(1);

        underTest.create(item);
        underTest.create(item);

        verify(accumulatorFactory, times(1)).create(item);
    }

    @Test
    public void innerAccumulatorShouldReturnAnEmptyListAfterTheFirstCall() throws Exception {
        SolrRow item = mock(SolrRow.class);
        given(accumulatorFactory.create(item)).willReturn(asList(mock(Accumulator.class)));
        given(item.getId()).willReturn(1);

        underTest.create(item);
        Collection<Accumulator> result = underTest.create(item);

        assertThat(result, empty());
    }
}