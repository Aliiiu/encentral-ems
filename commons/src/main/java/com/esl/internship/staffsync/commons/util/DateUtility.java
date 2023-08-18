package com.esl.internship.staffsync.commons.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
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

    public static Timestamp convertLocalTimeToTimestampOfToday(LocalTime localTime) {
        LocalDateTime localDateTimeWithDate = LocalDateTime.of(LocalDate.now(), localTime);
        return Timestamp.valueOf(localDateTimeWithDate);
    }

    public static Timestamp convertLocalTimeToTimestamp(LocalDate localDate, LocalTime localTime) {
        LocalDateTime localDateTimeWithDate = LocalDateTime.of(localDate, localTime);
        return Timestamp.valueOf(localDateTimeWithDate);
    }

    public static LocalTime convertTimestampToLocalTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime().toLocalTime();
    }


    public static Date getTheStartOfTheDay(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Set time to midnight (12:00 AM)
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date getTheEndOfTheDay(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Set time to midnight (11:59 AM)
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }

    public static double getDurationBetweenTwoTimePeriod(Instant start, Instant end) {
        if (start == null || end == null)
            return 0;
        Duration duration = Duration.between(start, end);

        double totalHours = 0;

        totalHours += duration.toHours();
        totalHours += duration.toMinutesPart();
        totalHours += duration.toSecondsPart();
        totalHours += duration.toNanosPart();

        return totalHours;
    }

    public static double getDurationBetweenTwoTimePeriod(Time start, Time end) {
        return getDurationBetweenTwoTimePeriod(start.toInstant(), end.toInstant());
    }

    public static boolean dateEquals(Date date, LocalDate localDate) {
        return convertToLocalDate(date).isEqual(localDate);
    }

}