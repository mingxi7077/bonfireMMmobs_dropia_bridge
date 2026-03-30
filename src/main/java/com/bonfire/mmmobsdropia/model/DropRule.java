package com.bonfire.mmmobsdropia.model;

import java.util.concurrent.ThreadLocalRandom;

public class DropRule {
    private final String iaId;
    private final double chance;
    private final int minAmount;
    private final int maxAmount;

    public DropRule(String iaId, double chance, int minAmount, int maxAmount) {
        this.iaId = iaId;
        this.chance = chance;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public String getIaId() {
        return iaId;
    }

    public boolean shouldDrop() {
        return ThreadLocalRandom.current().nextDouble(100.0D) < chance;
    }

    public int rollAmount() {
        if (minAmount >= maxAmount) {
            return minAmount;
        }
        return ThreadLocalRandom.current().nextInt(minAmount, maxAmount + 1);
    }
}
