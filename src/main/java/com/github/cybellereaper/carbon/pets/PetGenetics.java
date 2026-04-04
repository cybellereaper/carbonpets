package com.github.cybellereaper.carbon.pets;

import java.util.Objects;

public record PetGenetics(PetIvSpread ivs, PetEvSpread evs) {

    public PetGenetics {
        Objects.requireNonNull(ivs, "ivs");
        Objects.requireNonNull(evs, "evs");
    }

    public static PetGenetics withIvsAndEmptyEvs(PetIvSpread ivs) {
        return new PetGenetics(ivs, PetEvSpread.empty());
    }
}
