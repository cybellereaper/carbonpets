package com.github.cybellereaper.carbon.pets.talents;

import com.github.cybellereaper.carbon.pets.PetStat;
import com.github.cybellereaper.carbon.pets.RandomProcTalent;
import com.github.cybellereaper.carbon.pets.TalentExecutionContext;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;

public final class SpeedSurgeProcTalent implements RandomProcTalent {

    @Override
    public String id() {
        return "speed_surge";
    }

    @Override
    public Duration rollInterval() {
        return Duration.ofSeconds(5);
    }

    @Override
    public double procChance() {
        return 0.25D;
    }

    @Override
    public void execute(TalentExecutionContext context) {
        Player owner = context.owner();

        if (!owner.getWorld().equals(context.pet().getWorld())) {
            return;
        }

        if (owner.getLocation().distanceSquared(context.pet().getLocation()) > 100.0D) {
            return;
        }

        double agilityMultiplier = context.stats().value(PetStat.AGILITY);
        int durationTicks = (int) Math.round(100 * agilityMultiplier);
        int amplifier = agilityMultiplier >= 1.20D ? 1 : 0;

        owner.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, durationTicks, amplifier, true, true, true));
        context.pet().getWorld().spawnParticle(Particle.HAPPY_VILLAGER, context.pet().getLocation().add(0.0D, 0.8D, 0.0D), 10, 0.3D, 0.2D, 0.3D);
        owner.playSound(owner.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 0.8F, 1.3F);
    }
}
