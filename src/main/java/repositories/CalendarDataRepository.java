package repositories;

import calendar.events.CalendarEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class CalendarDataRepository extends AbstractDataRepository<ConcurrentHashMap<Long, PriorityBlockingQueue<CalendarEvent>>> {

    public CalendarDataRepository() {
        super(new TypeReference<ConcurrentHashMap<Long, PriorityBlockingQueue<CalendarEvent>>>() {}, "calendar");
        objectMapper.registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        load();
        if (this.obj == null) this.obj = new ConcurrentHashMap<>();
    }

    public void addCalendarEvent(Long guildId, CalendarEvent calendarEvent ) {
        obj.computeIfAbsent(guildId, e -> new PriorityBlockingQueue<>()).add(calendarEvent);
        save();
    }

    public CalendarEvent pollFromGivenGuild(Long guildId) {
        var queue = obj.get(guildId);
        CalendarEvent result = queue.poll();
        if (queue.isEmpty()) {
            obj.remove(guildId);
        }
        save();
        return result;
    }

    public ConcurrentHashMap<Long, PriorityBlockingQueue<CalendarEvent>> getGuildMap(){
        return obj;
    }
}
