package org.dspace.sushi.solr.statistics.accumulator;

import org.dspace.sushi.solr.service.model.SolrRow;

import java.util.ArrayList;
import java.util.Collection;

public class ItemAccumulatorFactory implements AccumulatorFactory {
    private final Collection<Integer> idsSeen = new ArrayList<>();
    private final AccumulatorFactory factory;

    public ItemAccumulatorFactory(AccumulatorFactory factory) {
        this.factory = factory;
    }

    @Override
    public Collection<Accumulator> create(SolrRow item) {
        Collection<Accumulator> result = new ArrayList<>();
        if (!idsSeen.contains(item.getId())) {
            result.addAll(factory.create(item));
            idsSeen.add(item.getId());
        }
        return result;
    }
}
