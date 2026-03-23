package com.github.cybellereaper.carbon.pets;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public record ResolvedPet(
        ActivePet activePet,
        Player owner,
        LivingEntity petEntity,
        PetDefinition definition
) {
}
