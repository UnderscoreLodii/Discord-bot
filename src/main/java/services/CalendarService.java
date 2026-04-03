package services;

import calendar.events.BirthdayCalendarEvent;
import repositories.CalendarDataRepository;

import java.time.ZonedDateTime;


public class CalendarService {

    private final CalendarDataRepository calendarDataRepository;

    public CalendarService(CalendarDataRepository calendarDataRepository) {
        this.calendarDataRepository = calendarDataRepository;
    }

    public void setBirthday(Long guildId, ZonedDateTime eventDate, Long targetId, String message, boolean isLeap) {
        //add a fix for when a user has multiple birthdays
        calendarDataRepository.addCalendarEvent(guildId,
                new BirthdayCalendarEvent(guildId ,eventDate, targetId, "<@" + targetId + "> " + message, isLeap));
    }
}
