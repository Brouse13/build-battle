package es.noobcraft.buildbattle.api.player;

import lombok.NonNull;

public interface PlayerCache {
    /**
     * Get a player from the cache,
     * if it doesn't exist it will return null
     * @param name player name
     * @return the player
     */
    BuildPlayer getPlayer(@NonNull String name);

    /**
     * Get if it exists the player into the cached players
     * @param name player name
     * @return if the player is cached
     */
    boolean existPlayer(@NonNull String name);

    /**
     * Load the player stats from the Database
     * and insert them into a BuildPlayer
     * @param player player to load
     * @return the BuildPlayer
     */
    BuildPlayer loadPlayer(BuildPlayer player);

    /**
     * Remove a player from the cached players
     * @param name player name
     */
    void removePlayer(@NonNull String name);
}
