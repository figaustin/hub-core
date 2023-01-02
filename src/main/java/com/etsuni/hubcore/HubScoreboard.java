package com.etsuni.hubcore;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.List;

public class HubScoreboard {

    private final HubCore plugin;

    public HubScoreboard(HubCore instance) {
        plugin = instance;
    }

    public Scoreboard hubScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Configuration config = plugin.getScoreboardConfig();

        Objective obj = scoreboard.registerNewObjective("Hub", "dummy");
        obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, config.getString("scoreboard.title"))));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<String> lines = config.getStringList("scoreboard.lines");
        //Iterate backwards over list
        int current = lines.size() - 1;

        for(String line : lines) {
            Score score = obj.getScore(ChatColor.translateAlternateColorCodes('&',PlaceholderAPI.setPlaceholders(player, line)));
            score.setScore(current);
            current--;
        }

        return scoreboard;

    }
}
