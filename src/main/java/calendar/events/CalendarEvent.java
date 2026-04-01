package calendar.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.ZonedDateTime;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include =  JsonTypeInfo.As.PROPERTY,
        property = "eventType"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = BirthdayCalendarEvent.class, name = "birthday")
})

public abstract class CalendarEvent implements Comparable<CalendarEvent> {
    public enum EventType {
        BIRTHDAY
    }

    private EventType eventType;
    private ZonedDateTime eventDate;

    public CalendarEvent() {}

    public CalendarEvent(EventType eventType,  ZonedDateTime eventDate) {
        this.eventType = eventType;
        this.eventDate = eventDate;
    }

    @Override
    public int compareTo(CalendarEvent o) {
        return this.eventDate.compareTo(o.eventDate);
    }

    public EventType getEventType() {return eventType;}
    public ZonedDateTime getEventDate() {return eventDate;}

    public void setEventType(EventType eventType) {this.eventType = eventType;}
    public void setEventDate(ZonedDateTime eventDate) {this.eventDate = eventDate;}
}
