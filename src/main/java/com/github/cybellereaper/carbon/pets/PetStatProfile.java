package com.github.cybellereaper.carbon.pets;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public record PetStatProfile(Map<PetStat, Double> values) {

    public PetStatProfile {
        Objects.requireNonNull(values, "values");
        EnumMap<PetStat, Double> copy = new EnumMap<>(PetStat.class);

        for (PetStat stat : PetStat.values()) {
            double value = Objects.requireNonNull(values.get(stat), "Missing stat value for " + stat);
            if (value <= 0.0D) {
                throw new IllegalArgumentException("Stat multiplier must be positive for " + stat);
            }
            copy.put(stat, value);
        }

        values = Map.copyOf(copy);
    }

    public double value(PetStat stat) {
        return values.get(stat);
    }
}
