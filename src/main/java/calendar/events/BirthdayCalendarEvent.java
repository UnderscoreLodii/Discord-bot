package calendar.calendarevents;

import java.time.ZonedDateTime;

public class BirthdayCalendarEvent extends CalendarEvent {

    private Long targetId;
    private String message;

    public BirthdayCalendarEvent() {
        super();
    }

    public BirthdayCalendarEvent(ZonedDateTime eventDate,  Long targetId,  String message) {
        super(EventType.BIRTHDAY, eventDate);
        this.targetId = targetId;
        this.message = message;
    }

    public Long getTargetId() {return targetId;}
    public String getMessage() {return message;}

    public void setTargetId(Long targetId) {this.targetId = targetId;}
    public void setMessage(String message) {this.message = message;}
}
