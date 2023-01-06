package com.etsuni.hubcore.commands;

import com.etsuni.hubcore.HubCore;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommands implements CommandExecutor {

    private final HubCore plugin;

    public GamemodeCommands(HubCore instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {


            if(command.getName().equalsIgnoreCase("gmc")) {
                ((Player) sender).setGameMode(GameMode.CREATIVE);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getMessagesConfig().getString("prefix") +
                        plugin.getMessagesConfig()
                        .getString("change_gamemode").replace("%gamemode%", "creative")));
                return true;
            }
            else if(command.getName().equalsIgnoreCase("gms")) {
                ((Player) sender).setGameMode(GameMode.SURVIVAL);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getMessagesConfig().getString("prefix") +
                        plugin.getMessagesConfig()
                        .getString("change_gamemode").replace("%gamemode%", "survival")));
                return true;
            }
            else if(command.getName().equalsIgnoreCase("gma")) {
                ((Player) sender).setGameMode(GameMode.ADVENTURE);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getMessagesConfig().getString("prefix") +
                        plugin.getMessagesConfig()
                        .getString("change_gamemode").replace("%gamemode%", "adventure")));
                return true;
            }
            else if(command.getName().equalsIgnoreCase("gmsp")) {
                ((Player) sender).setGameMode(GameMode.SPECTATOR);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getMessagesConfig().getString("prefix") +
                        plugin.getMessagesConfig()
                        .getString("change_gamemode").replace("%gamemode%", "spectator")));
                return true;
            }
        }
        return false;
    }
}
