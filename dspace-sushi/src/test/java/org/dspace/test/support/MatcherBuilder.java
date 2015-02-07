package org.dspace.test.support;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;

import java.util.ArrayList;
import java.util.Collection;

public class MatcherBuilder<M, T> extends BaseMatcher<T> {
    private Collection<Matcher<? super T>> list = new ArrayList<>();

    @Override
    public boolean matches(Object o) {
        return getMatcher().matches(o);
    }

    private Matcher<T> getMatcher() {
        return AllOf.allOf(list);
    }

    @Override
    public void describeTo(Description description) {
        description.appendDescriptionOf(getMatcher());
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        getMatcher().describeMismatch(item, description);
    }

    protected M self () {
        return (M) this;
    }

    public M with (Matcher<? super T> matcher) {
        this.list.add(matcher);
        return self();
    }
}
