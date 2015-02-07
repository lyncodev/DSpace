package org.dspace.sushi.consumer;

public interface Consumer<T> {
    void consume (T input);
}
