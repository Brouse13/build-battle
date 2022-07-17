package es.noobcraft.buildbattle.game.arenas;

import es.noobcraft.buildbattle.api.game.arena.ArenaSettings;
import lombok.Getter;

public class RedisArenaSettings implements ArenaSettings {
    @Getter
    private final int maxPlayers;

    public RedisArenaSettings(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Override
    public int getCountdown() {
        return 0;
    }

    @Override
    public int getVoteTheme() {
        return 0;
    }

    @Override
    public int getGameDuration() {
        return 0;
    }

    @Override
    public int getVoteConstruction() {
        return 0;
    }

    @Override
    public int getStopTime() {
        return 0;
    }

    @Override
    public int getArenaSize() {
        return 0;
    }

    @Override
    public int getBorderSize() {
        return 0;
    }
}
