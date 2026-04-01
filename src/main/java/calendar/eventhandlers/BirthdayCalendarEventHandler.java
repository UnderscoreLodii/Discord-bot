package calendar.eventhandlers;

import calendar.events.CalendarEvent;
import repositories.CalendarDataRepository;

public class BirthdayCalendarEventHandler implements CalendarEventHandler {

    private final CalendarDataRepository calendarDataRepository;

    public BirthdayCalendarEventHandler(CalendarDataRepository calendarDataRepository){
        this.calendarDataRepository = calendarDataRepository;
    }

    @Override
    public void handle(CalendarEvent calendarEvent) {
        
    }

    @Override
    public CalendarEvent.EventType getHandlingType() {
        return CalendarEvent.EventType.BIRTHDAY;
    }
}
