package com.github.cybellereaper.carbon.pets;

import java.time.Duration;

public interface RandomProcTalent extends PetTalent {
    Duration rollInterval();

    double procChance();

    void execute(TalentExecutionContext context);
}
