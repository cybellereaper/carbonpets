package com.github.cybellereaper.carbon.pets;

import java.util.EnumMap;
import java.util.concurrent.ThreadLocalRandom;

public final class ThreadLocalPetGeneticsGenerator implements PetGeneticsGenerator {

    @Override
    public PetGenetics generate() {
        EnumMap<PetStat, Integer> ivValues = new EnumMap<>(PetStat.class);

        for (PetStat stat : PetStat.values()) {
            ivValues.put(stat, ThreadLocalRandom.current().nextInt(32));
        }

        return PetGenetics.withIvsAndEmptyEvs(new PetIvSpread(ivValues));
    }
}
