package com.shakenbeer.wolttest.convert;

import com.shakenbeer.wolttest.model.Hour;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.shakenbeer.wolttest.model.Hour.CLOSE;
import static com.shakenbeer.wolttest.model.Hour.OPEN;

public class Hours2Strings {

    public static final String CLOSED_SUFFIX = ": Closed";
    public static final String NO_DATA_SUFFIX = ": No data provided";
    public static final String MALFORMED_DATA = "Malformed data";
    public static final String COLON = ": ";
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("h a");
    private static final DateTimeFormatter formatterWithMinutes = DateTimeFormat.forPattern("h.mm a");
    private static String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private Hours2Strings() {
    }

    public static Hours2Strings getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public List<String> convert(Map<String, Hour[]> week) {
        List<String> result = new ArrayList<>(days.length);
        for (int i = 0; i < days.length; i++) {
            result.add(convertDay(i, week));
        }
        return result;
    }

    private String convertDay(int i, Map<String, Hour[]> week) {
        String key = days[i].toLowerCase();
        Hour[] hours = week.get(key);
        if (hours == null) {
            return days[i] + NO_DATA_SUFFIX;
        } else if (hours.length == 0) {
            return days[i] + CLOSED_SUFFIX;
        }
        return days[i] + convertDay(hours);
    }

    private String convertDay(Hour[] hours) {
        if (hours.length % 2 == 1) {
            return COLON + MALFORMED_DATA;
        }
        StringBuilder sb = new StringBuilder(COLON);
        String currentType = CLOSE;
        for (int i = 0; i < hours.length; i++) {
            Hour hour = hours[i];
            if (hour.isType(currentType)) {
                return COLON + MALFORMED_DATA;
            }
            if (i > 0 && hour.isOpen()) {
                sb.append(", ");
            }
            if (hour.isClose()) {
                sb.append(" - ");
            }
            sb.append(format(hour.getValue()));
            currentType = next(currentType);
        }
        return sb.toString();
    }

    @Hour.Type
    private String next(String currentType) {
        return currentType.equals(CLOSE) ? OPEN : CLOSE;
    }

    private String format(long value) {
        LocalTime time = new LocalTime(value * 1000, DateTimeZone.UTC);
        if (time.getMinuteOfHour() == 0) {
            return formatter.print(time).toLowerCase();
        } else {
            return formatterWithMinutes.print(time).toLowerCase();
        }
    }

    private static class SingletonHelper {
        private static final Hours2Strings INSTANCE = new Hours2Strings();
    }
}
