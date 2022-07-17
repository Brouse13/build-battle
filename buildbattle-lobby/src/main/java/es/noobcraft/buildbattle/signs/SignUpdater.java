package es.noobcraft.buildbattle.signs;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.GameStatus;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.game.arena.RedisArena;
import es.noobcraft.core.api.Core;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static es.noobcraft.core.api.register.PropertyConstants.SERVER_PROPERTY_PLAYERS_ONLINE;

public class SignUpdater {
    public static void update(JavaPlugin plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (ArenaSign sign : ArenaSignManager.getSigns()) {
                String playerCount = Core.getServerRegistryManager().getProperties(sign.getServer())
                        .getProperty(SERVER_PROPERTY_PLAYERS_ONLINE);

                RedisArena redisArena = BuildBattleAPI.getArenaRegister().get(sign.getServer());
                if(redisArena == null) {
                    sign.getSign().setLine(2, "§cCLOSED");
                    sign.getSign().setLine(3, "0 / 0");
                    sign.getSign().update();
                    return;
                }

                BuildArena buildArena = BuildBattleAPI.getArenaManager().getByServer(redisArena.getServer());
                if (buildArena == null) return;

                sign.getSign().setLine(0, "§6BuildBattle");
                sign.getSign().setLine(1, "§9"+ buildArena.getName());
                sign.getSign().setLine(2, buildArena.getStatus() == GameStatus.WAITING ? "§aWAITING" : "§eIN PROGRESS");
                sign.getSign().setLine(3, "§2"+ Integer.parseInt(playerCount)+ "§7/§4"+ buildArena.getArenaSettings().getMaxPlayers());
                sign.getSign().update();
            }
        }, 0L, 20L);
    }
}
