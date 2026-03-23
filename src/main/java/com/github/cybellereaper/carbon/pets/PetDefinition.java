package com.github.cybellereaper.carbon.pets;


public record PetDefinition(
        String id,
        String displayName,
        PetSpawner spawner,
        PetTalentSet talents
) {
}
