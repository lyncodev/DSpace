package org.dspace.sushi.solr.reports;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.lyncode.sushicounter.events.model.ContentType;
import com.lyncode.sushicounter.events.model.EventType;
import com.lyncode.sushicounter.events.model.ItemUsageData;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.dspace.core.Constants;
import org.dspace.statistics.SolrLogger;
import org.dspace.sushi.consumer.CompoundConsumer;
import org.dspace.sushi.consumer.Consumer;
import org.dspace.sushi.events.DSpaceItemUsageDataBuilder;
import org.dspace.sushi.item.ItemResolver;
import org.dspace.sushi.item.ItemWrapper;
import org.dspace.sushi.solr.service.model.SolrRow;
import org.dspace.sushi.solr.statistics.accumulator.*;
import org.dspace.sushi.solr.statistics.accumulator.predicates.IsItemPredicate;
import org.dspace.sushi.solr.utils.DateUtils;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Number of Successful Title Requests by Month and Title
 */
public class BR1Release4ItemUsageStatisticsReport implements UsageStatisticsReport {
    private final ItemResolver itemResolver;
    private final LocalDate start;
    private final LocalDate end;
    private final Accumulator totalAccumulator;
    private final Collection<Accumulator> accumulators;
    private final Consumer<SolrRow> rootConsumer;
    private final AccumulatorFactory accumulatorFactory;
    private final Map<Integer, Collection<Accumulator>> itemAccumulators = new HashMap<>();

    public BR1Release4ItemUsageStatisticsReport(ItemResolver itemResolver, final LocalDate start, final LocalDate end) {
        this.itemResolver = itemResolver;
        this.start = start;
        this.end = end;
        this.totalAccumulator = new Accumulator(start, end, Predicates.<SolrRow>alwaysTrue());
        this.accumulators = new ArrayList<>(asList(totalAccumulator));
        this.rootConsumer = new CompoundConsumer<>(accumulators);
        this.accumulatorFactory = new ItemAccumulatorFactory(new CompoundAccumulatorFactory(
                asList(
                        new AccumulatorFactory() {
                            @Override
                            public Collection<Accumulator> create(SolrRow item) {
                                return asList(new Accumulator(start, end, new IsItemPredicate(item.getId())));
                            }
                        },
                        new MonthlyItemAccumulatorFactory(start, end)
                )
        ));
    }

    @Override
    public void consume(SolrRow input) {
        accumulators.addAll(getAccumulators(input));
        rootConsumer.consume(input);
    }

    private Collection<? extends Accumulator> getAccumulators(SolrRow input) {
        Collection<Accumulator> collection = accumulatorFactory.create(input);
        if (!itemAccumulators.containsKey(input.getId()))
            itemAccumulators.put(input.getId(), new ArrayList<Accumulator>());
        itemAccumulators.get(input.getId()).addAll(collection);
        return collection;
    }

    @Override
    public Collection<ItemUsageData> get() {
        Collection<ItemUsageData> result = new ArrayList<>();
        for (Map.Entry<Integer, Collection<Accumulator>> entry : itemAccumulators.entrySet()) {
            ItemWrapper item = itemResolver.resolve(entry.getKey());
            DSpaceItemUsageDataBuilder builder = new DSpaceItemUsageDataBuilder()
                    .withName(item.getName())
                    .withPublisher(item.getPublisher())
                    .withIdentifiers(item.identifiers());

            for (Accumulator accumulator : entry.getValue()) {
                builder.withItemStats(new ItemUsageData.ItemStats(EventType.REQUEST,
                        Optional.fromNullable(accumulator.start().toDate()),
                        Optional.fromNullable(accumulator.end().toDate()),
                        asList(new ItemUsageData.ItemEventGroup(ContentType.FT_HTML, accumulator.total()))));
            }

            result.add(builder.build());
        }
        return result;
    }

    @Override
    public SolrQuery query() {
        return new SolrQuery(StringUtils.join(asList(
                String.format("type:%d", Constants.ITEM),
                String.format("statistics_type:%s", SolrLogger.StatisticsType.VIEW.text()),
                String.format("time:[%s TO %s]", DateUtils.toQueryString(start), DateUtils.toQueryString(end))
        ), " AND "));
    }
}
