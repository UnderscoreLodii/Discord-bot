package calendar.services;

import repositories.CalendarDataRepository;

import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CalendarCheckerService {

    private final CalendarDataRepository calendarDataRepository;
    private final CalendarEventDispatcher calendarEventDispatcher;
    private final ScheduledExecutorService scheduledExecutorService;

    public CalendarCheckerService(CalendarDataRepository calendarDataRepository, CalendarEventDispatcher calendarEventDispatcher){
        this.calendarDataRepository = calendarDataRepository;
        this.calendarEventDispatcher = calendarEventDispatcher;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void start(){
        scheduledExecutorService.scheduleAtFixedRate(this::checkForEvents, 0,  60, TimeUnit.SECONDS);
    }

    private void checkForEvents(){
        ZonedDateTime now = ZonedDateTime.now();
        var guildMap = calendarDataRepository.getGuildMap();
        for(var entry : guildMap.entrySet()){
            while(entry.getValue().peek().getEventDate().isBefore(now)){
                calendarEventDispatcher.dispatch(calendarDataRepository.pollFromGivenGuild(entry.getKey()));
                if (entry.getValue().isEmpty()) break;
            }
        }
    }
}
