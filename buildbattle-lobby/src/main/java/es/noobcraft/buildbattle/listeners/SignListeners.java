package es.noobcraft.buildbattle.listeners;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.GameStatus;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.game.arena.RedisArena;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.buildbattle.signs.ArenaSign;
import es.noobcraft.buildbattle.signs.ArenaSignManager;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.NoobPlayer;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListeners implements Listener {
    @EventHandler
    public void createSign(SignChangeEvent event) {
        if (!(event.getBlock().getState() instanceof Sign)) return;

        NoobPlayer player = Core.getPlayerCache().getPlayer(event.getPlayer().getName());
        String server = event.getLine(1)+ event.getLine(2)+ event.getLine(3);

        if (!event.getLine(0).equalsIgnoreCase("[BuildBattle]")) return;

        if (!player.hasGroup(Group.CO_CREATOR)) {
            Logger.player(player, "build-battle.lobby.sign.no-permission");
            event.setCancelled(true);
            return;
        }
        BuildArena arena = BuildBattleAPI.getArenaManager().getByServer(server);

        if (arena == null) {
            Logger.player(player, "build-battle.lobby.sign.arena-not-found", server);
            event.setCancelled(true);
            return;
        }

        ArenaSignManager.addSign(new ArenaSign(arena.getServer(), event.getBlock().getLocation()));
        Logger.player(player, "build-battle.lobby.sign.sign-created");
        event.setLine(0, "§6BuildBattle");
        event.setLine(1, "§9"+ arena.getName());
        event.setLine(2, arena.getStatus() == GameStatus.WAITING ? "§aWAITING" : "§eIN PROGRESS");
        event.setLine(3, "§20§7/§4"+ arena.getArenaSettings().getMaxPlayers());
    }

    @EventHandler
    public void removeSign(BlockBreakEvent event) {
        if (!(event.getBlock().getState() instanceof Sign)) return;
        if (!event.getPlayer().isSneaking()) return;

        NoobPlayer player = Core.getPlayerCache().getPlayer(event.getPlayer().getName());

        ArenaSign arenaSign = ArenaSignManager.getSign(event.getBlock().getLocation());
        if (arenaSign == null) return;

        if (!player.hasGroup(Group.CO_CREATOR)) {
            event.setCancelled(true);
            return;
        }

        ArenaSignManager.removeSign(arenaSign);
        Logger.player(player, "build-battle.lobby.sign.sign-removed");
    }

    @EventHandler
    public void signClick(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (!(event.getClickedBlock().getState() instanceof Sign)) return;
        if(event.getPlayer().isSneaking()) return;

        NoobPlayer player = Core.getPlayerCache().getPlayer(event.getPlayer().getName());

        ArenaSign arenaSign = ArenaSignManager.getSign(event.getClickedBlock().getLocation());
        if (arenaSign == null) return;

        RedisArena redisArena = BuildBattleAPI.getArenaRegister().get(arenaSign.getServer());
        if (redisArena == null) {
            Logger.player(player, "build-battle.lobby.sign.server-closed");
            return;
        }

        if (!redisArena.isEnabled() && !player.hasGroup(Group.CO_CREATOR)) {
            Logger.player(player, "build-battle.lobby.sign.arena-disabled");
            return;
        }

        if (redisArena.getOnlinePlayers() < redisArena.getMaxPlayers() && redisArena.getGameStatus() == GameStatus.WAITING) {
            Logger.player(player, "build-battle.lobby.sign.tp-arena");
            player.connect(redisArena.getServer());
            return;
        }
        Logger.player(player, "build-battle.lobby.sign.game-full");
    }
}
