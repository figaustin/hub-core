package com.etsuni.hubcore.commands;

import com.etsuni.hubcore.HubCore;
import org.bukkit.ChatColor;
import utils.DBUtils;
import utils.PlayerName;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NameCommands implements CommandExecutor {
    private final HubCore plugin;

    public NameCommands(HubCore instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(command.getName().equalsIgnoreCase("rename")) {
                if(args.length > 0) {
                    String name = args[0];
                    ((Player) sender).setDisplayName(name);
                    DBUtils dbUtils = new DBUtils(plugin);
                    dbUtils.addPlayersNewNameToDb(((Player) sender).getPlayer(), name, true);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getMessagesConfig().getString("prefix") +
                            plugin.getMessagesConfig()
                            .getString("change_name").replace("%name%", name)));
                    return true;
                }
            }
            else if(command.getName().equalsIgnoreCase("namehistory")) {
                if(args.length > 0) {
                    if(Bukkit.getPlayer(args[0]) != null) {
                        DBUtils dbUtils = new DBUtils(plugin);
                        Optional<List<PlayerName>> namesList = dbUtils.getPlayerNameHistory(Bukkit.getPlayer(args[0]));
                        sender.sendMessage("Name history for " + args[0]);

                        List<String> allNames = new ArrayList<>();
                        for(PlayerName name : namesList.get()) {
                            allNames.add(name.getName());
                        }

                        List<String> nickNames = new ArrayList<>();
                        int counter = 0;
                        for(PlayerName name : namesList.get()) {
                            if(name.getNickname() && counter < 10) {
                                nickNames.add(name.getName());
                                counter++;
                            }
                        }
                        for(String str : plugin.getMessagesConfig().getStringList("name_history")) {
                            if(str.contains("%player%")) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("prefix") +
                                        str.replace("%player%", ((Player) sender).getDisplayName())));
                            }
                            else if(str.contains("%allnames%")) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getMessagesConfig().getString("prefix") +
                                        str.replace("%allnames%", allNames.toString()
                                        .replace("[", "")
                                        .replace("]", ""))));
                            }
                            else if(str.contains("%nicknames%")) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getMessagesConfig().getString("prefix") +
                                        str.replace("%nicknames%", nickNames.toString()
                                        .replace("[", "")
                                        .replace("]", ""))));
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
}
