package com.moon.myreadapp.common.components.rss;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @description:
 * @author: Match
 * @date: 15-7-8
 */
public class DateUtil {
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    /**
     * @see <a href="http://www.ietf.org/rfc/rfc0822.txt">RFC 822</a>
     */
    private static final SimpleDateFormat RFC822 = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss Z", java.util.Locale.ENGLISH);

    /* Hide constructor */
    private DateUtil() {}

    public static CharSequence formatTime(Date date) {
        return TIME_FORMAT.format(date);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Parses string as an RFC 822 date/time.
     *
     */
    public static Date parseRfc822(String date) {
        try {
            return RFC822.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }
}
