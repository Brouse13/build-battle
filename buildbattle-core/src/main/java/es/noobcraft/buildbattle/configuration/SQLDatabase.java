package es.noobcraft.buildbattle.configuration;

import es.noobcraft.buildbattle.api.configuration.DataBase;
import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.buildbattle.game.player.BuildPlayerImpl;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import es.noobcraft.core.api.player.OfflineNoobPlayer;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLDatabase implements DataBase {
    private static final String EXIST_USER_SQL = "SELECT username FROM build_battle_user WHERE username=?";
    private static final String EXIST_STATS_SQL = "SELECT username FROM build_battle_statistics WHERE username=?";

    private static final String NEW_USER_SQL = "INSERT INTO build_battle_user (username, experience) VALUES (?, 0)";
    private static final String NEW_STATS_SQL = "INSERT INTO build_battle_statistics (username, defeats, victories, victories_strike, games_played) VALUES (?, 0, 0, 0, 0)";

    private static final String GET_USER_SQL = "SELECT * FROM build_battle_user WHERE username=?";
    private static final String GET_STATS_SQL = "SELECT * FROM build_battle_statistics WHERE username=?";

    private static final String UPDATE_USER_SQL = "UPDATE build_battle_user SET experience=? WHERE username=?";
    private static final String UPDATE_STATS_SQL = "UPDATE build_battle_statistics SET defeats=?, victories=?, victories_strike=?, games_played=? WHERE username=?";

    private static final String DELETE_USER_SQL = "DELETE FROM build_battle_user WHERE username=?";
    private static final String DELETE_STATS_SQL = "DELETE FROM build_battle_statistics WHERE username=?";

    @Override
    public boolean existPlayer(@NonNull OfflineNoobPlayer player) throws SQLException {
        final Connection connection = Core.getSQLClient().getConnection();

        try (PreparedStatement user = connection.prepareStatement(EXIST_USER_SQL);
                PreparedStatement stats = connection.prepareStatement(EXIST_STATS_SQL)) {
            user.setString(1, player.getUsername());
            stats.setString(1, player.getUsername());

            try(ResultSet userResult = user.executeQuery();
                ResultSet statsResult = stats.executeQuery()) {
                return userResult.next() && statsResult.next();
            }
        }
    }

    @Override
    public boolean createPlayer(OfflineNoobPlayer player) throws SQLException {
        final Connection connection = Core.getSQLClient().getConnection();
        if (existPlayer(player)) return false;

        try(PreparedStatement user = connection.prepareStatement(NEW_USER_SQL)) {
            user.setString(1, player.getUsername());
            user.execute();
        }catch (SQLException exception) {/*Ignore this exception*/}

        try(PreparedStatement statsStament = connection.prepareStatement(NEW_STATS_SQL)) {
            statsStament.setString(1, player.getUsername());
            statsStament.execute();
        }catch (SQLException exception) {/*Ignore this exception*/}

        return existPlayer(player);
    }

    @Override
    public BuildPlayer getPlayer(BukkitNoobPlayer player) throws SQLException {
        final Connection connection = Core.getSQLClient().getConnection();

        if (!existPlayer(player)) createPlayer(player);

        try(PreparedStatement userStament = connection.prepareStatement(GET_USER_SQL);
            PreparedStatement statsStament = connection.prepareStatement(GET_STATS_SQL)) {
            userStament.setString(1, player.getUsername());
            statsStament.setString(1, player.getUsername());

            try(ResultSet user = userStament.executeQuery();
                ResultSet stats = statsStament.executeQuery()) {
                user.next(); stats.next();

                return new BuildPlayerImpl(user.getString("username"),
                        user.getInt("experience"), stats.getInt("victories"),
                        stats.getInt("defeats"), stats.getInt("games_played"),
                        stats.getInt("victories_strike"));
            }
        }
    }


    @Override
    public boolean updatePlayer(BuildPlayer player) throws SQLException {
        final Connection connection = Core.getSQLClient().getConnection();

        try(PreparedStatement userStament = connection.prepareStatement(UPDATE_USER_SQL);
            PreparedStatement statsStament = connection.prepareStatement(UPDATE_STATS_SQL)) {

            userStament.setInt(1, player.getExperience());
            userStament.setString(2, player.getName());

            statsStament.setInt(1, player.getDefeats());
            statsStament.setInt(2, player.getVictories());
            statsStament.setInt(3, player.getVictoriesStrike());
            statsStament.setInt(4, player.getGamesPlayed());
            statsStament.setString(5, player.getName());

            userStament.executeUpdate();
            statsStament.executeUpdate();
            return true;
        }
    }

    @Override
    public boolean deletePlayer(OfflineNoobPlayer player) throws SQLException {
        final Connection connection = Core.getSQLClient().getConnection();
        if (!existPlayer(player)) return false;

        try (PreparedStatement userStament = connection.prepareStatement(DELETE_USER_SQL);
             PreparedStatement statsStament = connection.prepareStatement(DELETE_STATS_SQL)) {

            userStament.setString(1, player.getUsername());
            statsStament.setString(1, player.getUsername());

            userStament.execute();
            statsStament.execute();
            return true;
        }
    }
}
