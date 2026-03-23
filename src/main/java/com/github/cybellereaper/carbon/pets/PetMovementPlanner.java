package com.github.cybellereaper.carbon.pets;

import org.bukkit.util.Vector;

public final class PetMovementPlanner {

    private final PetControllerConfig config;

    public PetMovementPlanner(PetControllerConfig config) {
        this.config = config;
    }

    public PetMovementDecision plan(boolean sameWorld, double deltaX, double deltaY, double deltaZ) {
        if (!sameWorld) {
            return PetMovementDecision.teleport();
        }

        double distanceSquared = square(deltaX) + square(deltaY) + square(deltaZ);
        if (distanceSquared >= config.teleportDistanceSquared()) {
            return PetMovementDecision.teleport();
        }

        if (distanceSquared <= config.followStartDistanceSquared()) {
            return PetMovementDecision.idle();
        }

        double speed = distanceSquared >= config.sprintStartDistanceSquared()
                ? config.sprintSpeed()
                : config.walkSpeed();

        Vector velocity = new Vector(deltaX, 0.0D, deltaZ);
        if (velocity.lengthSquared() <= 0.0001D) {
            return PetMovementDecision.idle();
        }

        velocity.normalize().multiply(speed);

        if (deltaY > 1.1D) {
            velocity.setY(config.stepUpVelocity());
        }

        return PetMovementDecision.follow(velocity);
    }

    private double square(double value) {
        return value * value;
    }
}
