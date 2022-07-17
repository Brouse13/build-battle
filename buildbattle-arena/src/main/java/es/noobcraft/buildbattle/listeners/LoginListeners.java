package es.noobcraft.buildbattle.listeners;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.GameStatus;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.buildbattle.events.BuildGameStopEvent;
import es.noobcraft.buildbattle.events.BuildPlayerJoinEvent;
import es.noobcraft.buildbattle.events.BuildPlayerLeaveEvent;
import es.noobcraft.buildbattle.events.SpectatorJoinEvent;
import es.noobcraft.buildbattle.game.scheduler.BaseGameScheduler;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.buildbattle.logger.LoggerType;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.event.NoobPlayerJoinEvent;
import es.noobcraft.core.api.event.NoobPlayerLoginEvent;
import es.noobcraft.core.api.event.NoobPlayerQuitEvent;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.NoobPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class LoginListeners implements Listener {
    private final JavaPlugin plugin;

    public LoginListeners(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void preLogin(NoobPlayerLoginEvent event) {
        if (!event.getResult().equals(PlayerLoginEvent.Result.ALLOWED)) return;

        BuildGame game = BuildBattleAPI.getGameManager().createGame(Core.getServerId());

        if (game == null) {
            Logger.log(LoggerType.ERROR, "Not found arena on server "+ Core.getServerId());
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Arena error");
            return;
        }

        if (game.getScheduler() == null) game.setScheduler(new BaseGameScheduler(plugin, game));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void noobPlayerLogin(NoobPlayerLoginEvent event) throws SQLException {
        if (!event.getResult().equals(PlayerLoginEvent.Result.ALLOWED)) return;

        if(!BuildBattleAPI.getPlayerCache().existPlayer(event.getPlayer().getName())) {
            BuildBattleAPI.getDatabase().createPlayer(event.getPlayer());
            BuildBattleAPI.getPlayerCache().loadPlayer(BuildBattleAPI.getDatabase().getPlayer(event.getPlayer()));
        }
    }

    @EventHandler
    public void playerJoin(NoobPlayerJoinEvent event) {
        NoobPlayer noobPlayer = event.getNoobPlayer();
        BuildPlayer buildPlayer = BuildBattleAPI.getPlayerCache().getPlayer(noobPlayer.getCaseInsensitiveName());
        BuildGame game = BuildBattleAPI.getGameManager().createGame(Core.getServerId());
        BuildArena arena = BuildBattleAPI.getArenaManager().getByServer(Core.getServerId());

        //Insert a new player
        if (arena.isEnabled()) {
            if (game.isFull()) {
                SpectatorJoinEvent spectatorJoinEvent = new SpectatorJoinEvent(game, buildPlayer);
                Bukkit.getServer().getPluginManager().callEvent(spectatorJoinEvent);
                if (spectatorJoinEvent.isCancelled()) {
                    game.removeSpectator(buildPlayer);
                    return;
                }
                Logger.player(noobPlayer, spectatorJoinEvent.getJoinMessage());
                return;
            }

            game.addPlayer(buildPlayer);
            BuildPlayerJoinEvent joinEvent = new BuildPlayerJoinEvent(game, buildPlayer, noobPlayer);
            Bukkit.getServer().getPluginManager().callEvent(joinEvent);
            if (joinEvent.isCancelled()) {
                game.removePlayer(buildPlayer);
                return;
            }
            Logger.broadcast(joinEvent.getJoinMessage(), game);
            return;
        }

        if (noobPlayer.getSetGroups().contains(Group.CO_CREATOR))
            Logger.player(noobPlayer, "build-battle.arena.messages.admin-enable-disabled");
    }

    @EventHandler
    public void playerLeave(NoobPlayerQuitEvent event) {
        BuildPlayer buildPlayer = BuildBattleAPI.getPlayerCache().getPlayer(event.getNoobPlayer().getCaseInsensitiveName());
        BuildGame game = BuildBattleAPI.getGameManager().getGame();
        BuildArena arena = BuildBattleAPI.getArenaManager().getByServer(Core.getServerId());
        if (game == null) return;

        if(game.getSpectators().contains(buildPlayer))
            game.removeSpectator(buildPlayer);
        else {
            BuildPlayerLeaveEvent leaveEvent = new BuildPlayerLeaveEvent(game, buildPlayer, event.getNoobPlayer());
            Bukkit.getServer().getPluginManager().callEvent(leaveEvent);
            Logger.broadcast(leaveEvent.getLeaveMessage(), game);
        }

        if (arena.getStatus() == GameStatus.COUNTDOWN) {
            Logger.broadcast("build-battle.arena.messages.countdown-stopped", game);
            arena.setStatus(GameStatus.WAITING);
            game.getScheduler().stop();
        }

        if (game.getPlayers().size() == 0)
            for (BuildPlayer spectator : game.getSpectators()) spectator.returnPlayer();

        if (game.getPlayers().size() == 0 &&  game.getSpectators().size() == 0) {
            BuildGameStopEvent stopEvent = new BuildGameStopEvent(game);
            Bukkit.getServer().getPluginManager().callEvent(stopEvent);
        }
    }
}
