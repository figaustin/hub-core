package com.etsuni.hubcore.events;

import com.etsuni.hubcore.HubCore;
import com.etsuni.hubcore.HubScoreboard;
import com.etsuni.hubcore.commands.CommandUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.event.EventPriority;
import com.etsuni.hubcore.utils.DBUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class JoinLeaveEvents implements Listener {

    private final HubCore plugin;

    public JoinLeaveEvents(HubCore instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();

        if(plugin.getCfg().getString("spawn.location") != null) {
            player.teleport(CommandUtils.parseLocationString(plugin.getCfg().getString("spawn.location")));
        }

        if(plugin.getCfg().getString("spawn.location") != null) {
            player.teleport(CommandUtils.parseLocationString(plugin.getCfg().getString("spawn.location")));
        }

        for(String s : plugin.getMotdConfig().getStringList("motd")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, s)));
        }
        
        if(player.hasPlayedBefore()) {

            DBUtils dbUtils = new DBUtils(plugin);
            if(!dbUtils.checkIfSameName(player)) {
                dbUtils.addPlayersNewNameToDb(player, player.getName(), false);
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

        PlayerInventory inv = player.getInventory();
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lGame &7Selector"));
        item.setItemMeta(meta);
        inv.setItem(3, item);

        item = new ItemStack(Material.FEATHER);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&e&lLobby &7Selector"));
        item.setItemMeta(meta);
        inv.setItem(8, item);
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
