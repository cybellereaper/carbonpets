package com.github.cybellereaper.carbon.pets;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class PetService {

    private final PetCatalog petCatalog;
    private final PetKeys petKeys;
    private final PetVisualService petVisualService;
    private final Map<UUID, ActivePet> petsByOwnerId = new ConcurrentHashMap<>();
    private final Map<UUID, ActivePet> petsByEntityId = new ConcurrentHashMap<>();

    public PetService(PetCatalog petCatalog, PetKeys petKeys, PetVisualService petVisualService) {
        this.petCatalog = petCatalog;
        this.petKeys = petKeys;
        this.petVisualService = petVisualService;
    }

    public Optional<PetDefinition> findDefinition(String petId) {
        return petCatalog.find(petId);
    }

    public Collection<String> knownPetIds() {
        return petCatalog.allIds();
    }

    public ResolvedPet summon(Player owner, PetDefinition definition) {
        despawnPet(owner.getUniqueId());

        LivingEntity petEntity = definition.spawner().spawn(owner, definition, petKeys);
        ActivePet activePet = new ActivePet(owner.getUniqueId(), petEntity.getUniqueId(), definition.id());

        petsByOwnerId.put(owner.getUniqueId(), activePet);
        petsByEntityId.put(petEntity.getUniqueId(), activePet);

        petVisualService.playSummonEffect(petEntity);

        return new ResolvedPet(activePet, owner, petEntity, definition);
    }

    public Optional<ResolvedPet> resolveByEntityId(UUID entityId) {
        ActivePet activePet = petsByEntityId.get(entityId);
        if (activePet == null) {
            return Optional.empty();
        }

        return resolve(activePet);
    }

    public Collection<ActivePet> activePets() {
        return new ArrayList<>(petsByOwnerId.values());
    }

    public Optional<ResolvedPet> resolve(ActivePet activePet) {
        Player owner = Bukkit.getPlayer(activePet.ownerId());
        if (owner == null || !owner.isOnline()) {
            return Optional.empty();
        }

        Entity entity = Bukkit.getEntity(activePet.entityId());
        if (!(entity instanceof LivingEntity livingEntity) || !livingEntity.isValid()) {
            return Optional.empty();
        }

        Optional<PetDefinition> definition = petCatalog.find(activePet.definitionId());
        if (definition.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new ResolvedPet(activePet, owner, livingEntity, definition.get()));
    }

    public boolean isOwnedPet(Player player, Entity entity) {
        ActivePet activePet = petsByEntityId.get(entity.getUniqueId());
        return activePet != null && activePet.ownerId().equals(player.getUniqueId());
    }

    public void despawnPet(UUID ownerId) {
        ActivePet activePet = petsByOwnerId.remove(ownerId);
        if (activePet == null) {
            return;
        }

        petsByEntityId.remove(activePet.entityId());

        Entity entity = Bukkit.getEntity(activePet.entityId());
        if (entity != null && entity.isValid()) {
            Location currentLocation = entity.getLocation();
            petVisualService.playDismissEffect(currentLocation);
            entity.remove();
        }
    }

    public void forgetByEntityId(UUID entityId) {
        ActivePet activePet = petsByEntityId.remove(entityId);
        if (activePet != null) {
            petsByOwnerId.remove(activePet.ownerId());
        }
    }

    public void forget(ActivePet activePet) {
        petsByOwnerId.remove(activePet.ownerId());
        petsByEntityId.remove(activePet.entityId());
    }

    public void despawnAll() {
        for (UUID ownerId : new ArrayList<>(petsByOwnerId.keySet())) {
            despawnPet(ownerId);
        }
    }
}