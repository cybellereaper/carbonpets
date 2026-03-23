package com.github.cybellereaper.carbon.pets;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

public final class PetVisualService {

    public void playSummonEffect(LivingEntity petEntity) {
        Location location = petEntity.getLocation().add(0.0D, 0.8D, 0.0D);
        petEntity.getWorld().spawnParticle(Particle.END_ROD, location, 16, 0.4D, 0.5D, 0.4D, 0.03D);
        petEntity.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, location, 10, 0.35D, 0.3D, 0.35D, 0.02D);
        petEntity.getWorld().playSound(location, Sound.ENTITY_WOLF_AMBIENT, 0.9F, 1.35F);
    }

    public void playDismissEffect(Location location) {
        Location centered = location.clone().add(0.0D, 0.8D, 0.0D);
        centered.getWorld().spawnParticle(Particle.CLOUD, centered, 18, 0.35D, 0.35D, 0.35D, 0.02D);
        centered.getWorld().spawnParticle(Particle.SMOKE, centered, 12, 0.3D, 0.25D, 0.3D, 0.01D);
        centered.getWorld().playSound(centered, Sound.ENTITY_WOLF_WHINE, 0.8F, 0.9F);
    }

    public void playRecoveryTeleportEffect(Location from, LivingEntity petEntity) {
        Location fromCenter = from.clone().add(0.0D, 0.8D, 0.0D);
        Location toCenter = petEntity.getLocation().add(0.0D, 0.8D, 0.0D);

        fromCenter.getWorld().spawnParticle(Particle.PORTAL, fromCenter, 24, 0.35D, 0.45D, 0.35D, 0.15D);
        petEntity.getWorld().spawnParticle(Particle.PORTAL, toCenter, 24, 0.35D, 0.45D, 0.35D, 0.15D);
        petEntity.getWorld().playSound(toCenter, Sound.ENTITY_ENDERMAN_TELEPORT, 0.55F, 1.6F);
    }

    public void playStepEffect(LivingEntity petEntity) {
        Location location = petEntity.getLocation().add(0.0D, 0.1D, 0.0D);
        petEntity.getWorld().spawnParticle(Particle.CRIT, location, 2, 0.12D, 0.04D, 0.12D, 0.0D);
    }
}
