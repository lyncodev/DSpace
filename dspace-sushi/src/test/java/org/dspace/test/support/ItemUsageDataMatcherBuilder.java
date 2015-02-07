package org.dspace.test.support;

import com.lyncode.sushicounter.events.model.ItemUsageData;
import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Collection;

import static com.lyncode.sushicounter.events.model.ItemUsageData.ItemStats;

public class ItemUsageDataMatcherBuilder extends MatcherBuilder<ItemUsageDataMatcherBuilder, ItemUsageData> {
    public static ItemUsageDataMatcherBuilder itemUsageData () {
        return new ItemUsageDataMatcherBuilder();
    }

    public ItemUsageDataMatcherBuilder withName(final Matcher<String> stringMatcher) {
        return with(new FeatureMatcher<ItemUsageData, String>(stringMatcher, "name", "name") {
            @Override
            protected String featureValueOf(ItemUsageData itemUsageData) {
                return itemUsageData.getName();
            }
        });
    }

    public ItemUsageDataMatcherBuilder withoutPublisher() {
        return with(new TypeSafeMatcher<ItemUsageData>() {
            @Override
            protected boolean matchesSafely(ItemUsageData itemUsageData) {
                return !itemUsageData.getPublisher().isPresent();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("without publisher");
            }
        });
    }

    public ItemUsageDataMatcherBuilder withItemStats(final Matcher<Iterable<? super ItemStats>> itemStats) {
        return with(new FeatureMatcher<ItemUsageData, Iterable<ItemStats>>(itemStats, "item stats", "item stats") {
            @Override
            protected Iterable<ItemStats> featureValueOf(ItemUsageData itemUsageData) {
                return itemUsageData.getItemStats();
            }
        });
    }
}
