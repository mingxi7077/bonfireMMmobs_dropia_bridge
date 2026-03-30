package com.bonfire.mmmobsdropia.listener;

import com.bonfire.mmmobsdropia.BonfireMMmobsDropIaBridge;
import com.bonfire.mmmobsdropia.hook.ItemsAdderHook;
import com.bonfire.mmmobsdropia.hook.MythicMobsHook;
import com.bonfire.mmmobsdropia.model.DropRule;
import com.bonfire.mmmobsdropia.service.DropConfig;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MythicMobDeathListener implements Listener {
    private final BonfireMMmobsDropIaBridge plugin;
    private final MythicMobsHook mythicMobsHook;
    private final ItemsAdderHook itemsAdderHook;
    private final DropConfig dropConfig;

    public MythicMobDeathListener(BonfireMMmobsDropIaBridge plugin, MythicMobsHook mythicMobsHook, ItemsAdderHook itemsAdderHook, DropConfig dropConfig) {
        this.plugin = plugin;
        this.mythicMobsHook = mythicMobsHook;
        this.itemsAdderHook = itemsAdderHook;
        this.dropConfig = dropConfig;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        String mobId = mythicMobsHook.getMobId(event.getEntity());
        if (mobId == null) {
            return;
        }

        List<DropRule> rules = dropConfig.getRules(mobId);
        if (rules.isEmpty()) {
            return;
        }

        Location location = event.getEntity().getLocation();
        for (DropRule rule : rules) {
            if (!rule.shouldDrop()) {
                continue;
            }

            ItemStack itemStack = itemsAdderHook.createItem(rule.getIaId(), rule.rollAmount());
            if (itemStack == null) {
                continue;
            }

            Item dropped = location.getWorld().dropItemNaturally(location, itemStack);
            if (dropConfig.isDebug()) {
                plugin.getLogger().info("Dropped " + itemStack.getAmount() + "x " + rule.getIaId() + " from " + mobId + " at " + location);
            }
            dropped.setPickupDelay(10);
        }
    }
}
