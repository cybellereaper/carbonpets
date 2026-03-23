package com.github.cybellereaper.carbon.pets;

import java.util.concurrent.ThreadLocalRandom;

public final class ThreadLocalChanceService implements ChanceService {

    @Override
    public boolean succeeds(double probability) {
        if (probability <= 0.0D) {
            return false;
        }

        if (probability >= 1.0D) {
            return true;
        }

        return ThreadLocalRandom.current().nextDouble() < probability;
    }
}
