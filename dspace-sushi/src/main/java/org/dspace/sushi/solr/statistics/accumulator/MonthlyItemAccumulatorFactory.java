package org.dspace.sushi.solr.statistics.accumulator;

import org.dspace.sushi.solr.service.model.SolrRow;
import org.dspace.sushi.solr.statistics.accumulator.predicates.IsItemPredicate;
import org.dspace.sushi.util.Interval;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collection;

public class MonthlyItemAccumulatorFactory implements AccumulatorFactory {
    private final Interval interval;

    public MonthlyItemAccumulatorFactory(LocalDate start, LocalDate end) {
        this.interval = new Interval(start, end);
    }

    @Override
    public Collection<Accumulator> create(SolrRow item) {
        Collection<Accumulator> result = new ArrayList<>();
        for (Interval month : interval.splitByMonth()) {
            result.add(new Accumulator(month.start(), month.end(), new IsItemPredicate(item.getId())));
        }
        return result;
    }
}
