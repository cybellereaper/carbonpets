package com.github.cybellereaper.carbon.pets;

import java.time.Duration;

public interface ManualTalent extends PetTalent {
    Duration cooldown();

    void execute(TalentExecutionContext context);
}
