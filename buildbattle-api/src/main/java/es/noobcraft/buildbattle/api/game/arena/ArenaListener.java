package es.noobcraft.buildbattle.api.game.arena;

import es.noobcraft.core.api.redis.RedisSubscriber;
import org.bukkit.plugin.java.JavaPlugin;

public interface ArenaListener extends RedisSubscriber {
    /**
     * Update all the data to all the servers that are listening to
     * @param plugin plugin to register
     */
    void updateData(JavaPlugin plugin);
}
