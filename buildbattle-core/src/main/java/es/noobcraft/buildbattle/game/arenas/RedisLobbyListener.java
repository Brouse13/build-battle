package es.noobcraft.buildbattle.game.arenas;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.arena.ArenaListener;
import es.noobcraft.buildbattle.api.game.arena.ArenaManager;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.game.arena.RedisArena;
import es.noobcraft.core.api.Core;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisLobbyListener implements ArenaListener {
    private int taskID = -1;
    private final ArenaManager arenaManager = BuildBattleAPI.getArenaManager();

    @Override
    public void onMessage(@NonNull String server) {
        //Is in cache
        if (arenaManager.getArenas().stream().map(BuildArena::getServer).anyMatch(server::equals)) {
            //Is in redis
            if (BuildBattleAPI.getArenaRegister().existArena(server)) {
                //Update
                RedisArena redisArena = BuildBattleAPI.getArenaRegister().get(server);
                BuildArena buildArena = arenaManager.getByServer(server);

                arenaManager.removeArena(buildArena);
                arenaManager.addArena(arenaManager.createArena(redisArena));
                return;
            }
            //Delete
            arenaManager.removeArena(arenaManager.getByServer(server));
        }else {
            //Create
            arenaManager.addArena(arenaManager.createArena(BuildBattleAPI.getArenaRegister().get(server)));
        }
    }

    public void updateData(JavaPlugin plugin) {
        if (taskID != -1) return;

        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {
            try(Jedis jedis = Core.getRedisClient().getJedis()) {
                List<String> keys = jedis.lrange("build-battle:online-arenas", 0, -1);
                if (keys.isEmpty()) return;

                BuildBattleAPI.getArenaManager().clearArenas();

                for (String server : keys) {
                    RedisArena redisArena = BuildBattleAPI.getArenaRegister().get(server);
                    BuildBattleAPI.getArenaManager().addArena(BuildBattleAPI.getArenaManager().createArena(redisArena));
                }
            }
        }, 0L, 20 * 10);
    }
}
