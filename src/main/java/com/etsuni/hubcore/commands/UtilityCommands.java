package com.etsuni.hubcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class UtilityCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            //FLY COMMAND
            if(command.getName().equalsIgnoreCase("fly")) {
                player.setFlying(true);
                return true;
            }

            //SPEED COMMAND
            if(command.getName().equalsIgnoreCase("speed")) {
                if(args.length > 0) {
                    if(player.isFlying()) {
                        try {
                            player.setFlySpeed(Float.parseFloat(args[0]));
                        } catch (NumberFormatException e) {
                            player.sendMessage(ChatColor.RED + "Invalid number.");
                        }
                    } else {
                        try {
                            player.setWalkSpeed(Float.parseFloat(args[0]));
                        } catch (NumberFormatException e) {
                            player.sendMessage(ChatColor.RED + "Invalid number.");
                        }
                    }
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + "Please specify an amount!");
                }

            }
            if(command.getName().equalsIgnoreCase("skull")) {
                if(args.length > 0) {
                    OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
                    ItemStack item = new ItemStack(Material.SKULL);
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    meta.setOwningPlayer(p);
                    item.setItemMeta(meta);
                    player.getInventory().addItem(item);
                } else {
                    player.sendMessage(ChatColor.RED + "Please specify a player!");
                }
            }
        }
        return false;
    }
}
