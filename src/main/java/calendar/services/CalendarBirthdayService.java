package calendar.services;

import calendar.events.BirthdayCalendarEvent;
import calendar.events.CalendarEvent;
import calendar.utils.DateTimeParser;
import repositories.CalendarDataRepository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;


public class CalendarBirthdayService {

    private final CalendarDataRepository calendarDataRepository;

    public CalendarBirthdayService(CalendarDataRepository calendarDataRepository) {
        this.calendarDataRepository = calendarDataRepository;
    }

    public boolean setBirthday(Long guildId, Long targetId, int month, int day, String timeZone, String message) {
        if(getBirthdaysForGivenGuildStream(guildId).anyMatch(e -> e.getTargetId().equals(targetId))) return false;

        timeZone = (timeZone == null) ? "Europe/Warsaw" : timeZone;
        ZonedDateTime eventDate = DateTimeParser.parseDateTime(timeZone, "00:00", day, month);

        calendarDataRepository.addCalendarEvent(guildId,
                new BirthdayCalendarEvent(guildId ,eventDate, targetId, "<@" + targetId + "> " + message, (day == 29 && month == 2)));
        return true;
    }

    public boolean editUsersBirthday(Long guildId, Long targetId, int month, int day, String timeZone, String message) {
        return true;
    }

    public void deleteBirthdayFromMember(Long guildId, Long targetId){
        getOptionalBirthdayForMember(guildId, targetId)
                .ifPresent(birthdayCalendarEvent -> calendarDataRepository.deleteCalendarEvent(guildId, birthdayCalendarEvent));
    }

    private Optional<BirthdayCalendarEvent> getOptionalBirthdayForMember(Long guildId, Long targetId){
         return getBirthdaysForGivenGuildStream(guildId)
                .filter(e -> e.getTargetId().equals(targetId))
                .findFirst();
    }

    private Stream<BirthdayCalendarEvent> getBirthdaysForGivenGuildStream(Long guildId){
        return calendarDataRepository.getQueueForGivenGuild(guildId)
                .stream()
                .filter(e -> e.getEventType()== CalendarEvent.EventType.BIRTHDAY)
                .map(BirthdayCalendarEvent.class::cast);
    }
}
