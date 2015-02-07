package org.dspace.sushi.util;

import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IntervalTest {
    Interval underTest;

    @Test
    public void oneMonthInterval() throws Exception {
        underTest = new Interval(new LocalDate(2000, 1, 1), new LocalDate(2000, 2, 1));

        List<Interval> intervals = underTest.splitByMonth();

        assertThat(intervals, hasSize(1));
    }

    @Test
    public void oneMonthIntervalStartingInTheMiddleOfTheMonth() throws Exception {
        underTest = new Interval(new LocalDate(2000, 1, 21), new LocalDate(2000, 2, 1));

        List<Interval> intervals = underTest.splitByMonth();

        assertThat(intervals, hasSize(1));
        assertThat(intervals.get(0).start().getDayOfMonth(), is(21));
    }

    @Test
    public void oneMonthIntervalEndingInTheMiddleOfTheMonth() throws Exception {
        underTest = new Interval(new LocalDate(2000, 1, 21), new LocalDate(2000, 1, 23));

        List<Interval> intervals = underTest.splitByMonth();

        assertThat(intervals, hasSize(1));
        assertThat(intervals.get(0).start().getDayOfMonth(), is(21));
        assertThat(intervals.get(0).end().getDayOfMonth(), is(23));
    }

    @Test
    public void twoMonthIntervalEndingInTheMiddleOfTheMonth() throws Exception {
        underTest = new Interval(new LocalDate(2000, 1, 21), new LocalDate(2000, 2, 23));

        List<Interval> intervals = underTest.splitByMonth();

        assertThat(intervals, hasSize(2));
        assertThat(intervals.get(0).start().getDayOfMonth(), is(21));
        assertThat(intervals.get(0).end().getMonthOfYear(), is(2));
        assertThat(intervals.get(0).end().getDayOfMonth(), is(1));

        assertThat(intervals.get(1).start().getDayOfMonth(), is(1));
        assertThat(intervals.get(1).start().getMonthOfYear(), is(2));
        assertThat(intervals.get(1).end().getMonthOfYear(), is(2));
        assertThat(intervals.get(1).end().getDayOfMonth(), is(23));
    }

    @Test
    public void oneYear() throws Exception {
        underTest = new Interval(new LocalDate(2010, 1, 1), new LocalDate(2011, 1, 1));

        List<Interval> intervals = underTest.splitByMonth();

        assertThat(intervals, hasSize(12));
    }
}