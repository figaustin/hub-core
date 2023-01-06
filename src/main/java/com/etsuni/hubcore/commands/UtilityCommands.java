package com.etsuni.hubcore.commands;

import com.etsuni.hubcore.HubCore;
import net.md_5.bungee.protocol.packet.Chat;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class UtilityCommands implements CommandExecutor {

    private final HubCore plugin;

    public UtilityCommands(HubCore instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            //FLY COMMAND
            if(command.getName().equalsIgnoreCase("fly")) {
                if(player.getAllowFlight()) {
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig()
                            .getString("prefix") + plugin.getMessagesConfig().getString("disable_fly")));
                } else {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                            plugin.getMessagesConfig().getString("fly")));
                }
                return true;
            }

            //SPEED COMMAND
            else if(command.getName().equalsIgnoreCase("speed")) {
                if(args.length > 0) {
                    try {
                        if (player.isFlying()) {
                            try {
                                player.setFlySpeed(Float.parseFloat(args[0]));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getMessagesConfig().getString("prefix") +
                                        plugin.getMessagesConfig().getString("change_speed")
                                        .replace("%speed%", args[0])));
                            } catch (NumberFormatException e) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                                        plugin.getMessagesConfig()
                                        .getString("speed_invalid_number")));
                            }
                        } else {
                            try {
                                player.setWalkSpeed(Float.parseFloat(args[0]));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getMessagesConfig().getString("prefix") +
                                        plugin.getMessagesConfig().getString("change_speed")
                                        .replace("%speed%", args[0])));
                            } catch (NumberFormatException e) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                                        plugin.getMessagesConfig()
                                        .getString("speed_invalid_number")));
                            }
                        }
                        return true;
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                                plugin.getMessagesConfig()
                                .getString("speed_too_high_or_too_low")));
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                            plugin.getMessagesConfig().getString("speed_no_amount")));
                }
            }

            //SKULL COMMAND
            else if(command.getName().equalsIgnoreCase("skull")) {
                if(args.length > 0) {
                    OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
                    ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    meta.setOwningPlayer(p);
                    item.setItemMeta(meta);
                    player.getInventory().addItem(item);
                    player.updateInventory();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                            plugin.getMessagesConfig()
                            .getString("skull").replace("%player%", player.getName())));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                            plugin.getMessagesConfig()
                            .getString("skull_specify_player")));
                }
            }


        }
        return false;
    }
}
