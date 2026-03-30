package com.bonfire.mmmobsdropia.service;

import com.bonfire.mmmobsdropia.model.DropRule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropConfig {
    private final JavaPlugin plugin;
    private final Map<String, List<DropRule>> dropsByMob = new HashMap<>();
    private boolean debug;

    public DropConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        plugin.reloadConfig();
        dropsByMob.clear();
        debug = plugin.getConfig().getBoolean("debug", false);

        ConfigurationSection drops = plugin.getConfig().getConfigurationSection("drops");
        if (drops == null) {
            return;
        }

        for (String mobId : drops.getKeys(false)) {
            List<Map<?, ?>> entries = drops.getMapList(mobId);
            List<DropRule> rules = new ArrayList<>();
            for (Map<?, ?> entry : entries) {
                DropRule rule = parseRule(mobId, entry);
                if (rule != null) {
                    rules.add(rule);
                }
            }
            if (!rules.isEmpty()) {
                dropsByMob.put(mobId, List.copyOf(rules));
            }
        }
    }

    public List<DropRule> getRules(String mobId) {
        return dropsByMob.getOrDefault(mobId, Collections.emptyList());
    }

    public boolean isDebug() {
        return debug;
    }

    public int getMobCount() {
        return dropsByMob.size();
    }

    private DropRule parseRule(String mobId, Map<?, ?> raw) {
        Object iaIdObject = raw.get("ia_id");
        Object chanceObject = raw.get("chance");
        if (!(iaIdObject instanceof String iaId) || iaId.isBlank()) {
            plugin.getLogger().warning("Skipping invalid drop entry for " + mobId + ": missing ia_id");
            return null;
        }
        if (!(chanceObject instanceof Number chanceNumber)) {
            plugin.getLogger().warning("Skipping invalid drop entry for " + mobId + ": missing numeric chance");
            return null;
        }

        int minAmount;
        int maxAmount;
        Object amountObject = raw.get("amount");
        Object rangeObject = raw.get("amount_range");
        if (amountObject instanceof Number amountNumber) {
            minAmount = amountNumber.intValue();
            maxAmount = amountNumber.intValue();
        } else if (amountObject instanceof String amountString && !amountString.isBlank()) {
            int[] range = parseRange(mobId, amountString);
            if (range == null) {
                return null;
            }
            minAmount = range[0];
            maxAmount = range[1];
        } else if (rangeObject instanceof String rangeString && !rangeString.isBlank()) {
            int[] range = parseRange(mobId, rangeString);
            if (range == null) {
                return null;
            }
            minAmount = range[0];
            maxAmount = range[1];
        } else {
            plugin.getLogger().warning("Skipping invalid drop entry for " + mobId + ": missing amount or amount_range");
            return null;
        }

        if (minAmount < 1 || maxAmount < minAmount) {
            plugin.getLogger().warning("Skipping invalid drop entry for " + mobId + ": invalid amount bounds");
            return null;
        }

        return new DropRule(iaId, chanceNumber.doubleValue(), minAmount, maxAmount);
    }

    private int[] parseRange(String mobId, String rawRange) {
        String[] parts = rawRange.trim().split("-");
        if (parts.length == 1) {
            try {
                int amount = Integer.parseInt(parts[0].trim());
                return new int[]{amount, amount};
            } catch (NumberFormatException ex) {
                plugin.getLogger().warning("Skipping invalid drop range for " + mobId + ": " + rawRange);
                return null;
            }
        }
        if (parts.length != 2) {
            plugin.getLogger().warning("Skipping invalid drop range for " + mobId + ": " + rawRange);
            return null;
        }
        try {
            int min = Integer.parseInt(parts[0].trim());
            int max = Integer.parseInt(parts[1].trim());
            return new int[]{min, max};
        } catch (NumberFormatException ex) {
            plugin.getLogger().warning("Skipping invalid drop range for " + mobId + ": " + rawRange);
            return null;
        }
    }
}
