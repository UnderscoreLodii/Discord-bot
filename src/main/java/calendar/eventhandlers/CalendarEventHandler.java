package calendar.eventhandlers;

import calendar.events.CalendarEvent;

public interface CalendarEventHandler {
    void handle(CalendarEvent calendarEvent);
    CalendarEvent.EventType getHandlingType();
}
