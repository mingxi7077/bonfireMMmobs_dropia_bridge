package com.bonfire.mmmobsdropia.hook;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public class MythicMobsHook {
    private final Logger logger;
    private Method mythicInstMethod;
    private Method getApiHelperMethod;
    private Method isMythicMobMethod;
    private Method getMythicMobInstanceMethod;
    private Method getMobTypeMethod;

    public MythicMobsHook(Plugin plugin) {
        this.logger = plugin.getLogger();
    }

    public boolean init() {
        if (!Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            logger.warning("MythicMobs is not enabled.");
            return false;
        }

        try {
            Class<?> mythicBukkitClass = Class.forName("io.lumine.mythic.bukkit.MythicBukkit");
            Class<?> apiHelperClass = Class.forName("io.lumine.mythic.bukkit.BukkitAPIHelper");
            Class<?> activeMobClass = Class.forName("io.lumine.mythic.core.mobs.ActiveMob");

            mythicInstMethod = mythicBukkitClass.getMethod("inst");
            getApiHelperMethod = mythicBukkitClass.getMethod("getAPIHelper");
            isMythicMobMethod = apiHelperClass.getMethod("isMythicMob", Entity.class);
            getMythicMobInstanceMethod = apiHelperClass.getMethod("getMythicMobInstance", Entity.class);
            getMobTypeMethod = activeMobClass.getMethod("getMobType");
            logger.info("MythicMobs hook initialized.");
            return true;
        } catch (ReflectiveOperationException ex) {
            logger.warning("Failed to initialize MythicMobs hook: " + ex.getMessage());
            return false;
        }
    }

    public String getMobId(Entity entity) {
        try {
            Object mythic = mythicInstMethod.invoke(null);
            Object apiHelper = getApiHelperMethod.invoke(mythic);
            boolean isMythic = (boolean) isMythicMobMethod.invoke(apiHelper, entity);
            if (!isMythic) {
                return null;
            }

            Object activeMob = getMythicMobInstanceMethod.invoke(apiHelper, entity);
            if (activeMob == null) {
                return null;
            }
            return (String) getMobTypeMethod.invoke(activeMob);
        } catch (ReflectiveOperationException ex) {
            logger.warning("Failed to resolve MythicMob id: " + ex.getMessage());
            return null;
        }
    }
}
