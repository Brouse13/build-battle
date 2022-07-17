package es.noobcraft.buildbattle.api.game.arena;

import es.noobcraft.buildbattle.api.game.GameStatus;
import org.bukkit.Location;

public interface BuildArena {
    /**
     * Get the name of the server where it will
     * be hosted the game with this arena
     * @return the arena server name
     */
    String getServer();

    /**
     * Get the unique name of the arena
     * it also represents the name of the
     * directory where the settings arena.
     *
     * .../arenas/{name}
     * @return the arena name
     */
    String getName();

    /**
     * Get the location of the lobby of this arena
     * @return lobby location
     */
    Location getArenaLobby();

    /**
     * Get the location of the arena corner
     * @return arena corner location
     */
    Location getCorner();

    /**
     * Get the GameStatus of the arena, the default value is WAITING
     * @return the arena GameStatus
     */
    GameStatus getStatus();

    /**
     * Set a new GameStatus to the arena
     * @param status new GameStatus
     */
    void setStatus(GameStatus status);

    /**
     * Set the Location of the lobby of this arena
     * @param location new Spawn
     */
    void setArenaLobby(Location location);

    /**
     * Get the all the settings of this arena loaded from the configuration
     * @return the arena settings
     */
    ArenaSettings getArenaSettings();

    /**
     * Enable or disable this arena
     * @param bool arena enabled
     */
    void setEnabled(boolean bool);

    /**
     * Get if this arena is enabled
     * @return if the arena is enabled
     */
    boolean isEnabled();
}
