package com.github.cybellereaper.carbon.pets;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public record TalentExecutionContext(
        Plugin plugin,
        Player owner,
        LivingEntity pet,
        PetDefinition definition,
        PetStatProfile stats
) {
}
