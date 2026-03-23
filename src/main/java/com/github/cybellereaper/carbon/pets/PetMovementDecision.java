package com.github.cybellereaper.carbon.pets;

import org.bukkit.util.Vector;

public record PetMovementDecision(
        PetMovementAction action,
        Vector velocity
) {
    public static PetMovementDecision idle() {
        return new PetMovementDecision(PetMovementAction.IDLE, new Vector());
    }

    public static PetMovementDecision follow(Vector velocity) {
        return new PetMovementDecision(PetMovementAction.FOLLOW, velocity);
    }

    public static PetMovementDecision teleport() {
        return new PetMovementDecision(PetMovementAction.TELEPORT, new Vector());
    }
}
