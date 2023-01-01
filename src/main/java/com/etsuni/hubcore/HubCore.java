package com.etsuni.hubcore;

import com.etsuni.hubcore.events.KeepDayTime;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

public final class HubCore extends JavaPlugin {

    private File motdFile;
    private FileConfiguration motdConfig;

    public static HubCore plugin;

    @Override
    public void onEnable() {
        plugin = this;


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void createConfigs() {
        motdFile = new File(getDataFolder(), "motd.yml");
        if(!motdFile.exists()) {
            motdFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        motdConfig = new YamlConfiguration();

        try {
            motdConfig.load(motdFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public FileConfiguration getMotdConfig() {
        return this.motdConfig;
    }





    public void dayTime() {
        BukkitScheduler scheduler = getServer().getScheduler();

        BukkitTask keepDayTime = new KeepDayTime(this).runTaskTimer(this, 0, 100L);
    }
}
