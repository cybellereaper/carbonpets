package com.github.cybellereaper.carbon.pets;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CooldownService {
    private final Clock clock;
    private final Map<String, Instant> cooldowns = new ConcurrentHashMap<>();

    public CooldownService(Clock clock) {
        this.clock = clock;
    }

    public boolean isReady(String key) {
        Instant expiresAt = cooldowns.get(key);
        return expiresAt == null || !clock.instant().isBefore(expiresAt);
    }

    public boolean tryUse(String key, Duration cooldown) {
        if (!isReady(key)) {
            return false;
        }

        cooldowns.put(key, clock.instant().plus(cooldown));
        return true;
    }

    public void clear(String keyPrefix) {
        cooldowns.keySet().removeIf(key -> key.startsWith(keyPrefix));
    }
}
