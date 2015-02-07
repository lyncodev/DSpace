package org.dspace.sushi.solr.statistics.accumulator.predicates;

import com.google.common.base.Predicate;
import org.dspace.sushi.solr.service.model.SolrRow;

import javax.annotation.Nullable;

public class IsItemPredicate implements Predicate<SolrRow> {
    private final int id;

    public IsItemPredicate(int itemId) {
        this.id = itemId;
    }

    @Nullable
    @Override
    public boolean apply(@Nullable SolrRow input) {
        return input.getId() == id;
    }
}
