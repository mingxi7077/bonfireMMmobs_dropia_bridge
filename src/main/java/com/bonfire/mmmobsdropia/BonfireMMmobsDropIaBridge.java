package com.bonfire.mmmobsdropia;

import com.bonfire.mmmobsdropia.command.MMmobsDropIaCommand;
import com.bonfire.mmmobsdropia.hook.ItemsAdderHook;
import com.bonfire.mmmobsdropia.hook.MythicMobsHook;
import com.bonfire.mmmobsdropia.listener.MythicMobDeathListener;
import com.bonfire.mmmobsdropia.service.DropConfig;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BonfireMMmobsDropIaBridge extends JavaPlugin {
    private ItemsAdderHook itemsAdderHook;
    private MythicMobsHook mythicMobsHook;
    private DropConfig dropConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.itemsAdderHook = new ItemsAdderHook(this);
        this.mythicMobsHook = new MythicMobsHook(this);
        if (!itemsAdderHook.init() || !mythicMobsHook.init()) {
            getLogger().warning("Required runtime hooks are unavailable. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.dropConfig = new DropConfig(this);
        dropConfig.reload();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new MythicMobDeathListener(this, mythicMobsHook, itemsAdderHook, dropConfig), this);

        PluginCommand command = getCommand("mmdropia");
        if (command != null) {
            MMmobsDropIaCommand executor = new MMmobsDropIaCommand(this, itemsAdderHook, dropConfig);
            command.setExecutor(executor);
            command.setTabCompleter(executor);
        }

        getLogger().info("bonfireMMmobs_dropia_bridge enabled with " + dropConfig.getMobCount() + " mob entries.");
    }

    public DropConfig getDropConfig() {
        return dropConfig;
    }
}
