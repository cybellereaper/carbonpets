package com.github.cybellereaper.carbon;

import com.github.cybellereaper.carbon.pets.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Clock;
import java.util.Objects;

public final class Carbon extends JavaPlugin {

    private PetService petService;
    private TalentEngine talentEngine;
    private PetEntityController petEntityController;

    @Override
    public void onEnable() {
        var clock = Clock.systemUTC();
        var petKeys = new PetKeys(this);
        var cooldownService = new CooldownService(clock);
        var intervalGate    = new IntervalGate(clock);
        var chanceService   = new ThreadLocalChanceService();

        var petVisualService = new PetVisualService();
        var petCatalog = new PetCatalog();
        this.petService = new PetService(petCatalog, petKeys, petVisualService);
        this.talentEngine = new TalentEngine(this, petService, cooldownService, intervalGate, chanceService);

        var petMovementPlanner = new PetMovementPlanner(PetControllerConfig.standard());
        this.petEntityController = new PetEntityController(this, petService, petMovementPlanner, petVisualService);


        getServer().getPluginManager().registerEvents(new PetInteractListener(petService, talentEngine), this);

        var petCommand = new PetCommand(petService);
        var pluginCommand = Objects.requireNonNull(getCommand("pet"), "Missing /pet command in plugin.yml");
        pluginCommand.setExecutor(petCommand);
        pluginCommand.setTabCompleter(petCommand);

        talentEngine.start();
        petEntityController.start();
    }

    @Override
    public void onDisable() {
        if (petEntityController != null) {
            petEntityController.stop();
        }
        if (talentEngine != null) {
            talentEngine.stop();
        }
        if (petService != null) {
            petService.despawnAll();
        }
    }
}
