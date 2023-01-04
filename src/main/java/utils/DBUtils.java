package utils;

import com.etsuni.hubcore.HubCore;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public Boolean addPlayersNewNameToDb(Player player, String name, Boolean nickname) {
        try(Connection conn = plugin.getDataSource().getConnection(); PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO names(name,is_nick_name,created_at,player_id) VALUES(?,?,?,?);"
        )) {
            statement.setString(1, name);
            statement.setBoolean(2, nickname);
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(4, getPlayerDbId(player).getAsLong());
            statement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<List<PlayerName>> getPlayerNameHistory(Player player) {
        try(Connection conn = plugin.getDataSource().getConnection(); PreparedStatement statement = conn.prepareStatement(
                "SELECT * FROM names WHERE player_id = ? ORDER BY id DESC"
        )) {
            statement.setLong(1, getPlayerDbId(player).getAsLong());
            ResultSet resultSet = statement.executeQuery();
            List<PlayerName> names = new ArrayList<>();
            while(resultSet.next()) {
                names.add(
                        new PlayerName(resultSet.getString("name"),
                                resultSet.getBoolean("is_nick_name"),
                                resultSet.getTimestamp("created_at"))
                );
            }
            return Optional.of(names);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

}
