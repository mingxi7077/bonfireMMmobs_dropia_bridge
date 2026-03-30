package com.bonfire.mmmobsdropia.command;

import com.bonfire.mmmobsdropia.BonfireMMmobsDropIaBridge;
import com.bonfire.mmmobsdropia.hook.ItemsAdderHook;
import com.bonfire.mmmobsdropia.service.DropConfig;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MMmobsDropIaCommand implements CommandExecutor, TabCompleter {
    private final BonfireMMmobsDropIaBridge plugin;
    private final ItemsAdderHook itemsAdderHook;
    private final DropConfig dropConfig;

    public MMmobsDropIaCommand(BonfireMMmobsDropIaBridge plugin, ItemsAdderHook itemsAdderHook, DropConfig dropConfig) {
        this.plugin = plugin;
        this.itemsAdderHook = itemsAdderHook;
        this.dropConfig = dropConfig;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/mmdropia <reload|spawn>");
            return true;
        }

        if ("reload".equalsIgnoreCase(args[0])) {
            dropConfig.reload();
            sender.sendMessage("bonfireMMmobs_dropia_bridge config reloaded.");
            return true;
        }

        if ("spawn".equalsIgnoreCase(args[0])) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players can use /mmdropia spawn.");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage("/mmdropia spawn <ia_id> [amount]");
                return true;
            }

            int amount = 1;
            if (args.length >= 3) {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage("Amount must be a number.");
                    return true;
                }
            }
            if (amount < 1) {
                sender.sendMessage("Amount must be at least 1.");
                return true;
            }

            ItemStack itemStack = itemsAdderHook.createItem(args[1], amount);
            if (itemStack == null) {
                sender.sendMessage("Failed to create IA item: " + args[1]);
                return true;
            }

            Location location = player.getLocation();
            location.getWorld().dropItemNaturally(location, itemStack);
            sender.sendMessage("Spawned " + amount + "x " + args[1] + ".");
            return true;
        }

        sender.sendMessage("/mmdropia <reload|spawn>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("reload");
            completions.add("spawn");
        } else if (args.length == 2 && "spawn".equalsIgnoreCase(args[0])) {
            completions.add("nogs_menagerie:nm_rhino_horn");
            completions.add("nogs_menagerie:nm_capybara_pelt");
            completions.add("nogs_menagerie:nm_shark_fin");
            completions.add("nogs_menagerie:nm_shark_tooth");
            completions.add("nogs_menagerie:nm_bald_eagle_feather");
        }
        return completions;
    }
}
