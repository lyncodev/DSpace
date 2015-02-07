package org.dspace.sushi.solr.statistics.accumulator;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.dspace.sushi.consumer.Consumer;
import org.dspace.sushi.solr.service.model.SolrRow;
import org.dspace.sushi.solr.statistics.accumulator.predicates.IsBetweenDatesPredicate;
import org.joda.time.LocalDate;

public class Accumulator implements Consumer<SolrRow> {
    private long count = 0;
    private final LocalDate start;
    private final LocalDate end;
    private final Predicate<SolrRow> condition;

    public Accumulator(LocalDate start, LocalDate end, Predicate<SolrRow> condition) {
        this.start = start;
        this.end = end;
        this.condition = Predicates.and(new IsBetweenDatesPredicate(start, end), condition);
    }

    @Override
    public void consume(SolrRow input) {
        if (condition.apply(input))
            count++;
    }

    public long total() {
        return count;
    }

    public LocalDate start() {
        return start;
    }

    public LocalDate end() {
        return end;
    }
}
