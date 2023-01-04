package com.etsuni.hubcore.menus;

import com.etsuni.hubcore.HubCore;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class MenuEvent implements Listener {

    private final HubCore plugin;

    public MenuEvent(HubCore instance) {
        plugin = instance;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Configuration config = plugin.getMenusConfig();
        int slot = event.getSlot();
        if(event.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', config.getString("gameselector.title")))) {

            ConfigurationSection items = config.getConfigurationSection("gameselector.items");

            for(String item : items.getKeys(false)) {
                if(slot == items.getInt(item + ".position")) {
                    player.performCommand(items.getString(item + ".command"));
                }
            }
        }
        else if(event.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', config.getString("lobbyselector.title")))) {

            ConfigurationSection items = config.getConfigurationSection("lobbyselector.items");

            for (String item : items.getKeys(false)) {
                if (slot == items.getInt(item + ".position")) {
                    player.performCommand(items.getString(item + ".command"));
                }
            }
        }
    }
}
