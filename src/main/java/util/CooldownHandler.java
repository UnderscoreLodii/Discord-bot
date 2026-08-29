package util;

import java.util.HashMap;
import java.util.Map;

public class CooldownHandler {

    private final Map<Long, Map<Long, Timer>> timers = new HashMap<>();
    private final Long standardCooldownTime;

    public CooldownHandler(Long cooldownTime) {
        this.standardCooldownTime = cooldownTime;
    }

    public boolean checkCooldown(Long guildId, Long userId) {
        return timers.getOrDefault(guildId, new HashMap<>()).getOrDefault(userId, Timer.ALWAYS_READY).isReady();
    }

    public void resetCooldown(Long guildId, Long userId) {
        timers.computeIfAbsent(guildId, _ -> new HashMap<>())
                .compute(userId, (_, existingTimer) -> {
                    if (existingTimer != null && existingTimer != Timer.ALWAYS_READY) {
                        existingTimer.startTimer(standardCooldownTime);
                        return existingTimer;
                    }
                    return Timer.buildTheTimerAndStart(standardCooldownTime);
                });
    }

    private static class Timer {

        public static final Timer ALWAYS_READY = new Timer() {
            @Override
            public boolean isReady() {
                return true;
            }
            @Override
            public void startTimer(long timeInMillis) {
                throw new UnsupportedOperationException("Cannot restart the readyTimer instance.");
            }
        };

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
