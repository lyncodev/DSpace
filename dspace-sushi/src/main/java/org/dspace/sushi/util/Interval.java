package org.dspace.sushi.util;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Interval {
    private final LocalDate start;
    private final LocalDate end;

    public Interval(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public List<Interval> splitByMonth() {
        Period period = new Period(this.start, this.end);
        int months = period.getMonths() + period.getYears() * 12;
        ArrayList<Interval> intervals = new ArrayList<>();
        LocalDate end = start.plusMonths(1).withDayOfMonth(1);
        if (this.end.isBefore(end)) {
            return asList(new Interval(this.start, this.end));
        } else {
            intervals.add(new Interval(this.start, end));
            for (int i = 1; i < months; i++) {
                intervals.add(new Interval(end, end.plusMonths(1)));
                end = end.plusMonths(1);
            }
            if (this.end.getDayOfMonth() > 1) {
                intervals.add(new Interval(end, this.end));
            }
            return intervals;
        }
    }

    public LocalDate start() {
        return start;
    }

    public LocalDate end () {
        return end;
    }
}
