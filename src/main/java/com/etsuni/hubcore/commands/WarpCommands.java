package com.etsuni.hubcore.commands;

import com.etsuni.hubcore.HubCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class WarpCommands implements CommandExecutor {

    private final HubCore plugin;

    public WarpCommands(HubCore instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(command.getName().equalsIgnoreCase("setwarp")) {
                if(args.length > 0) {

                    Location location = ((Player) sender).getLocation();
                    Configuration config = plugin.getWarpsConfig();
                    ConfigurationSection section = config.createSection("warps." + args[0]);
                    section.set("location", CommandUtils.makeLocationString(location));
                    plugin.saveCfgs();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                            plugin.getMessagesConfig()
                            .getString("set_warp").replace("%warpname%", args[0])));
                    return true;
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                            plugin.getMessagesConfig()
                            .getString("set_warp_error")));
                    return false;
                }
            }

            else if(command.getName().equalsIgnoreCase("delwarp")) {
                if(args.length > 0) {
                    Configuration config = plugin.getWarpsConfig();

                    if(config.getConfigurationSection("warps") != null
                            && config.getConfigurationSection("warps").getKeys(false).contains(args[0]))
                    {
                        config.set("warps." + args[0], null);
                        plugin.saveCfgs();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                                plugin.getMessagesConfig()
                                .getString("del_warp").replace("%warpname", args[0])));
                        return true;
                    }

                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                            plugin.getMessagesConfig()
                            .getString("set_warp_error")));
                    return false;
                }
            }

            else if(command.getName().equalsIgnoreCase("warp")) {
                if(args.length > 0) {
                    Configuration config = plugin.getWarpsConfig();

                    if(config.getConfigurationSection("warps") != null
                            && config.getConfigurationSection("warps").getKeys(false).contains(args[0]))
                    {
                        ((Player) sender).teleport(
                                CommandUtils.parseLocationString(plugin.getWarpsConfig().getString("warps." + args[0] + ".location")));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                                plugin.getMessagesConfig()
                                .getString("warp").replace("%warpname%", args[0])));
                        return true;
                    }

                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                            plugin.getMessagesConfig()
                            .getString("set_warp_error")));
                    return false;
                }
            }

            else if(command.getName().equalsIgnoreCase("warps")) {
                Configuration config = plugin.getWarpsConfig();
                if(config.getConfigurationSection("warps") == null) {
                    sender.sendMessage(ChatColor.GOLD + "Warps List:");
                    return false;
                }
                Set<String> warps = config.getConfigurationSection("warps").getKeys(false);
                List<String> warpNames = new ArrayList<>(warps);

                for(String str : plugin.getMessagesConfig().getStringList("warps")) {
                    if(str.contains("%warps%")) {
                        for(String warp : warpNames) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                                    str.replace("%warps%", warp)));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                                str));
                    }
                }

                return true;
            }

        }

        return false;
    }
}
