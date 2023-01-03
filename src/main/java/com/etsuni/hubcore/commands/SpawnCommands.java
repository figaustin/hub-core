package com.etsuni.hubcore.commands;

import com.etsuni.hubcore.HubCore;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommands implements CommandExecutor {
    private final HubCore plugin;

    public SpawnCommands(HubCore instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(command.getName().equalsIgnoreCase("setspawn")) {
                plugin.getCfg().set("spawn.location", CommandUtils.makeLocationString(((Player) sender).getLocation()));
                return true;
            }
            else if(command.getName().equalsIgnoreCase("spawn")) {
                Location location = CommandUtils.parseLocationString(plugin.getCfg().getString("spawn.location"));
                ((Player) sender).teleport(location);
                return true;
            }
        }
        return false;
    }
}
