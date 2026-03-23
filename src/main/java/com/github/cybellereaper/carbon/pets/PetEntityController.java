package com.github.cybellereaper.carbon.pets;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public final class PetEntityController {

    private final Plugin plugin;
    private final PetService petService;
    private final PetMovementPlanner movementPlanner;
    private final PetVisualService petVisualService;
    private final long tickPeriod;

    private BukkitTask tickTask;

    public PetEntityController(
            Plugin plugin,
            PetService petService,
            PetMovementPlanner movementPlanner,
            PetVisualService petVisualService
    ) {
        this(plugin, petService, movementPlanner, petVisualService, PetControllerConfig.standard().tickPeriod());
    }

    public PetEntityController(
            Plugin plugin,
            PetService petService,
            PetMovementPlanner movementPlanner,
            PetVisualService petVisualService,
            long tickPeriod
    ) {
        this.plugin = plugin;
        this.petService = petService;
        this.movementPlanner = movementPlanner;
        this.petVisualService = petVisualService;
        this.tickPeriod = tickPeriod;
    }

    public void start() {
        if (tickTask != null) {
            return;
        }

        tickTask = plugin.getServer().getScheduler().runTaskTimer(plugin, this::tick, tickPeriod, tickPeriod);
    }

    public void stop() {
        if (tickTask != null) {
            tickTask.cancel();
            tickTask = null;
        }
    }

    private void tick() {
        for (ActivePet activePet : petService.activePets()) {
            petService.resolve(activePet).ifPresentOrElse(
                    this::controlPet,
                    () -> petService.forget(activePet)
            );
        }
    }

    private void controlPet(ResolvedPet pet) {
        Player owner = pet.owner();
        LivingEntity petEntity = pet.petEntity();

        if (!owner.isOnline() || owner.isDead()) {
            petService.despawnPet(owner.getUniqueId());
            return;
        }

        keepPetResponsive(petEntity);

        Location ownerAnchor = ownerFollowAnchor(owner);
        Location petLocation = petEntity.getLocation();

        boolean sameWorld = ownerAnchor.getWorld() != null && ownerAnchor.getWorld().equals(petLocation.getWorld());

        double deltaX = ownerAnchor.getX() - petLocation.getX();
        double deltaY = ownerAnchor.getY() - petLocation.getY();
        double deltaZ = ownerAnchor.getZ() - petLocation.getZ();

        PetMovementDecision decision = movementPlanner.plan(sameWorld, deltaX, deltaY, deltaZ);

        switch (decision.action()) {
            case IDLE -> slowDown(petEntity);
            case FOLLOW -> follow(petEntity, owner, decision.velocity());
            case TELEPORT -> recoverToOwner(petEntity, owner);
        }
    }

    private void keepPetResponsive(LivingEntity petEntity) {
        if (petEntity instanceof Mob mob) {
            mob.setTarget(null);
            mob.setAware(true);
        }

        petEntity.setPersistent(true);
        petEntity.setRemoveWhenFarAway(false);
    }

    private void slowDown(LivingEntity petEntity) {
        petEntity.setVelocity(petEntity.getVelocity().multiply(0.45D));
    }

    private void follow(LivingEntity petEntity, Player owner, Vector plannedVelocity) {
        Vector smoothedVelocity = petEntity.getVelocity().multiply(0.35D).add(plannedVelocity);

        if (petEntity.isOnGround() && plannedVelocity.getY() > 0.0D) {
            smoothedVelocity.setY(plannedVelocity.getY());
        }

        petEntity.setVelocity(smoothedVelocity);

//        petEntity.lookAt(
//                owner.getX(),
//                owner.getEyeLocation().getY(),
//                owner.getZ(),
//                LookAnchor.EYES
//        );

        petVisualService.playStepEffect(petEntity);
    }

    private void recoverToOwner(LivingEntity petEntity, Player owner) {
        Location from = petEntity.getLocation();
        Location target = ownerTeleportAnchor(owner);

        boolean teleported = petEntity.teleport(target, PlayerTeleportEvent.TeleportCause.PLUGIN);
        if (teleported) {
            petVisualService.playRecoveryTeleportEffect(from, petEntity);
        }
    }

    private Location ownerFollowAnchor(Player owner) {
        return owner.getLocation();
    }

    private Location ownerTeleportAnchor(Player owner) {
        Location location = owner.getLocation().clone();
        Vector direction = location.getDirection();

        if (direction.lengthSquared() > 0.0001D) {
            location.add(direction.normalize().multiply(-1.5D));
        }

        return location;
    }
}
