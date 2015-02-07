package org.dspace.sushi.solr.statistics.accumulator.predicates;

import com.google.common.base.Predicate;
import org.dspace.sushi.solr.service.model.SolrRow;
import org.joda.time.LocalDate;

import javax.annotation.Nullable;
import java.util.Date;

public class IsBetweenDatesPredicate implements Predicate<SolrRow> {
    private final LocalDate start;
    private final LocalDate end;

    public IsBetweenDatesPredicate(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean apply(@Nullable SolrRow row) {
        Date time = row.getTime();
        LocalDate localDate = LocalDate.fromDateFields(time);
        return !localDate.isBefore(start) && localDate.isBefore(end);
    }
}
