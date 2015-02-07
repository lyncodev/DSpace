package org.dspace.sushi.solr.statistics.accumulator;

import org.dspace.sushi.solr.service.model.SolrRow;
import org.junit.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class CompoundAccumulatorFactoryTest {
    private final AccumulatorFactory accumulatorFactory1 = mock(AccumulatorFactory.class);
    private final AccumulatorFactory accumulatorFactory2 = mock(AccumulatorFactory.class);
    private CompoundAccumulatorFactory underTest = new CompoundAccumulatorFactory(asList(accumulatorFactory1, accumulatorFactory2));

    @Test
    public void createShouldDelegateToChildAccumulatorFactories() throws Exception {
        SolrRow input = mock(SolrRow.class);
        Accumulator accumulator1 = mock(Accumulator.class);
        Accumulator accumulator2 = mock(Accumulator.class);
        given(accumulatorFactory1.create(input)).willReturn(asList(accumulator1));
        given(accumulatorFactory2.create(input)).willReturn(asList(accumulator2));

        Collection<Accumulator> result = underTest.create(input);

        assertThat(result.size(), is(2));
        assertThat(result, hasItem(accumulator1));
        assertThat(result, hasItem(accumulator2));
    }
}