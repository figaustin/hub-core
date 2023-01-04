package com.etsuni.hubcore.events;

import com.etsuni.hubcore.HubCore;
import com.etsuni.hubcore.HubScoreboard;
import com.etsuni.hubcore.commands.CommandUtils;
import com.etsuni.hubcore.utils.DBUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JoinLeaveEvents implements Listener {

    private final HubCore plugin;

    public JoinLeaveEvents(HubCore instance) {
        plugin = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        if(plugin.getCfg().getString("spawn.location") != null) {
            player.teleport(CommandUtils.parseLocationString(plugin.getCfg().getString("spawn.location")));
        }

        if(player.hasPlayedBefore()) {
            for(String s : plugin.getMotdConfig().getStringList("motd")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        s.replace("%player%", player.getDisplayName())));
            }
        } else {
            DBUtils dbUtils = new DBUtils(plugin);
            if(dbUtils.addPlayerToDB(player)){
                dbUtils.addPlayersNewNameToDb(player, player.getDisplayName(), false);
                player.sendMessage(ChatColor.translateAlternateColorCodes(
                        '&', plugin.getCfg().getString("settings.first_join_msg")
                                .replace("%player%", player.getDisplayName())
                                .replace("%join_id%", String.valueOf(dbUtils.getPlayerDbId(player).getAsLong()))));
            }
            else {
                player.sendMessage(ChatColor.translateAlternateColorCodes(
                        '&', plugin.getCfg().getString("settings.first_join_msg")
                                .replace("%player%", player.getDisplayName())
                                .replace("%join_id%", "")));
            }
        }

        for(Player p : Bukkit.getOnlinePlayers()) {
            HubScoreboard hubScoreboard = new HubScoreboard(plugin);
            p.setScoreboard(hubScoreboard.hubScoreboard(p));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            HubScoreboard hubScoreboard = new HubScoreboard(plugin);
            p.setScoreboard(hubScoreboard.hubScoreboard(p));
        }

        event.setQuitMessage(null);
    }
}
