package com.github.cybellereaper.carbon.pets;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public final class TalentEngine {
    private final Plugin plugin;
    private final PetService petService;
    private final CooldownService cooldownService;
    private final IntervalGate intervalGate;
    private final ChanceService chanceService;

    private BukkitTask tickTask;

    public TalentEngine(
            Plugin plugin,
            PetService petService,
            CooldownService cooldownService,
            IntervalGate intervalGate,
            ChanceService chanceService
    ) {
        this.plugin = plugin;
        this.petService = petService;
        this.cooldownService = cooldownService;
        this.intervalGate = intervalGate;
        this.chanceService = chanceService;
    }

    public void start() {
        if (tickTask != null) {
            return;
        }

        tickTask = plugin.getServer().getScheduler().runTaskTimer(plugin, this::tick, 20L, 20L);
    }

    public void stop() {
        if (tickTask != null) {
            tickTask.cancel();
            tickTask = null;
        }
    }

    public void triggerManualTalents(ResolvedPet pet) {
        TalentExecutionContext context = contextOf(pet);
        boolean anyTriggered = false;

        for (ManualTalent talent : pet.definition().talents().manualTalents()) {
            String cooldownKey = manualCooldownKey(pet, talent);

            if (!cooldownService.tryUse(cooldownKey, talent.cooldown())) {
                continue;
            }

            talent.execute(context);
            anyTriggered = true;
        }

        if (!anyTriggered) {
            pet.owner().sendMessage("Your pet is recovering.");
        }
    }

    private void tick() {
        for (ActivePet activePet : petService.activePets()) {
            petService.resolve(activePet).ifPresentOrElse(
                    this::runTalents,
                    () -> cleanup(activePet)
            );
        }
    }

    private void runTalents(ResolvedPet pet) {
        TalentExecutionContext context = contextOf(pet);

        for (PassiveTalent passiveTalent : pet.definition().talents().passiveTalents()) {
            if (intervalGate.shouldRun(passiveRunKey(pet, passiveTalent), passiveTalent.interval())) {
                passiveTalent.execute(context);
            }
        }

        for (RandomProcTalent randomProcTalent : pet.definition().talents().randomProcTalents()) {
            boolean canRoll = intervalGate.shouldRun(procRunKey(pet, randomProcTalent), randomProcTalent.rollInterval());
            if (!canRoll) {
                continue;
            }

            if (chanceService.succeeds(boostedProcChance(randomProcTalent.procChance(), context.stats()))) {
                randomProcTalent.execute(context);
            }
        }
    }

    private double boostedProcChance(double baseProcChance, PetStatProfile stats) {
        return Math.min(0.85D, baseProcChance * stats.value(PetStat.AGILITY));
    }

    private TalentExecutionContext contextOf(ResolvedPet pet) {
        return new TalentExecutionContext(plugin, pet.owner(), pet.petEntity(), pet.definition(), pet.statProfile());
    }

    private void cleanup(ActivePet activePet) {
        String keyPrefix = activePet.entityId().toString() + ":";
        cooldownService.clear(keyPrefix);
        intervalGate.clear(keyPrefix);
        petService.forget(activePet);
    }

    private String passiveRunKey(ResolvedPet pet, PassiveTalent talent) {
        return pet.petEntity().getUniqueId() + ":passive:" + talent.id();
    }

    private String procRunKey(ResolvedPet pet, RandomProcTalent talent) {
        return pet.petEntity().getUniqueId() + ":proc:" + talent.id();
    }

    private String manualCooldownKey(ResolvedPet pet, ManualTalent talent) {
        return pet.petEntity().getUniqueId() + ":manual:" + talent.id();
    }
}
