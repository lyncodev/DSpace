package org.dspace.sushi.events;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.lyncode.sushicounter.events.model.ItemUsageData;
import org.apache.commons.lang3.builder.Builder;
import org.niso.schemas.counter.Identifier;
import org.niso.schemas.counter.IdentifierType;

import java.util.ArrayList;
import java.util.Collection;

public class DSpaceItemUsageDataBuilder implements Builder<ItemUsageData> {
    private final Collection<Identifier> identifiers = new ArrayList<>();
    private String name;
    private final Collection<ItemUsageData.ItemStats> itemStats = new ArrayList<>();
    private Optional<String> publisher = Optional.absent();

    public DSpaceItemUsageDataBuilder () {}

    public DSpaceItemUsageDataBuilder withIdentifiers (Collection<Identifier> identifiers) {
        this.identifiers.addAll(identifiers);
        return this;
    }
    public DSpaceItemUsageDataBuilder withName (String name) {
        this.name = name;
        return this;
    }
    public DSpaceItemUsageDataBuilder withItemStats (ItemUsageData.ItemStats stats) {
        this.itemStats.add(stats);
        return this;
    }

    public DSpaceItemUsageDataBuilder withPublisher (String publisher) {
        this.publisher = Optional.fromNullable(publisher);
        return this;
    }

    @Override
    public ItemUsageData build() {
        return new DSpaceItemUsage(ImmutableList.copyOf(identifiers), name, ImmutableList.copyOf(itemStats), publisher);
    }

    private static class DSpaceItemUsage implements ItemUsageData {
        private final Collection<Identifier> identifiers;
        private final String name;
        private final Collection<ItemStats> itemStats;
        private final Optional<String> publisher;

        private DSpaceItemUsage(Collection<Identifier> identifiers, String name, Collection<ItemStats> itemStats, Optional<String> publisher) {
            this.identifiers = identifiers;
            this.name = name;
            this.itemStats = itemStats;
            this.publisher = publisher;
        }

        @Override
        public Collection<Identifier> getItemIdentifiers() {
            return identifiers;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Collection<ItemStats> getItemStats() {
            return itemStats;
        }

        @Override
        public Optional<String> getPublisher() {
            return publisher;
        }
    }
}
