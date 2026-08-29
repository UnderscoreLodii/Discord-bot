package util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

/**
 * Manages per-guild, per-user action cooldowns backed by an in-memory expiring cache.
 *
 * <p>This class uses a flat composite key ({@link CooldownKey}) with write-based expiration
 * policies to ensure that inactive entries are automatically evicted from memory, preventing
 * memory leaks across high-churn events (such as voice channel updates or command invocations).
 *
 * <p>This implementation is thread-safe and optimized for concurrent read/write operations.
 *
 * @author YourName
 * @version 1.0
 */
public class CooldownHandler {

    /**
     * The internal expiring cache mapping compound guild/user keys to their active cooldown state.
     */
    private final Cache<CooldownKey, Boolean> timers;

    /**
     * The standard duration of a cooldown period in milliseconds.
     */
    private final Long standardCooldownTime;

    /**
     * Constructs a new {@code CooldownHandler} with a specified uniform cooldown duration.
     *
     * @param cooldownTime the duration of the cooldown in milliseconds; must be non-negative
     */
    public CooldownHandler(Long cooldownTime) {
        this.standardCooldownTime = cooldownTime;
        this.timers = Caffeine.newBuilder()
                .expireAfterWrite(standardCooldownTime, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * Checks whether a user is currently eligible to perform an action (i.e., not on cooldown).
     *
     * @param guildId the unique identifier of the Discord guild (server)
     * @param userId  the unique identifier of the Discord user
     * @return {@code true} if the cooldown has elapsed or was never set;
     *         {@code false} if the user is currently cooling down
     */
    public boolean checkCooldown(Long guildId, Long userId) {
        return timers.getIfPresent(new CooldownKey(guildId, userId)) == null;
    }

    /**
     * Triggers or resets the cooldown for a specific user within a guild.
     *
     * <p>Subsequent calls to {@link #checkCooldown(Long, Long)} for this user/guild pair
     * will return {@code false} until the configured {@link #standardCooldownTime} elapses.
     *
     * @param guildId the unique identifier of the Discord guild (server)
     * @param userId  the unique identifier of the Discord user
     */
    public void resetCooldown(Long guildId, Long userId) {
        timers.put(new CooldownKey(guildId, userId), Boolean.TRUE);
    }

    /**
     * Immutable composite key representing a unique user within a specific guild.
     *
     * @param guildId the unique identifier of the Discord guild
     * @param userId  the unique identifier of the Discord user
     */
    private record CooldownKey(Long guildId, Long userId) {}
}
