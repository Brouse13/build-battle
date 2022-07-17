package es.noobcraft.buildbattle.game.arenas;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.GameStatus;
import es.noobcraft.buildbattle.api.game.arena.ArenaSettings;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

public class BuildArenaImpl implements BuildArena {
    @Getter
    private final String server;
    @Getter
    private final String name;
    @Getter @Setter
    private Location arenaLobby;
    @Getter private final Location corner;
    @Getter
    private GameStatus status;
    @Getter
    ArenaSettings arenaSettings;
    @Getter
    private boolean enabled;

    public BuildArenaImpl(String name, String server, boolean enabled, GameStatus gameStatus, Location arenaLobby, Location corner, ArenaSettings settings) {
        this.name = name;
        this.server = server;
        this.enabled = enabled;
        this.status = gameStatus;
        this.arenaLobby = arenaLobby;
        this.arenaSettings = settings;
        this.corner = corner;
    }

    @Override
    public void setStatus(GameStatus status) {
        this.status = status;
        BuildBattleAPI.getArenaRegister().update(server);
    }

    @Override
    public void setEnabled(boolean bool) {
        this.enabled = bool;
        BuildBattleAPI.getArenaRegister().update(server);
    }
}
