package es.noobcraft.buildbattle.api.configuration;

import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import es.noobcraft.core.api.player.OfflineNoobPlayer;

import java.sql.SQLException;

public interface DataBase {
    /**
     * Check if a player exists in the DataBase
     * @param player player to check
     * @return if exist the player
     * @throws SQLException throw exception if was an error on the query
     */
    boolean existPlayer(OfflineNoobPlayer player) throws SQLException;

    /**
     * Create a new player in the DataBase
     * @param player player to create
     * @return if the operation was executed successfully
     * @throws SQLException throw exception if was an error on the query
     */
    boolean createPlayer(OfflineNoobPlayer player) throws SQLException;

    /**
     * Get a player from the DataBase
     * @param player player to get
     * @return if the operation was executed successfully
     * @throws SQLException throw exception if was an error on the query
     */
    BuildPlayer getPlayer(BukkitNoobPlayer player) throws SQLException;

    /**
     * Update a player in the DataBase
     * @param player player to update
     * @return if the operation was executed successfully
     * @throws SQLException throw exception if was an error on the query
     */
    boolean updatePlayer(BuildPlayer player) throws SQLException;

    /**
     * Delete a player from the DataBase
     * @param player player to delete
     * @return if the operation was executed successfully
     * @throws SQLException throw exception if was an error on the query
     */
    boolean deletePlayer(OfflineNoobPlayer player) throws SQLException;
}
