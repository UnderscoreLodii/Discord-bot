package calendar.utils;

import exceptions.InvalidDateTimeFormatException;


import java.time.DateTimeException;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeParser {

    private static final Pattern timePattern = Pattern.compile("^([01]\\d|2[0-3]):([0-5]\\d)$");

    public static ZonedDateTime parseDateTime(String timezone, String timeString, Integer day, Integer month, Integer year) throws DateTimeException {

        Matcher matcher = timePattern.matcher(timeString);
        if(!matcher.matches()) throw new InvalidDateTimeFormatException("Time must be in HH:MM format");

        int hour = Integer.parseInt(matcher.group(1));
        int minute = Integer.parseInt(matcher.group(2));

        ZonedDateTime dateTime = ZonedDateTime.of(
                year, month, day, hour, minute, 0, 0, ZoneId.of(timezone)
        );

        return dateTime;
    }

    public static ZonedDateTime parseDateTime(String timezone, String timeString, Integer day, Integer month) throws DateTimeException {
        ZonedDateTime result;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(timezone));
        if(day == 29 && month == 2){
            int year = now.getYear();
            if(now.getMonthValue() > 2 || (now.getMonthValue() == 2 && now.getDayOfMonth() == 29)) year++;

            if(Year.isLeap(year)){
                result = parseDateTime(timezone, timeString, 29, 2, year);
            } else {
                result = parseDateTime(timezone, timeString, 1, 3, year);
            }
        }
        else {
            result = parseDateTime(timezone, timeString, day, month, now.getYear());
            if(!result.isAfter(now)) result = result.plusYears(1);
        }
        return result;
    }
}
