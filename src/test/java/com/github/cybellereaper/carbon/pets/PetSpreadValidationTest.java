package com.github.cybellereaper.carbon.pets;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PetSpreadValidationTest {

    @Test
    void ivSpread_rejectsOutOfRangeValues() {
        EnumMap<PetStat, Integer> values = allStats(0);
        values.put(PetStat.POWER, 32);

        assertThrows(IllegalArgumentException.class, () -> new PetIvSpread(values));
    }

    @Test
    void evSpread_rejectsValuesAbovePerStatCap() {
        EnumMap<PetStat, Integer> values = allStats(0);
        values.put(PetStat.AGILITY, PetEvSpread.MAX_PER_STAT + 1);

        assertThrows(IllegalArgumentException.class, () -> new PetEvSpread(values));
    }

    @Test
    void evSpread_rejectsTotalBudgetOverflow() {
        EnumMap<PetStat, Integer> values = allStats(0);
        values.put(PetStat.VITALITY, 150);
        values.put(PetStat.POWER, 150);
        values.put(PetStat.GUARD, 120);
        values.put(PetStat.AGILITY, 100);

        assertThrows(IllegalArgumentException.class, () -> new PetEvSpread(values));
    }

    @Test
    void evSpread_acceptsValidPokemonStyleBudget() {
        EnumMap<PetStat, Integer> values = allStats(0);
        values.put(PetStat.POWER, 252);
        values.put(PetStat.AGILITY, 252);
        values.put(PetStat.SPIRIT, 6);

        assertDoesNotThrow(() -> new PetEvSpread(values));
    }

    private EnumMap<PetStat, Integer> allStats(int value) {
        EnumMap<PetStat, Integer> values = new EnumMap<>(PetStat.class);
        for (PetStat stat : PetStat.values()) {
            values.put(stat, value);
        }
        return values;
    }
}
