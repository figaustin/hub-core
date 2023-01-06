package com.etsuni.hubcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class CommandUtils {

    public static Location parseLocationString(String cfgLoc) {
        String[] split = cfgLoc.split(":");
        return new Location(Bukkit.getWorld(split[0]), Float.parseFloat(split[1]),
                Float.parseFloat(split[2]), Float.parseFloat(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }

    public static String makeLocationString(Location location) {
        return location.getWorld().getName() + ":" + location.getX() + ":"
                + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }


}
