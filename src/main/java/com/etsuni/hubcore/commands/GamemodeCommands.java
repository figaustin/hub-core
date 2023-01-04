package com.etsuni.hubcore.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(command.getName().equalsIgnoreCase("gmc")) {
                ((Player) sender).setGameMode(GameMode.CREATIVE);
                return true;
            }
            else if(command.getName().equalsIgnoreCase("gms")) {
                ((Player) sender).setGameMode(GameMode.SURVIVAL);
                return true;
            }
            else if(command.getName().equalsIgnoreCase("gma")) {
                ((Player) sender).setGameMode(GameMode.ADVENTURE);
                return true;
            }
            else if(command.getName().equalsIgnoreCase("gmsp")) {
                ((Player) sender).setGameMode(GameMode.SPECTATOR);
                return true;
            }
        }
        return false;
    }
}
