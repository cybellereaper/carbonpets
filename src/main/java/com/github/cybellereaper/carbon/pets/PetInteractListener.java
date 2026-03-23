package com.github.cybellereaper.carbon.pets;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Optional;

public final class PetInteractListener implements Listener {

    private final PetService petService;
    private final TalentEngine talentEngine;

    public PetInteractListener(PetService petService, TalentEngine talentEngine) {
        this.petService = petService;
        this.talentEngine = talentEngine;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPetRightClick(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (!(event.getRightClicked() instanceof LivingEntity)) {
            return;
        }

        Optional<ResolvedPet> resolvedPet = petService.resolveByEntityId(event.getRightClicked().getUniqueId());
        if (resolvedPet.isEmpty()) {
            return;
        }

        if (!resolvedPet.get().owner().getUniqueId().equals(event.getPlayer().getUniqueId())) {
            return;
        }

        event.setCancelled(true);
        talentEngine.triggerManualTalents(resolvedPet.get());
    }

    @EventHandler
    public void onOwnerQuit(PlayerQuitEvent event) {
        petService.despawnPet(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onOwnerDeath(PlayerDeathEvent event) {
        petService.despawnPet(event.getEntity().getUniqueId());
    }

    @EventHandler
    public void onPetDeath(EntityDeathEvent event) {
        petService.forgetByEntityId(event.getEntity().getUniqueId());
    }
}
