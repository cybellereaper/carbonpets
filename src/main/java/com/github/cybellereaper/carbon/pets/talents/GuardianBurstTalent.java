package com.github.cybellereaper.carbon.pets.talents;

import com.github.cybellereaper.carbon.pets.ManualTalent;
import com.github.cybellereaper.carbon.pets.TalentExecutionContext;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.util.Vector;

import java.time.Duration;

public final class GuardianBurstTalent implements ManualTalent {

    @Override
    public String id() {
        return "guardian_burst";
    }

    @Override
    public Duration cooldown() {
        return Duration.ofSeconds(12);
    }

    @Override
    public void execute(TalentExecutionContext context) {
        for (Entity nearbyEntity : context.pet().getNearbyEntities(4.0D, 2.0D, 4.0D)) {
            if (!(nearbyEntity instanceof Monster monster)) {
                continue;
            }

            Vector knockback = monster.getLocation().toVector().subtract(context.pet().getLocation().toVector());
            if (knockback.lengthSquared() > 0.0001D) {
                knockback.normalize().multiply(1.2D);
            }
            knockback.setY(0.35D);

            monster.setVelocity(knockback);
            monster.damage(4.0D, context.pet());
        }

        context.pet().getWorld().spawnParticle(Particle.CRIT, context.pet().getLocation().add(0.0D, 0.8D, 0.0D), 25, 0.6D, 0.4D, 0.6D, 0.1D);
        context.owner().playSound(context.owner().getLocation(), Sound.ENTITY_WOLF_BIG_GROWL, 1.0F, 1.1F);
        context.owner().sendMessage("Your pet used Guardian Burst.");
    }
}
