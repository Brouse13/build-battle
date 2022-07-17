package es.noobcraft.buildbattle.game.scheduler;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.GameScheduler;
import es.noobcraft.buildbattle.api.game.GameStatus;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import es.noobcraft.buildbattle.api.game.themes.Theme;
import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.buildbattle.api.scoreboard.ScoreboardManager;
import es.noobcraft.buildbattle.events.BuildGameStartEvent;
import es.noobcraft.buildbattle.events.BuildGameStopEvent;
import es.noobcraft.buildbattle.game.gui.ThemeGUI;
import es.noobcraft.buildbattle.game.inventories.GameInv;
import es.noobcraft.buildbattle.game.inventories.VoteInv;
import es.noobcraft.buildbattle.game.scoreboard.GameScoreboard;
import es.noobcraft.buildbattle.game.scoreboard.VoteScoreboard;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.player.NoobPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseGameScheduler implements GameScheduler {
    private final JavaPlugin plugin;
    private final BuildGame game;
    private int taskID;

    public BaseGameScheduler(JavaPlugin plugin, BuildGame game) {
        this.plugin = plugin;
        this.game = game;
        this.taskID = -1;
    }

    @Override
    public void start() {
        AtomicInteger countdown = new AtomicInteger(BuildBattleAPI.getArenaManager().getByServer(game.getServer()).getArenaSettings().getCountdown());

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            BuildArena arena = BuildBattleAPI.getArenaManager().getByServer(game.getServer());
            System.out.println(countdown+ " "+ arena.getStatus());

            ScoreboardManager.update();
            switch (arena.getStatus()) {
                case WAITING:
                    countdown.set(0);
                    break;
                case COUNTDOWN:
                    if (countdown.get() == 10) {
                        Logger.broadcast("build-battle.arena.messages.starting-in", game, 10);
                        sound();
                    }
                    if (countdown.get() == 5) {
                        Logger.broadcast("build-battle.arena.messages.starting-in", game, 5);
                        sound();
                    }
                    if (countdown.get() < 5 && countdown.get() > 0) {
                        Logger.broadcast("build-battle.arena.messages.starting-in", game, countdown.get());
                        sound();
                    }
                    if (countdown.get() == 0) {
                        Logger.broadcast("build-battle.arena.messages.starting-in", game, 0);
                        sound();

                        BuildGameStartEvent startEvent = new BuildGameStartEvent(game);
                        Bukkit.getServer().getPluginManager().callEvent(startEvent);

                        if (!startEvent.isCancelled()) arena.setStatus(GameStatus.THEME_VOTING);
                    }
                    countdown.getAndDecrement();
                    break;
                case THEME_VOTING:
                    if (countdown.get() == 0) {
                        game.setGameTheme(BuildBattleAPI.getThemeManager().createGameTheme());

                        for (BuildPlayer buildPlayer : game.getPlayers()) {
                            new ThemeGUI(game.getGameTheme(), Core.getPlayerCache().getPlayer(buildPlayer.getName())).openInventory();
                            buildPlayer.getPlayer().getInventory().clear();
                        }
                    }

                    if (countdown.get() == arena.getArenaSettings().getVoteTheme()) {
                        Theme winner = game.getGameTheme().getWinner();
                        arena.setStatus(GameStatus.INPROGRESS);

                        for (int i = 0; i < game.getPlayers().size(); i++) {
                            BuildPlayer buildPlayer = game.getPlayers().get(i);
                            Player player = buildPlayer.getPlayer();
                            NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(player.getName());

                            Logger.player(noobPlayer, "build-battle.arena.messages.tp-arena");
                            Logger.title(noobPlayer, "build-battle.arena.titles.winner-theme.title", "", winner.getTranslate(noobPlayer));
                            SpigotCore.getInventoryManager().removeInventory(noobPlayer);
                            player.getOpenInventory().close();
                            player.setGameMode(GameMode.CREATIVE);
                            player.getPlayer().getInventory().clear();
                            GameInv.giveInventory(buildPlayer);
                            ScoreboardManager.create(buildPlayer, new GameScoreboard(game, noobPlayer));
                            player.teleport(BuildBattleAPI.getRegionManager().getCenter(game.getBuildSpawns().get(i),
                                    arena.getArenaSettings()));
                            game.getBuildSpawns().get(i).addOwner(player);
                        }
                        countdown.set(0);
                    }
                    break;
                case INPROGRESS:
                    if (game.getTime() == 120) {
                        Logger.broadcast("build-battle.arena.messages.minutes-remaining", game, 2);
                        sound();
                    }
                    else if (game.getTime() == 60) {
                        Logger.broadcast("build-battle.arena.messages.minutes-remaining", game, 1);
                        sound();
                    }
                    else if (game.getTime() == 30) {
                        Logger.broadcast("build-battle.arena.messages.seconds-remaining", game, 30);
                        sound();
                    }
                    else if (game.getTime() == 10) {
                        Logger.broadcast("build-battle.arena.messages.seconds-remaining", game, 10);
                        sound();
                    }
                    else if (game.getTime() <= 5 && game.getTime() >= 0) {
                        Logger.broadcast("build-battle.arena.messages.seconds-remaining", game, game.getTime());
                        sound();
                    }

                    if (game.getTime() == 0) {
                        for (CuboidRegion region : game.getBuildSpawns()) region.setCanBuild(false);
                        arena.setStatus(GameStatus.BUILDING_VOTING);
                        game.setTime(1);
                        countdown.set(-1);
                    }
                    game.setTime(game.getTime()-1);
                    break;
                case BUILDING_VOTING:
                    if (countdown.get() == 0) {
                        for (BuildPlayer buildPlayer : game.getPlayers()) {
                            VoteInv.giveInventory(buildPlayer);
                            buildPlayer.getPlayer().setGameMode(GameMode.ADVENTURE);
                            buildPlayer.getPlayer().setAllowFlight(true);
                            buildPlayer.getPlayer().setFlying(true);
                        }
                    }

                    int currentRegionIndex = game.getBuildSpawns().indexOf(game.getCurrentRegion());
                    int nextVote = arena.getArenaSettings().getVoteConstruction() * (currentRegionIndex+1);

                    if (currentRegionIndex == game.getBuildSpawns().size()-1 && countdown.get() == nextVote) {
                        //Winner region
                        CuboidRegion winner = BuildBattleAPI.getGameManager().getWinner(game);

                        for (BuildPlayer buildPlayer : game.getPlayers()) {
                            NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(buildPlayer.getName());

                            buildPlayer.getPlayer().teleport(BuildBattleAPI.getRegionManager().getVoteTeleport(winner, arena.getArenaSettings()));
                            buildPlayer.getPlayer().getInventory().clear();

                            Logger.player(noobPlayer, "build-battle.arena.messages.winner-was", winner.getOwners().get(0).getName());
                            Logger.title(noobPlayer, "build-battle.arena.titles.winner-was.title",
                                    "", winner.getOwners().get(0).getName());

                            if (winner.getOwners().stream().anyMatch(target-> target.getName().equals(buildPlayer.getName()))) {
                                buildPlayer.setVictories(buildPlayer.getVictories()+ 1);
                                buildPlayer.setVictoriesStrike(buildPlayer.getVictoriesStrike()+ 1);
                            }else {
                                buildPlayer.setDefeats(buildPlayer.getDefeats()+1);
                                buildPlayer.setVictoriesStrike(0);
                            }
                            buildPlayer.setExperience(buildPlayer.getExperience()+
                                    BuildBattleAPI.getRegionManager().getPlayerRegion(game, buildPlayer.getPlayer()).getRegionVotes().getTotalScore());
                            buildPlayer.setGamesPlayed(buildPlayer.getGamesPlayed()+ 1);
                            try {
                                BuildBattleAPI.getDatabase().updatePlayer(buildPlayer);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        arena.setStatus(GameStatus.STOPPING);
                        countdown.set(0);
                    }

                    if((countdown.get() == nextVote && game.getCurrentRegion() != null) || currentRegionIndex == -1) {
                        if (currentRegionIndex == -1)
                            game.setCurrentRegion(game.getBuildSpawns().get(0));
                        else
                            game.setCurrentRegion(game.getBuildSpawns().get(currentRegionIndex+1));

                        CuboidRegion region = game.getCurrentRegion();

                        for (BuildPlayer buildPlayer : game.getPlayers()) {
                            ScoreboardManager.create(buildPlayer, new VoteScoreboard(game, region, Core.getPlayerCache().getPlayer(buildPlayer.getName())));

                            buildPlayer.getPlayer().setPlayerTime(region.getTime().getDuration(), false);
                            buildPlayer.getPlayer().setPlayerWeather(region.getWeather().toWeatherType());
                            buildPlayer.getPlayer().teleport(BuildBattleAPI.getRegionManager().getVoteTeleport(
                                    region, arena.getArenaSettings()));
                        }
                    }
                        //Next voted region
                    break;
                case STOPPING:
                    if (countdown.get() == arena.getArenaSettings().getStopTime()) {
                        for (BuildPlayer spectator : game.getSpectators()) spectator.returnPlayer();

                        for (BuildPlayer player : game.getPlayers())
                            SpigotCore.getScoreBoardManager().removeScoreBoard(Core.getPlayerCache().getPlayer(player.getName()));

                        BuildGameStopEvent stopEvent = new BuildGameStopEvent(game);
                        Bukkit.getServer().getPluginManager().callEvent(stopEvent);
                    }
                    break;
            }
            if (!arena.getStatus().equals(GameStatus.COUNTDOWN) || arena.getStatus().equals(GameStatus.BUILDING_VOTING))
                countdown.getAndIncrement();

        }, 0L,20L);
    }

    private void sound() {
        game.getPlayers().forEach(buildPlayer ->
                buildPlayer.getPlayer().playSound(buildPlayer.getPlayer().getLocation(), Sound.NOTE_PLING, 0.6f, 1f)
        );
    }

    @Override
    public void stop() {
        if (taskID != -1)
            Bukkit.getScheduler().cancelTask(taskID);
    }
}
