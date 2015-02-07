package org.dspace.sushi.solr.statistics.accumulator;

import org.dspace.sushi.solr.service.model.SolrRow;

import java.util.ArrayList;
import java.util.Collection;

public class CompoundAccumulatorFactory implements AccumulatorFactory {
    private final Collection<? extends AccumulatorFactory> accumulatorFactories;

    public CompoundAccumulatorFactory(Collection<? extends AccumulatorFactory> accumulatorFactories) {
        this.accumulatorFactories = accumulatorFactories;
    }

    @Override
    public Collection<Accumulator> create(SolrRow item) {
        ArrayList<Accumulator> result = new ArrayList<>();
        for (AccumulatorFactory accumulatorFactory : accumulatorFactories) {
            result.addAll(accumulatorFactory.create(item));
        }
        return result;
    }
}
