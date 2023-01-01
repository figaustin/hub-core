package com.etsuni.hubcore.events;

import com.etsuni.hubcore.HubCore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;



public class KeepDayTime extends BukkitRunnable {

    HubCore plugin;

    public KeepDayTime(HubCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        //CHANGE TO CONFIG OPTION
        World world = Bukkit.getWorld("world");
        world.setTime(3000L);
    }
}
