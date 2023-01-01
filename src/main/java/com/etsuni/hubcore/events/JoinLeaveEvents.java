package com.etsuni.hubcore.events;

import com.etsuni.hubcore.HubCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveEvents implements Listener {

    private static final HubCore plugin = HubCore.plugin;

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


    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }
}
