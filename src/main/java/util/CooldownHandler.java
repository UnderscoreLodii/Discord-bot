package util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class CooldownHandler {
    private final Cache<CooldownKey, Boolean> timers;
    private final Long standardCooldownTime;

    public CooldownHandler(Long cooldownTime) {
        this.standardCooldownTime = cooldownTime;
        this.timers = Caffeine.newBuilder()
                .expireAfterWrite(standardCooldownTime, java.util.concurrent.TimeUnit.MILLISECONDS)
                .build();
    }

    public boolean checkCooldown(Long guildId, Long userId) {
        return timers.getIfPresent(new CooldownKey(guildId, userId)) == null;
    }

    public void resetCooldown(Long guildId, Long userId) {
        timers.put(new CooldownKey(guildId, userId), Boolean.TRUE);
    }

    private record CooldownKey(Long guildId, Long userId) {}
}
