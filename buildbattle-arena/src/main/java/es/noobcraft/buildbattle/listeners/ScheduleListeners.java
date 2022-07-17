package es.noobcraft.buildbattle.listeners;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.GameStatus;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.buildbattle.api.scoreboard.ScoreboardManager;
import es.noobcraft.buildbattle.events.*;
import es.noobcraft.buildbattle.game.gui.particles.ParticlesScheduler;
import es.noobcraft.buildbattle.game.inventories.LobbyInv;
import es.noobcraft.buildbattle.game.scoreboard.LobbyScoreboard;
import es.noobcraft.buildbattle.game.scoreboard.StaffScoreboard;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.buildbattle.logger.LoggerType;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.player.NoobPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ScheduleListeners implements Listener {
    private final JavaPlugin plugin;

    public ScheduleListeners(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBuildGameCreate(BuildGameCreateEvent event) {
        event.getSpawnWorld().setDifficulty(Difficulty.PEACEFUL);
        event.getSpawnWorld().setGameRuleValue("doMobLoot", "false");
        event.getSpawnWorld().setGameRuleValue("doMobSpawning", "false");
        event.getSpawnWorld().setGameRuleValue("announceAdvancements", "false");
        event.getSpawnWorld().setGameRuleValue("doDaylightCycle", "false");
        event.getSpawnWorld().setGameRuleValue("doWeatherCycle", "false");
        Logger.log(LoggerType.CONSOLE, "Game has successfully created");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBuildGameStop(BuildGameStopEvent event) {
        for (BuildPlayer buildPlayer : event.getGame().getPlayers()) buildPlayer.returnPlayer();
        for (BuildPlayer buildPlayer : event.getGame().getSpectators()) buildPlayer.returnPlayer();

            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                Logger.log(LoggerType.CONSOLE, "Restarting game...");
                Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("Lobby not found"));

                ScoreboardManager.clear();
                ParticlesScheduler.stop();

                BuildBattleAPI.getArenaRegister().delete(Core.getServerId());
                BuildBattleAPI.getArenaRegister().register(Core.getServerId());

                event.getGame().getScheduler().stop();

                BuildBattleAPI.getGameManager().deleteGame();
            }, 100L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBuildPlayerJoin(BuildPlayerJoinEvent event) {
        BuildArena buildArena = BuildBattleAPI.getArenaManager().getByServer(event.getGame().getServer());
        NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(event.getPlayer().getName());

        //Set player the join options
        Player player = Bukkit.getPlayer(event.getPlayer().getName());
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.teleport(buildArena.getArenaLobby());
        LobbyInv.giveInventory(event.getPlayer());

        ScoreboardManager.create(event.getPlayer(), new LobbyScoreboard(event.getGame(), noobPlayer));

        Logger.title(noobPlayer, "build-battle.arena.messages.welcome-title", event.getPlayer().getName());

        if (event.getGame().isFull()) {
            buildArena.setStatus(GameStatus.COUNTDOWN);
            event.getGame().getScheduler().start();
        }
    }

    @EventHandler
    public void onSpectatorJoin(SpectatorJoinEvent event) {
        for (BuildPlayer player : event.getGame().getPlayers()) player.getPlayer().hidePlayer(event.getPlayer().getPlayer());

        event.getPlayer().getPlayer().setGameMode(GameMode.SPECTATOR);
        event.getGame().addSpectator(event.getPlayer());
        ScoreboardManager.create(event.getPlayer(), new StaffScoreboard(event.getGame(), Core.getPlayerCache().getPlayer(event.getPlayer().getName())));
    }

    @EventHandler(ignoreCancelled = true)
    public void onBuildPlayerLeave(BuildPlayerLeaveEvent event) {
        event.getGame().removePlayer(event.getPlayer());
        ScoreboardManager.remove(event.getPlayer());

        if (event.getGame().getPlayers().size() == 0)
            event.getGame().getScheduler().stop();
    }
}
