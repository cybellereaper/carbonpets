package com.github.cybellereaper.carbon.pets;

import java.util.EnumMap;
import java.util.Objects;

public final class PetStatCalculator {

    private static final double BASE_MULTIPLIER = 1.0D;
    private static final double IV_WEIGHT = 0.20D;
    private static final double EV_WEIGHT = 0.25D;
    private static final double MAX_IV_VALUE = 31.0D;
    private static final double MAX_EV_VALUE = 252.0D;

    public PetStatProfile calculate(PetGenetics genetics) {
        Objects.requireNonNull(genetics, "genetics");

        EnumMap<PetStat, Double> values = new EnumMap<>(PetStat.class);
        for (PetStat stat : PetStat.values()) {
            values.put(stat, BASE_MULTIPLIER + ivContribution(genetics, stat) + evContribution(genetics, stat));
        }

        return new PetStatProfile(values);
    }

    private double ivContribution(PetGenetics genetics, PetStat stat) {
        return (genetics.ivs().value(stat) / MAX_IV_VALUE) * IV_WEIGHT;
    }

    private double evContribution(PetGenetics genetics, PetStat stat) {
        return (genetics.evs().value(stat) / MAX_EV_VALUE) * EV_WEIGHT;
    }
}
