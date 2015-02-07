package org.dspace.test.support;

import com.lyncode.sushicounter.events.model.ItemUsageData;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static com.lyncode.sushicounter.events.model.ItemUsageData.ItemEventGroup;

public class ItemEventGroupMatcherBuilder extends MatcherBuilder<ItemEventGroupMatcherBuilder, ItemEventGroup> {
    public static ItemEventGroupMatcherBuilder itemEventGroup () {
        return new ItemEventGroupMatcherBuilder();
    }

    public ItemEventGroupMatcherBuilder withCount(final Matcher<Long> integerMatcher) {
        return with(new FeatureMatcher<ItemEventGroup, Long>(integerMatcher, "count", "count") {
            @Override
            protected Long featureValueOf(ItemEventGroup itemEventGroup) {
                return itemEventGroup.getCount();
            }
        });
    }
}
