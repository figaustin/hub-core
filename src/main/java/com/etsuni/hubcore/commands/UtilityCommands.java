package com.etsuni.hubcore.commands;

import org.bukkit.*;
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
                player.setAllowFlight(true);
                player.setFlying(true);
                return true;
            }

            //SPEED COMMAND
            if(command.getName().equalsIgnoreCase("speed")) {
                if(args.length > 0) {
                    try {
                        if (player.isFlying()) {
                            try {
                                if (player.getFlySpeed() == Float.parseFloat(args[0])) {
                                    player.sendMessage("same speed");
                                }
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
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(ChatColor.RED  + "Number is either too high or too low, please enter a value from 0.00 to 1.00");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Please specify an amount!");
                }
            }

            if(command.getName().equalsIgnoreCase("skull")) {
                if(args.length > 0) {
                    OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
                    ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    meta.setOwningPlayer(p);
                    item.setItemMeta(meta);
                    player.getInventory().addItem(item);
                    player.updateInventory();
                } else {
                    player.sendMessage(ChatColor.RED + "Please specify a player!");
                }
            }
        }
        return false;
    }
}
