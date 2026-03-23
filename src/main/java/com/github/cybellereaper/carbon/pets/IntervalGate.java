package com.github.cybellereaper.carbon.pets;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class IntervalGate {
    private final Clock clock;
    private final Map<String, Instant> nextRunTimes = new ConcurrentHashMap<>();

    public IntervalGate(Clock clock) {
        this.clock = clock;
    }

    public boolean shouldRun(String key, Duration interval) {
        Instant now = clock.instant();
        Instant nextRun = nextRunTimes.get(key);

        if (nextRun != null && now.isBefore(nextRun)) {
            return false;
        }

        nextRunTimes.put(key, now.plus(interval));
        return true;
    }

    public void clear(String keyPrefix) {
        nextRunTimes.keySet().removeIf(key -> key.startsWith(keyPrefix));
    }
}
