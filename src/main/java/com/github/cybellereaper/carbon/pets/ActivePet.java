package com.github.cybellereaper.carbon.pets;

import java.util.UUID;

public record ActivePet(
        UUID ownerId,
        UUID entityId,
        String definitionId,
        PetGenetics genetics
) {
}
