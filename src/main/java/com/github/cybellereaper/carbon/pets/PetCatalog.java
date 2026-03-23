package com.github.cybellereaper.carbon.pets;

import com.github.cybellereaper.carbon.pets.talents.GuardianBurstTalent;
import com.github.cybellereaper.carbon.pets.talents.OwnerHealPulseTalent;
import com.github.cybellereaper.carbon.pets.talents.SpeedSurgeProcTalent;
import org.bukkit.DyeColor;

import java.util.*;

public final class PetCatalog {

    private final Map<String, PetDefinition> definitions;

    public PetCatalog() {
        PetDefinition sunHound = new PetDefinition(
                "sun_hound",
                "Sun Hound",
                new WolfPetSpawner(DyeColor.ORANGE),
                new PetTalentSet(
                        List.of(new OwnerHealPulseTalent()),
                        List.of(new SpeedSurgeProcTalent()),
                        List.of(new GuardianBurstTalent())
                )
        );

        definitions = Map.of(sunHound.id(), sunHound);
    }

    public Optional<PetDefinition> find(String petId) {
        return Optional.ofNullable(definitions.get(petId.toLowerCase(Locale.ROOT)));
    }

    public Collection<String> allIds() {
        return definitions.keySet();
    }
}
