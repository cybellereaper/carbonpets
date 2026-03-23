package com.github.cybellereaper.carbon.pets;

import java.time.Duration;

public interface PassiveTalent extends PetTalent {
    Duration interval();

    void execute(TalentExecutionContext context);
}
