package com.etsuni.hubcore;

import com.etsuni.hubcore.commands.UtilityCommands;
import com.etsuni.hubcore.commands.WarpCommands;
import com.etsuni.hubcore.events.CancelledEvents;
import com.etsuni.hubcore.events.ChatEvents;
import com.etsuni.hubcore.events.JoinLeaveEvents;
import com.etsuni.hubcore.events.KeepDayTime;
import org.bukkit.Bukkit;
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
    private File warpsFile;
    private FileConfiguration warpsConfig;
    private File scoreboardFile;
    private FileConfiguration scoreboardConfig;

    private final UtilityCommands utilityCommands = new UtilityCommands();

    public static HubCore plugin;

    @Override
    public void onEnable() {
        plugin = this;
        createConfigs();

        this.getCommand("fly").setExecutor(utilityCommands);
        this.getCommand("speed").setExecutor(utilityCommands);
        this.getCommand("skull").setExecutor(utilityCommands);
        this.getCommand("setwarp").setExecutor(new WarpCommands(this));
        this.getCommand("delwarp").setExecutor(new WarpCommands(this));
        this.getCommand("warp").setExecutor(new WarpCommands(this));
        this.getCommand("warps").setExecutor(new WarpCommands(this));

        this.getServer().getPluginManager().registerEvents(new ChatEvents(), this);
        this.getServer().getPluginManager().registerEvents(new CancelledEvents(), this);
        this.getServer().getPluginManager().registerEvents(new JoinLeaveEvents(this), this);

        dayTime();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void createConfigs() {
        motdFile = new File(getDataFolder(), "motd.yml");
        if(!motdFile.exists()) {
            motdFile.getParentFile().mkdirs();
            saveResource("motd.yml", false);
        }

        motdConfig = new YamlConfiguration();

        try {
            motdConfig.load(motdFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        warpsFile = new File(getDataFolder(), "warps.yml");
        if(!warpsFile.exists()) {
            warpsFile.getParentFile().mkdirs();
            saveResource("warps.yml", false);
        }

        warpsConfig = new YamlConfiguration();

        try {
            warpsConfig.load(warpsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        scoreboardFile = new File(getDataFolder(), "scoreboard.yml");
        if(!scoreboardFile.exists()) {
            scoreboardFile.getParentFile().mkdirs();
            saveResource("scoreboard.yml", false);
        }

        scoreboardConfig = new YamlConfiguration();

        try {
            scoreboardConfig.load(scoreboardFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }



    }

    public FileConfiguration getMotdConfig() {
        return this.motdConfig;
    }

    public FileConfiguration getWarpsConfig() {
        return this.warpsConfig;
    }

    public FileConfiguration getScoreboardConfig() {
        return this.scoreboardConfig;
    }


    public void saveCfgs() {
        try {
            motdConfig.save(motdFile);
            warpsConfig.save(warpsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void dayTime() {
        BukkitTask keepDayTime = new KeepDayTime(this).runTaskTimer(this, 0, 100L);
    }
}
