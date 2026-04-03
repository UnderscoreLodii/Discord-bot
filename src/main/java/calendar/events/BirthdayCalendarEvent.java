package calendar.events;

import java.time.ZonedDateTime;

public class BirthdayCalendarEvent extends CalendarEvent {

    private Long targetId;
    private String message;
    private Boolean isLeap;

    public BirthdayCalendarEvent() {
        super();
    }

    public BirthdayCalendarEvent(Long guildId, ZonedDateTime eventDate,  Long targetId,  String message, Boolean isLeap) {
        super(guildId, EventType.BIRTHDAY, eventDate);
        this.targetId = targetId;
        this.message = message;
        this.isLeap = isLeap;
    }

    public Long getTargetId() {return targetId;}
    public String getMessage() {return message;}
    public Boolean getIsLeap() {return isLeap;}

    public void setTargetId(Long targetId) {this.targetId = targetId;}
    public void setMessage(String message) {this.message = message;}
    public void setIsLeap(Boolean isLeap) {this.isLeap = isLeap;}
}
