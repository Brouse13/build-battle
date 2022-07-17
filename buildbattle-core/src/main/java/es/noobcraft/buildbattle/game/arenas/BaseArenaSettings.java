package es.noobcraft.buildbattle.game.arenas;

import es.noobcraft.buildbattle.api.game.arena.ArenaSettings;
import es.noobcraft.buildbattle.configuration.Yaml;
import lombok.Getter;

public class BaseArenaSettings implements ArenaSettings {
    @Getter
    private final int maxPlayers, countdown, voteTheme, gameDuration, voteConstruction, stopTime;
    @Getter
    private final int borderSize, arenaSize;

    public BaseArenaSettings(Yaml yml) {
        this.maxPlayers = yml.getFile().getInt("arenas.settings.max_players");
        this.countdown = yml.getFile().getInt("arenas.settings.countdown");
        this.voteTheme = yml.getFile().getInt("arenas.settings.vote_theme");
        this.gameDuration = yml.getFile().getInt("arenas.settings.game_duration");
        this.voteConstruction = yml.getFile().getInt("arenas.settings.vote_construction");
        this.stopTime = yml.getFile().getInt("arenas.settings.stop_time");
        this.arenaSize = yml.getFile().getInt("arenas.settings.plot.size");
        this.borderSize = yml.getFile().getInt("arenas.settings.plot.border");
    }
}
