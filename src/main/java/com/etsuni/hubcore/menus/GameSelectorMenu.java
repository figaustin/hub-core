package com.etsuni.hubcore.menus;

import com.etsuni.hubcore.HubCore;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class GameSelectorMenu extends Menu {
    private final HubCore plugin;

    public GameSelectorMenu(PlayerMenuUtility playerMenuUtility, HubCore plugin) {
        super(playerMenuUtility, plugin);
        this.plugin = plugin;
    }

    @Override
    public String getMenuName() {
        return ChatColor.translateAlternateColorCodes('&',plugin.getMenusConfig().getString("gameselector.title"));
    }

    @Override
    public int getSlots() {
        return plugin.getMenusConfig().getInt("gameselector.size");
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Configuration config = plugin.getMenusConfig();
        int slot = event.getSlot();
        ConfigurationSection items = config.getConfigurationSection("gameselector.items");

        for (String item : items.getKeys(false)) {
            if (slot == items.getInt(item + ".position")) {
                player.chat(items.getString(item + ".command"));
                player.closeInventory();
            }
        }
    }

    @Override
    public void setMenuItems() {
        Configuration config = plugin.getMenusConfig();

        ConfigurationSection items = config.getConfigurationSection("gameselector.items");

        for (String item : items.getKeys(false)) {
            ItemStack itemStack = new ItemStack(Material.valueOf(items.getString(item + ".item")), 1, (short) items.getInt(item + ".id"));
            ItemMeta meta;
            if (items.getBoolean(item + ".is_player_head")) {
                itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                meta = (SkullMeta) itemStack.getItemMeta();
                ((SkullMeta) meta).setOwningPlayer(this.playerMenuUtility.getOwner());
            } else {
                meta = itemStack.getItemMeta();
            }

            if (PlaceholderAPI.containsPlaceholders(items.getString(item + ".name"))) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(this.playerMenuUtility.getOwner(), items.getString(item + ".name"))));
            } else {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', items.getString(item + ".name")));
            }

            List<String> lore = new ArrayList<>();
            for (String str : items.getStringList(item + ".description")) {
                if (PlaceholderAPI.containsPlaceholders(str)) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(this.playerMenuUtility.getOwner(), str)));
                } else {

                    lore.add(ChatColor.translateAlternateColorCodes('&', str));
                }
            }
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            inventory.setItem(items.getInt(item + ".position"), itemStack);
        }
    }
}
