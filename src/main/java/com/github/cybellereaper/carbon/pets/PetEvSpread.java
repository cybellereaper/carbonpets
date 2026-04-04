package com.github.cybellereaper.carbon.pets;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public record PetEvSpread(Map<PetStat, Integer> values) {

    public static final int MAX_PER_STAT = 252;
    public static final int MAX_TOTAL = 510;

    public PetEvSpread {
        Objects.requireNonNull(values, "values");

        EnumMap<PetStat, Integer> copy = new EnumMap<>(PetStat.class);
        int total = 0;

        for (PetStat stat : PetStat.values()) {
            int value = Objects.requireNonNull(values.get(stat), "Missing EV for stat " + stat);
            if (value < 0 || value > MAX_PER_STAT) {
                throw new IllegalArgumentException("EV must be between 0 and " + MAX_PER_STAT + " for stat " + stat);
            }
            total += value;
            copy.put(stat, value);
        }

        if (total > MAX_TOTAL) {
            throw new IllegalArgumentException("Total EV budget exceeded: " + total + " > " + MAX_TOTAL);
        }

        values = Map.copyOf(copy);
    }

    public static PetEvSpread empty() {
        EnumMap<PetStat, Integer> emptyMap = new EnumMap<>(PetStat.class);
        for (PetStat stat : PetStat.values()) {
            emptyMap.put(stat, 0);
        }
        return new PetEvSpread(emptyMap);
    }

    public int value(PetStat stat) {
        return values.get(stat);
    }
}
