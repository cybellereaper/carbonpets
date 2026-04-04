package com.github.cybellereaper.carbon.pets.talents;

import com.github.cybellereaper.carbon.pets.PassiveTalent;
import com.github.cybellereaper.carbon.pets.PetStat;
import com.github.cybellereaper.carbon.pets.TalentExecutionContext;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.time.Duration;

public final class OwnerHealPulseTalent implements PassiveTalent {

    @Override
    public String id() {
        return "owner_heal_pulse";
    }

    @Override
    public Duration interval() {
        return Duration.ofSeconds(4);
    }

    @Override
    public void execute(TalentExecutionContext context) {
        Player owner = context.owner();

        if (!owner.getWorld().equals(context.pet().getWorld())) {
            return;
        }

        if (owner.getLocation().distanceSquared(context.pet().getLocation()) > 64.0D) {
            return;
        }

        double maxHealth = owner.getMaxHealth();
        double healAmount = 1.0D * context.stats().value(PetStat.SPIRIT);

        double nextHealth = Math.min(maxHealth, owner.getHealth() + healAmount);
        if (nextHealth <= owner.getHealth()) {
            return;
        }

        owner.setHealth(nextHealth);
        context.pet().getWorld().spawnParticle(
                Particle.HEART,
                context.pet().getLocation().add(0.0D, 1.0D, 0.0D),
                3
        );
    }
}