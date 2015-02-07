package org.dspace.test.support;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.lyncode.sushicounter.events.model.ItemUsageData;
import org.dspace.content.Item;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.joda.time.LocalDate;

import javax.annotation.Nullable;
import java.util.Date;

import static com.lyncode.sushicounter.events.model.ItemUsageData.ItemEventGroup;
import static com.lyncode.sushicounter.events.model.ItemUsageData.ItemStats;
import static org.dspace.test.support.OptionalMatchers.containsValue;

public class ItemStatsMatcherBuilder extends MatcherBuilder<ItemStatsMatcherBuilder, ItemStats> {
    public static ItemStatsMatcherBuilder itemStats () {
        return new ItemStatsMatcherBuilder();
    }

    public ItemStatsMatcherBuilder withStart(final Matcher<LocalDate> dateMatcher) {
        return with(new FeatureMatcher<ItemStats, Optional<LocalDate>>(containsValue(dateMatcher), "start", "start") {
            @Override
            protected Optional<LocalDate> featureValueOf(ItemStats itemStats) {
                return itemStats.getStart().transform(new Function<Date, LocalDate>() {
                    @Nullable
                    @Override
                    public LocalDate apply(@Nullable Date date) {
                        return LocalDate.fromDateFields(date);
                    }
                });
            }
        });
    }

    public ItemStatsMatcherBuilder withEnd(final Matcher<LocalDate> dateMatcher) {
        return with(new FeatureMatcher<ItemStats, Optional<LocalDate>>(containsValue(dateMatcher), "start", "start") {
            @Override
            protected Optional<LocalDate> featureValueOf(ItemStats itemStats) {
                return itemStats.getEnd().transform(new Function<Date, LocalDate>() {
                    @Nullable
                    @Override
                    public LocalDate apply(@Nullable Date date) {
                        return LocalDate.fromDateFields(date);
                    }
                });
            }
        });
    }

    public ItemStatsMatcherBuilder withGroups(final Matcher<Iterable<? super ItemEventGroup>> matcher) {
        return with(new FeatureMatcher<ItemStats, Iterable<ItemEventGroup>>(matcher, "measure", "measure") {
            @Override
            protected Iterable<ItemEventGroup> featureValueOf(ItemStats itemStats) {
                return itemStats.getGroups();
            }
        });
    }
}
