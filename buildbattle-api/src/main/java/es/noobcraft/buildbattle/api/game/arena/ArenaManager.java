package es.noobcraft.buildbattle.api.game.arena;

import com.avaje.ebeaninternal.server.lib.util.NotFoundException;

import java.util.Set;

/**
 * All the methods are thread safety
 **/
public interface ArenaManager {
    /**
     * Create a new arena on the given server.
     * This method will load all the data from /arenas/{server}
     * Calling the method from a lobby module will throw IllegalAccessError
     *
     * @param server server name
     * @throws NotFoundException if the server hasn't data on /arenas/{server}
     * @throws IllegalAccessError if it's called from a Lobby module
     * @return a new BuildArena
     */
    BuildArena createArena(String server) throws NotFoundException, IllegalAccessError;

    /**
     * Create a new Arena from a RedisArena.
     * if it exists an arena on the RedisArena server
     * it will complete the arena with it details
     * @param redisArena RedisArena used to create the BuildArena
     * @return a new BuildArena
     */
    BuildArena createArena(RedisArena redisArena);

    /**
     * Get all the loaded arenas on all the
     * servers
     * @return the loaded arenas
     */
    Set<BuildArena> getArenas();

    /**
     * Get an arena by its name
     * @param name arena name
     * @return the arena
     */
    BuildArena getByName(String name);

    /**
     * Get an arena by its server
     * @param server arena server
     * @return the arena
     */
    BuildArena getByServer(String server);

    /**
     * Add a new arena to the list of loaded arenas
     * @param buildArena BuildArena to add
     */
    void addArena(BuildArena buildArena);

    /**
     * Remove an arena to the list of loaded arenas
     * @param buildArena BuildArena to remove
     */
    void removeArena(BuildArena buildArena);

    /**
     * Replace an arena to the list of loaded arenas
     * @param arena arena to update
     * @param redisArena redisArena from where update
     */
    void replaceArena(BuildArena arena, RedisArena redisArena);

    /**
     * Clear all the loaded arenas
     */
    void clearArenas();
}