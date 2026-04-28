package calendar.eventhandlers;

import calendar.events.BirthdayCalendarEvent;
import calendar.events.CalendarEvent;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import repositories.CalendarDataRepository;

import java.time.Year;
import java.time.ZonedDateTime;

public class BirthdayCalendarEventHandler implements CalendarEventHandler {

    private final CalendarDataRepository calendarDataRepository;
    private final JDA jda;

    public BirthdayCalendarEventHandler(CalendarDataRepository calendarDataRepository, JDA jda){
        this.calendarDataRepository = calendarDataRepository;
        this.jda = jda;
    }

    @Override
    public void handle(CalendarEvent calendarEvent) {
        BirthdayCalendarEvent birthdayEvent;
        if (calendarEvent instanceof BirthdayCalendarEvent) birthdayEvent = (BirthdayCalendarEvent) calendarEvent;
        else throw new IllegalArgumentException("Tried to handle " + calendarEvent.getClass().getSimpleName() + " with BirthdayCalendarEventHandler");

        //temporary hard coded channel for sending wishes. Gonna change it when I add config feature
        String channelId = Dotenv.load().get("CHANNEL_ID");
//        testing
        channelId = Dotenv.load().get("TEST_CHANNEL_ID");
        TextChannel channel = jda.getTextChannelById(channelId);

        if(channel != null) {
            channel.sendMessage(birthdayEvent.getMessage()).queue();
            reAddBirthdayEventToCalendar(birthdayEvent);
        }
    }

    @Override
    public CalendarEvent.EventType getHandlingType() {
        return CalendarEvent.EventType.BIRTHDAY;
    }

    private void reAddBirthdayEventToCalendar(BirthdayCalendarEvent birthdayEvent){
        ZonedDateTime newDate;
        if(birthdayEvent.getIsLeap()){
            ZonedDateTime currentDate = birthdayEvent.getEventDate();
            int nextYear = currentDate.getYear() + 1;

            if (Year.isLeap(nextYear)){
                newDate = currentDate.withYear(nextYear).withDayOfMonth(29).withMonth(2);
            } else {
                newDate = currentDate.withYear(nextYear).withDayOfMonth(1).withMonth(3);
            }

        } else {
            newDate = birthdayEvent.getEventDate().plusYears(1);
        }

        birthdayEvent.setEventDate(newDate);
        calendarDataRepository.addCalendarEvent(birthdayEvent.getGuildId(), birthdayEvent);
    }
}
