package com.etsuni.hubcore.events;

import com.etsuni.hubcore.HubCore;
import com.etsuni.hubcore.HubScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveEvents implements Listener {

    private final HubCore plugin;

    public JoinLeaveEvents(HubCore instance) {
        plugin = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        //TODO TP TO SPAWN
        //player.teleport()

        if(player.hasPlayedBefore()) {
            for(String s : plugin.getMotdConfig().getStringList("motd")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        s.replace("%player%", player.getDisplayName())));
            }
        } else {
            //ADD JOIN ID FROM MYSQL
            player.sendMessage("Hello " + player.getDisplayName() + " and welcome to us. (");
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
