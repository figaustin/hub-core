package com.etsuni.hubcore.menus;

import com.etsuni.hubcore.HubCore;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;


public class MenuManager {

    private final HubCore plugin;

    public MenuManager(HubCore instance) {
        plugin = instance;
    }

    public void openGameSelector(Player player) {
        Configuration config = plugin.getMenusConfig();
        Inventory inv = Bukkit.createInventory(null, config.getInt("gameselector.size"),
                ChatColor.translateAlternateColorCodes('&',PlaceholderAPI.setPlaceholders(player, config.getString("gameselector.title"))));

        ConfigurationSection items = config.getConfigurationSection("gameselector.items");

        for(String item : items.getKeys(false)) {
            ItemStack itemStack = new ItemStack(Material.valueOf(items.getString(item + ".item")));
            ItemMeta meta;
            if(items.getBoolean(item + ".is_player_head")) {
                itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                meta = (SkullMeta) itemStack.getItemMeta();
                ((SkullMeta) meta).setOwningPlayer(player);
            } else {
                meta = itemStack.getItemMeta();
            }

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, items.getString(item + ".name"))));
            List<String> lore = new ArrayList<>();
            for(String str : items.getStringList(item + ".description")) {
                if(PlaceholderAPI.containsPlaceholders(str)) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, str)));
                } else {

                lore.add(ChatColor.translateAlternateColorCodes('&', str));
                }
            }
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            inv.setItem(items.getInt(item + ".position"), itemStack);
        }
        player.openInventory(inv);
    }

    public void openLobbySelector(Player player) {
        Configuration config = plugin.getMenusConfig();
        Inventory inv = Bukkit.createInventory(null, config.getInt("lobbyselector.size"),
                ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player,config.getString("lobbyselector.title"))));

        ConfigurationSection items = config.getConfigurationSection("lobbyselector.items");

        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(player.getName());
        for(String item : items.getKeys(false)) {
            ItemStack itemStack;
            if(proxiedPlayer.getServer().getInfo().getName().equalsIgnoreCase(ChatColor.stripColor(items.getString(item + ".name")))) {
                itemStack = new ItemStack(Material.valueOf(config.getString("lobbyselector.current_lobby_item")));
            } else {
                itemStack = new ItemStack(Material.valueOf(config.getString("lobbyselector.other_lobbies_item")));
            }
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, items.getString(item + ".name"))));
            List<String> lore = new ArrayList<>();
            for(String str : items.getStringList(item + ".description")) {
                if(PlaceholderAPI.containsPlaceholders(str)) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, str)));
                } else {
                    lore.add(ChatColor.translateAlternateColorCodes('&', str));
                }
            }

            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            inv.setItem(items.getInt(item + ".position"), itemStack);
        }
        player.openInventory(inv);
    }


}
