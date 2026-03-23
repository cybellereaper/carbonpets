package com.github.cybellereaper.carbon.pets;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface PetSpawner {
    LivingEntity spawn(Player owner, PetDefinition definition, PetKeys petKeys);
}
