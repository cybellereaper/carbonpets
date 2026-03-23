package com.github.cybellereaper.carbon.pets;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.UUID;

public final class PetKeys {

    private final NamespacedKey ownerIdKey;
    private final NamespacedKey definitionIdKey;

    public PetKeys(Plugin plugin) {
        this.ownerIdKey = new NamespacedKey(plugin, "owner_id");
        this.definitionIdKey = new NamespacedKey(plugin, "pet_definition_id");
    }

    public void markPet(LivingEntity petEntity, UUID ownerId, String definitionId) {
        PersistentDataContainer data = petEntity.getPersistentDataContainer();
        data.set(ownerIdKey, PersistentDataType.STRING, ownerId.toString());
        data.set(definitionIdKey, PersistentDataType.STRING, definitionId);
    }

    public Optional<UUID> readOwnerId(Entity entity) {
        String rawOwnerId = entity.getPersistentDataContainer().get(ownerIdKey, PersistentDataType.STRING);
        if (rawOwnerId == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(UUID.fromString(rawOwnerId));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }

    public Optional<String> readDefinitionId(Entity entity) {
        return Optional.ofNullable(entity.getPersistentDataContainer().get(definitionIdKey, PersistentDataType.STRING));
    }
}
