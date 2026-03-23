package com.github.cybellereaper.carbon.pets;

import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

public final class WolfPetSpawner implements PetSpawner {
    private final DyeColor collarColor;

    public WolfPetSpawner(DyeColor collarColor) {
        this.collarColor = collarColor;
    }

    @Override
    public LivingEntity spawn(Player owner, PetDefinition definition, PetKeys petKeys) {
        Location spawnLocation = owner.getLocation();
        return owner.getWorld().spawn(spawnLocation, Wolf.class, spawnedWolf -> {
            spawnedWolf.customName(Component.text(definition.displayName()));
            spawnedWolf.setCustomNameVisible(true);
            spawnedWolf.setAdult();
            spawnedWolf.setTamed(true);
            spawnedWolf.setOwner(owner);
            spawnedWolf.setSitting(false);
            spawnedWolf.setCollarColor(collarColor);
            spawnedWolf.setRemoveWhenFarAway(false);
            spawnedWolf.setPersistent(true);
            petKeys.markPet(spawnedWolf, owner.getUniqueId(), definition.id());
        });
    }
}
