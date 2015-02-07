package org.dspace.sushi.solr.utils;

import com.google.common.base.Optional;
import org.apache.solr.common.util.DateUtil;
import org.joda.time.LocalDate;

import java.util.Date;

public class DateUtils {
    public static String toQueryString(Optional<Date> dateOptional) {
        if (dateOptional.isPresent())
            return toQueryString(dateOptional.get());
        else
            return "*";
    }

    public static String toQueryString(Date date) {
        return DateUtil.getThreadLocalDateFormat().format(date);
    }

    public static String toQueryString(LocalDate date) {
        return DateUtil.getThreadLocalDateFormat().format(date);
    }
}
