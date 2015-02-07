package org.dspace.sushi.solr.reports;

import com.lyncode.sushicounter.events.model.ItemUsageData;
import org.dspace.sushi.item.ItemResolver;
import org.dspace.sushi.item.ItemWrapper;
import org.dspace.sushi.solr.service.model.SolrRow;
import org.hamcrest.core.AllOf;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.Collection;

import static org.dspace.test.support.ItemEventGroupMatcherBuilder.itemEventGroup;
import static org.dspace.test.support.ItemStatsMatcherBuilder.itemStats;
import static org.dspace.test.support.ItemUsageDataMatcherBuilder.itemUsageData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class BR1Release4ItemUsageStatisticsReportTest {
    private final ItemResolver itemResolver = mock(ItemResolver.class);
    private final LocalDate start = new LocalDate(2012, 1, 1);
    private final LocalDate end = new LocalDate(2013, 1, 1);

    private BR1Release4ItemUsageStatisticsReport underTest = new BR1Release4ItemUsageStatisticsReport(itemResolver, start, end);

    @Test
    public void shouldCountGroupTheValuesByMonth() throws Exception {
        // given
        int itemId = 1;
        String itemName = "name";

        SolrRow solrRow = mock(SolrRow.class);
        given(solrRow.getTime()).willReturn(start.plusWeeks(1).toDate());
        given(solrRow.getId()).willReturn(itemId);

        ItemWrapper itemWrapper = mock(ItemWrapper.class);
        given(itemWrapper.getName()).willReturn(itemName);
        given(itemResolver.resolve(itemId)).willReturn(itemWrapper);
        underTest.consume(solrRow);

        // when
        Collection<ItemUsageData> itemUsageData = underTest.get();

        // then
        assertThat(itemUsageData.size(), is(1));
        ItemUsageData usageData = itemUsageData.iterator().next();
        assertThat(usageData, is(itemUsageData()
                        .withName(equalTo(itemName))
                        .withoutPublisher()
                        .withItemStats(AllOf.allOf(
                                hasItem(itemStats()
                                                .withStart(equalTo(start))
                                                .withEnd(equalTo(start.plusMonths(1)))
                                                .withGroups(hasItem(itemEventGroup().withCount(equalTo(1L))))
                                ),
                                hasItem(itemStats()
                                                .withStart(equalTo(start.plusMonths(1)))
                                                .withEnd(equalTo(start.plusMonths(2)))
                                                .withGroups(hasItem(itemEventGroup().withCount(equalTo(0L))))
                                )
                        ))
        ));
        assertThat(usageData.getItemStats().size(), is(13)); // total + 12 months
    }
}