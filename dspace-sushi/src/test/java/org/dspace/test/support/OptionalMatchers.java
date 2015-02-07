package org.dspace.test.support;

import com.google.common.base.Optional;
import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class OptionalMatchers {
    public static <T> Matcher<Optional<T>> containsValue (final Matcher<? super T> subMatcher) {
        return new TypeSafeMatcher<Optional<T>>() {
            @Override
            protected boolean matchesSafely(Optional<T> tOptional) {
                return tOptional.isPresent() && subMatcher.matches(tOptional.get());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("contains value and ").appendDescriptionOf(subMatcher);
            }
        };
    }
}
