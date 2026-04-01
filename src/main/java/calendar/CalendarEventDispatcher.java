package calendar;

import calendar.eventhandlers.CalendarEventHandler;
import calendar.events.CalendarEvent;
import calendar.events.CalendarEvent.EventType;

import java.util.HashMap;
import java.util.Map;

public class CalendarEventDispatcher {

    private final Map<EventType, CalendarEventHandler> eventHandlers = new HashMap<>();

    public CalendarEventDispatcher addHandler(CalendarEventHandler calendarEventHandler){
        eventHandlers.put(calendarEventHandler.getHandlingType(), calendarEventHandler);
        return this;
    }

    public void dispatch(CalendarEvent event){
        eventHandlers.get(event.getEventType()).handle(event);
    }
}
