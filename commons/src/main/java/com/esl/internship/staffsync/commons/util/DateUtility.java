package com.esl.internship.staffsync.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class DateUtility {

    public static LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static long[] convertDateToHoursAndMinutes(long milliseconds) {
        long totalDurationMinutes = milliseconds / (60 * 1000);
        long hours = totalDurationMinutes / 60;
        long minutes = totalDurationMinutes % 60;
        return new long[]{hours, minutes};
    }

    public static Date formatDate(String dateInString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        return formatter.parse(dateInString);
    }
}
