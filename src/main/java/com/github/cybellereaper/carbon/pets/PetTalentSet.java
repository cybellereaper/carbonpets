package com.github.cybellereaper.carbon.pets;

import java.util.List;

public record PetTalentSet(
        List<PassiveTalent> passiveTalents,
        List<RandomProcTalent> randomProcTalents,
        List<ManualTalent> manualTalents
) {
    public PetTalentSet {
        passiveTalents = List.copyOf(passiveTalents);
        randomProcTalents = List.copyOf(randomProcTalents);
        manualTalents = List.copyOf(manualTalents);
    }
}
