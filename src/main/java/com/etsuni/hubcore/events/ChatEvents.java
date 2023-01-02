package com.etsuni.hubcore.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;


public class ChatEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        for(Player player : event.getRecipients()) {
            if(player != null && event.getMessage().contains(player.getDisplayName())) {
                event.setCancelled(true);
                messageWithMention(event.getRecipients(), event.getPlayer(), player, event.getFormat(), event.getMessage());
            }
        }

    }

    public void messageWithMention(Set<Player> players, Player sender, Player target, String format, String message) {

        for(Player player : players) {
            if(player == target) {
                //TODO ADD SOUND?
                player.sendMessage(
                        String.format(format, sender.getDisplayName(),message.replace(
                                target.getDisplayName(),
                                ChatColor.YELLOW + target.getDisplayName()) + ChatColor.RESET));
            } else {
                player.sendMessage(String.format(format, sender.getDisplayName(), message));
            }
        }
    }
}
