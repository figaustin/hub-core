package com.etsuni.hubcore.utils;

import com.etsuni.hubcore.HubCore;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.OptionalLong;

public class DBUtils {
    private final HubCore plugin;

    public DBUtils(HubCore instance) {
        plugin = instance;
    }

    public Boolean addPlayerToDB(Player player) {
        try(Connection conn = plugin.getDataSource().getConnection(); PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO players(uuid) VALUES(?);"
        )) {
            statement.setString(1, player.getUniqueId().toString());
            statement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public OptionalLong getPlayerDbId(Player player) {
        try(Connection conn = plugin.getDataSource().getConnection(); PreparedStatement statement = conn.prepareStatement(
                "SELECT id FROM players WHERE uuid = ?;"
        )) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return OptionalLong.of(resultSet.getLong("id"));
            }
            return OptionalLong.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return OptionalLong.empty();
        }
    }
}
