package com.github.cybellereaper.carbon.pets;

import java.util.EnumMap;

public final class PetStatCalculator {

    private static final double BASE_MULTIPLIER = 1.0D;
    private static final double IV_WEIGHT = 0.20D;
    private static final double EV_WEIGHT = 0.25D;

    public PetStatProfile calculate(PetGenetics genetics) {
        EnumMap<PetStat, Double> values = new EnumMap<>(PetStat.class);

        for (PetStat stat : PetStat.values()) {
            double ivContribution = (genetics.ivs().value(stat) / 31.0D) * IV_WEIGHT;
            double evContribution = (genetics.evs().value(stat) / 252.0D) * EV_WEIGHT;
            values.put(stat, BASE_MULTIPLIER + ivContribution + evContribution);
        }

        return new PetStatProfile(values);
    }
}
