package org.dspace.sushi.consumer;

import java.util.Collection;

public class CompoundConsumer<T> implements Consumer<T> {
    private final Collection<? extends Consumer<T>> consumers;

    public CompoundConsumer(Collection<? extends Consumer<T>> consumers) {
        this.consumers = consumers;
    }

    @Override
    public void consume(T input) {
        for (Consumer<T> consumer : consumers) {
            consumer.consume(input);
        }
    }
}
