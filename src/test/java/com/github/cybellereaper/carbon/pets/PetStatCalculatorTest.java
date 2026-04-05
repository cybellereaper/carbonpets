package com.github.cybellereaper.carbon.pets;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PetStatCalculatorTest {

    private final PetStatCalculator calculator = new PetStatCalculator();

    @Test
    void calculate_returnsBaseMultiplierWhenIvsAndEvsAreZero() {
        PetGenetics genetics = PetGenetics.withIvsAndEmptyEvs(new PetIvSpread(allStats(0)));

        PetStatProfile profile = calculator.calculate(genetics);

        for (PetStat stat : PetStat.values()) {
            assertEquals(1.0D, profile.value(stat), 0.0001D);
        }
    }

    @Test
    void calculate_appliesIvAndEvContributions() {
        EnumMap<PetStat, Integer> ivs = allStats(31);
        EnumMap<PetStat, Integer> evs = allStats(0);
        evs.put(PetStat.POWER, 252);

        PetGenetics genetics = new PetGenetics(new PetIvSpread(ivs), new PetEvSpread(evs));

        PetStatProfile profile = calculator.calculate(genetics);

        assertEquals(1.45D, profile.value(PetStat.POWER), 0.0001D);
        assertEquals(1.20D, profile.value(PetStat.AGILITY), 0.0001D);
    }

    @Test
    void calculate_rejectsNullGenetics() {
        assertThrows(NullPointerException.class, () -> calculator.calculate(null));
    }

    private EnumMap<PetStat, Integer> allStats(int value) {
        EnumMap<PetStat, Integer> values = new EnumMap<>(PetStat.class);
        for (PetStat stat : PetStat.values()) {
            values.put(stat, value);
        }
        return values;
    }
}
