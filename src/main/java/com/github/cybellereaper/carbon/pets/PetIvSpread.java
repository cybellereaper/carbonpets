package com.github.cybellereaper.carbon.pets;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public record PetIvSpread(Map<PetStat, Integer> values) {

    private static final int MIN_IV = 0;
    private static final int MAX_IV = 31;

    public PetIvSpread {
        Objects.requireNonNull(values, "values");

        EnumMap<PetStat, Integer> copy = new EnumMap<>(PetStat.class);
        for (PetStat stat : PetStat.values()) {
            int value = Objects.requireNonNull(values.get(stat), "Missing IV for stat " + stat);
            if (value < MIN_IV || value > MAX_IV) {
                throw new IllegalArgumentException("IV must be between 0 and 31 for stat " + stat);
            }
            copy.put(stat, value);
        }
        values = Map.copyOf(copy);
    }

    public int value(PetStat stat) {
        return values.get(stat);
    }
}
