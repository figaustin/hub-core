package com.etsuni.hubcore.menus.listeners;

import com.etsuni.hubcore.HubCore;
import com.etsuni.hubcore.menus.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {

    private final HubCore plugin;

    public MenuListener(HubCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        InventoryHolder holder = event.getClickedInventory().getHolder();

        if(holder instanceof Menu) {

            event.setCancelled(true);

            if(event.getCurrentItem() == null) {return;}

            Menu menu = (Menu) holder;

            menu.handleMenu(event);
        }
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        Player player = (Player) event.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();

        if(!mainHand.hasItemMeta()) {
            return;
        }
        if(!mainHand.getItemMeta().hasDisplayName()){
            return;
        }

        if(mainHand.getItemMeta().getDisplayName().contains("Game") && mainHand.getType().equals(Material.COMPASS)) {

            GameSelectorMenu menu = new GameSelectorMenu(plugin.getPlayerMenuUtility(player), plugin);
            menu.open();
        }
        else if(mainHand.getItemMeta().getDisplayName().contains("Lobby") && mainHand.getType().equals(Material.FEATHER)) {
            LobbySelectorMenu menu = new LobbySelectorMenu(plugin.getPlayerMenuUtility(player), plugin);
            menu.open();
        }

    }
}
