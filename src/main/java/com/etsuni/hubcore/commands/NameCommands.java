package com.etsuni.hubcore.commands;

import com.etsuni.hubcore.HubCore;
import com.etsuni.hubcore.utils.DBUtils;
import com.etsuni.hubcore.utils.PlayerName;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                    return true;
                }
            }
            else if(command.getName().equalsIgnoreCase("namehistory")) {
                if(args.length > 0) {
                    if(Bukkit.getPlayer(args[0]) != null) {
                        DBUtils dbUtils = new DBUtils(plugin);
                        Optional<List<PlayerName>> namesList = dbUtils.getPlayerNameHistory(Bukkit.getPlayer(args[0]));
                        sender.sendMessage(ChatColor.GOLD + "Name history for " + args[0]);

                        String allNames = "All Names: ";
                        for(PlayerName name : namesList.get()) {
                            allNames = allNames.concat(name + ", ");
                        }

                        String nickNames = "10 recent nicknames: ";
                        for(int i = 0; i < 10; i++) {
                            nickNames = nickNames.concat(namesList.get().get(i) + ", ");
                        }
                    }
                }
            }
        }

        return false;
    }
}
