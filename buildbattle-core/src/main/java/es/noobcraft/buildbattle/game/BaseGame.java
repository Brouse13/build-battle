package es.noobcraft.buildbattle.game;

import com.google.common.collect.ImmutableList;
import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.GameScheduler;
import es.noobcraft.buildbattle.api.game.GameStatus;
import es.noobcraft.buildbattle.api.game.arena.ArenaSettings;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import es.noobcraft.buildbattle.api.game.themes.GameTheme;
import es.noobcraft.buildbattle.api.player.BuildPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class BaseGame implements BuildGame {
    @Getter private final String server;
    @Getter @Setter private GameStatus status;
    @Getter @Setter private GameTheme gameTheme;
    @Getter @Setter private GameScheduler scheduler;
    private final List<BuildPlayer> players = new ArrayList<>();
    private final List<BuildPlayer> spectators = new ArrayList<>();
    @Getter @Setter private int time;
    @Getter @Setter private CuboidRegion currentRegion;
    @Getter private final List<CuboidRegion> buildSpawns;

    private final boolean teamMode = false;
    private final ArenaSettings settings;

    public BaseGame(BuildArena arena) {
        this.server = arena.getServer();
        this.settings = arena.getArenaSettings();
        this.time = settings.getGameDuration();
        this.gameTheme = BuildBattleAPI.getThemeManager().createGameTheme();
        this.buildSpawns = BuildBattleAPI.getRegionManager().createRegions(arena.getCorner(), settings);
    }

    @Override
    public boolean teamMode() {
        return teamMode;
    }

    @Override
    public List<BuildPlayer> getSpectators() {
        return ImmutableList.copyOf(spectators);
    }

    @Override
    public void addSpectator(BuildPlayer player) {
        spectators.add(player);
    }

    @Override
    public void removeSpectator(BuildPlayer player) {
        spectators.remove(player);
    }

    @Override
    public List<BuildPlayer> getPlayers() {
        return ImmutableList.copyOf(players);
    }

    @Override
    public void addPlayer(BuildPlayer player) {
        players.add(player);
    }

    @Override
    public void removePlayer(BuildPlayer player) {
        players.remove(player);
    }

    public boolean isFull() {
        return this.players.size() == settings.getMaxPlayers();
    }
}
