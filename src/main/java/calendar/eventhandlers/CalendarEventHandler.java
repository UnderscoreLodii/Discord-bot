package calendar.eventhandlers;

import calendar.events.CalendarEvent;

public abstract class CalendarEventHandler {

    protected final CalendarEvent.EventType handlingType;

    public CalendarEventHandler(CalendarEvent.EventType handlingType){
        this.handlingType = handlingType;
    }

    public CalendarEvent.EventType getHandlingType(){
        return handlingType;
    }


    public abstract void handle(CalendarEvent calendarEvent);

}
