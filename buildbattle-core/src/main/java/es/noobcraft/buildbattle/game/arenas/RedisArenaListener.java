package es.noobcraft.buildbattle.game.arenas;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.arena.ArenaListener;
import es.noobcraft.buildbattle.api.game.arena.ArenaManager;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.game.arena.RedisArena;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

public class RedisArenaListener implements ArenaListener {
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

                System.out.println(redisArena.getGameStatus().name());
                arenaManager.replaceArena(arenaManager.getByServer(redisArena.getServer()), redisArena);
                return;
            }
            //Delete
            arenaManager.removeArena(arenaManager.getByServer(server));
        }else {
            //Create
            arenaManager.addArena(arenaManager.createArena(server));
        }
    }

    @Override
    public void updateData(JavaPlugin plugin) {
        if (taskID != -1) return;

        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {
            if (BuildBattleAPI.getArenaManager().getArenas().size() == 0) return;
            arenaManager.getArenas().forEach(arena -> BuildBattleAPI.getArenaRegister().update(arena.getServer()));
        }, 0L, 20);
    }
}
