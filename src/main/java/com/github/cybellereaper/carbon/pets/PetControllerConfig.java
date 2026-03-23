package com.github.cybellereaper.carbon.pets;

public record PetControllerConfig(
        double followStartDistance,
        double sprintStartDistance,
        double teleportDistance,
        double walkSpeed,
        double sprintSpeed,
        double stepUpVelocity,
        long tickPeriod
) {
    public static PetControllerConfig standard() {
        return new PetControllerConfig(
                2.25D,
                8.0D,
                20.0D,
                0.30D,
                0.42D,
                0.35D,
                10L
        );
    }

    public double followStartDistanceSquared() {
        return followStartDistance * followStartDistance;
    }

    public double sprintStartDistanceSquared() {
        return sprintStartDistance * sprintStartDistance;
    }

    public double teleportDistanceSquared() {
        return teleportDistance * teleportDistance;
    }
}
