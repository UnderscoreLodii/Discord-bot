package util;

import java.util.HashMap;
import java.util.Map;

public class CooldownHandler {

    private final Map<Long, Map<Long, Timer>> timers = new HashMap<>();
    private Long cooldownTime;

    public CooldownHandler(Long cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    public boolean handleCooldown(Long guildId, Long userId) {
        var guildIntroTimers = timers.computeIfAbsent(guildId,  k -> new HashMap<>());
        Timer timer = guildIntroTimers.get(userId);
        if (timer == null){
            guildIntroTimers.put(userId, Timer.buildTheTimerAndStart(cooldownTime));
        }
        else if (timer.isReady()){
            timer.startTimer(cooldownTime);
        }
        else return false;
        return true;
    }

    private static class Timer {

        private long readyTime = 0;

        public static Timer buildTheTimerAndStart(long timeInMillis) {
            Timer timer = new Timer();
            timer.startTimer(timeInMillis);
            return timer;
        }

        public boolean isReady() {
            return readyTime < System.currentTimeMillis();
        }

        public void startTimer(long timeInMillis) {
            readyTime = System.currentTimeMillis() + timeInMillis;
        }
    }
}
