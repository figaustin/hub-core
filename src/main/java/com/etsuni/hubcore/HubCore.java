package com.etsuni.hubcore;

import com.etsuni.hubcore.commands.*;
import com.etsuni.hubcore.events.CancelledEvents;
import com.etsuni.hubcore.events.ChatEvents;
import com.etsuni.hubcore.events.JoinLeaveEvents;
import com.etsuni.hubcore.events.KeepDayTime;
import com.etsuni.hubcore.menus.MenuEvent;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class HubCore extends JavaPlugin {

    private File motdFile;
    private FileConfiguration motdConfig;
    private File warpsFile;
    private FileConfiguration warpsConfig;
    private File scoreboardFile;
    private FileConfiguration scoreboardConfig;
    private File configFile;
    private FileConfiguration config;
    private File menusFile;
    private FileConfiguration menusConfig;

    private final UtilityCommands utilityCommands = new UtilityCommands();
    private final WarpCommands warpCommands = new WarpCommands(this);
    private final SpawnCommands spawnCommands = new SpawnCommands(this);
    private final GamemodeCommands gamemodeCommands = new GamemodeCommands();
    private final NameCommands nameCommands = new NameCommands(this);

    public static HubCore plugin;

    private DataSource dataSource;

    @Override
    public void onEnable() {
        plugin = this;
        createConfigs();

        this.getCommand("fly").setExecutor(utilityCommands);
        this.getCommand("speed").setExecutor(utilityCommands);
        this.getCommand("skull").setExecutor(utilityCommands);
        this.getCommand("setwarp").setExecutor(warpCommands);
        this.getCommand("delwarp").setExecutor(warpCommands);
        this.getCommand("warp").setExecutor(warpCommands);
        this.getCommand("warps").setExecutor(warpCommands);
        this.getCommand("setspawn").setExecutor(spawnCommands);
        this.getCommand("spawn").setExecutor(spawnCommands);
        this.getCommand("rename").setExecutor(nameCommands);
        this.getCommand("namehistory").setExecutor(nameCommands);
        this.getCommand("gmc").setExecutor(gamemodeCommands);
        this.getCommand("gms").setExecutor(gamemodeCommands);
        this.getCommand("gma").setExecutor(gamemodeCommands);
        this.getCommand("gmsp").setExecutor(gamemodeCommands);
        this.getCommand("gameselector").setExecutor(nameCommands);
        //SERVER RESTART COMMAND


        this.getServer().getPluginManager().registerEvents(new ChatEvents(), this);
        this.getServer().getPluginManager().registerEvents(new CancelledEvents(), this);
        this.getServer().getPluginManager().registerEvents(new JoinLeaveEvents(this), this);
        this.getServer().getPluginManager().registerEvents(new MenuEvent(this), this);

        dayTime();

        try {
            dataSource = initMySQLDataSource();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            initDb();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {

    }

    private DataSource initMySQLDataSource() throws SQLException {
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName(config.getString("database.host"));
        dataSource.setPortNumber(config.getInt("database.port"));
        dataSource.setDatabaseName(config.getString("database.database"));
        dataSource.setUser(config.getString("database.user"));
        dataSource.setPassword(config.getString("database.password"));

        testDataSource(dataSource);
        return dataSource;
    }

    private void testDataSource(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            if (!conn.isValid(1)) {
                throw new SQLException("Could not establish database connection.");
            }
        }
    }

    private void initDb() throws SQLException, IOException {
        String setup;
        try(InputStream in = getClassLoader().getResourceAsStream("dbsetup.sql")) {
            setup = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not read db setup file");
            throw e;
        }
        String[] queries = setup.split(";");
        // execute each query to the database.
        for (String query : queries) {
            // If you use the legacy way you have to check for empty queries here.
            if (query.isEmpty()) continue;
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.execute();
            }
        }
        getLogger().info("Database setup complete");
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    private void createConfigs() {
        configFile = new File(getDataFolder(), "config.yml");
        if(!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        config = new YamlConfiguration();

        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

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

        menusFile = new File(getDataFolder(), "menus.yml");
        if(!menusFile.exists()) {
            menusFile.getParentFile().mkdirs();
            saveResource("menus.yml", false);
        }

        menusConfig = new YamlConfiguration();

        try {
            menusConfig.load(menusFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }



    }

    public FileConfiguration getCfg(){
        return this.config;
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

    public FileConfiguration getMenusConfig() {
        return this.menusConfig;
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
