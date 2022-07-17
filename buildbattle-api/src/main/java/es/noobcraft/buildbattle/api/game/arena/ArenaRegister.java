package es.noobcraft.buildbattle.api.game.arena;

public interface ArenaRegister {
    /**
     * Register a new BuildArena into the redis client
     * @param server arena server
     */
    void register(String server);

    /**
     * Delete an existing arena from the redis client
     * @param server arena server
     */
    void delete(String server);

    /**
     * Update an arena on redis using the local BuildArenas
     * from BuildBattleAPI#getArenaManager()#getArenas()
     * @param server server name
     */
    void update(String server);

    /**
     * Get a RedisArena from redis with a
     * given name, it may return null if redis doesn't contain
     * the specific server
     * @param server server to get arena
     * @return the RedisArena from redis
     */
    RedisArena get(String server);

    /**
     * Get if there's an arena on redis
     * with the given server
     * @param server server name
     * @return if the arena exist
     */
    boolean existArena(String server);
}