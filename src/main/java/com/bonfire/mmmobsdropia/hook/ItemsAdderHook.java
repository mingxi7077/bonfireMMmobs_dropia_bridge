package com.bonfire.mmmobsdropia.hook;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class ItemsAdderHook {
    private final Plugin plugin;
    private final Logger logger;
    private final Set<String> missingItems = new HashSet<>();
    private Method getInstanceMethod;
    private Method getItemStackMethod;

    public ItemsAdderHook(Plugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    public boolean init() {
        if (!Bukkit.getPluginManager().isPluginEnabled("ItemsAdder")) {
            logger.warning("ItemsAdder is not enabled.");
            return false;
        }
        try {
            Class<?> customStackClass = Class.forName("dev.lone.itemsadder.api.CustomStack");
            getInstanceMethod = customStackClass.getMethod("getInstance", String.class);
            getItemStackMethod = customStackClass.getMethod("getItemStack");
            logger.info("ItemsAdder hook initialized.");
            return true;
        } catch (ReflectiveOperationException ex) {
            logger.warning("Failed to initialize ItemsAdder hook: " + ex.getMessage());
            return false;
        }
    }

    public ItemStack createItem(String namespacedId, int amount) {
        try {
            Object customStack = getInstanceMethod.invoke(null, namespacedId);
            if (customStack == null) {
                warnMissing(namespacedId);
                return null;
            }

            ItemStack itemStack = ((ItemStack) getItemStackMethod.invoke(customStack)).clone();
            itemStack.setAmount(amount);
            return itemStack;
        } catch (ReflectiveOperationException ex) {
            logger.warning("Failed to create IA item '" + namespacedId + "': " + ex.getMessage());
            return null;
        }
    }

    private void warnMissing(String namespacedId) {
        if (missingItems.add(namespacedId)) {
            logger.warning("ItemsAdder item not found: " + namespacedId);
        }
    }
}
