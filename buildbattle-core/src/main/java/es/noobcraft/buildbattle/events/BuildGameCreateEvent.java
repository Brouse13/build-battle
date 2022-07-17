package es.noobcraft.buildbattle.events;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.event.BuildGameEvent;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;
import org.bukkit.event.Cancellable;

public class BuildGameCreateEvent extends BuildGameEvent implements Cancellable {
    @Getter @Setter
    private boolean cancelled;
    @Getter
    private final BuildArena arena;

    public BuildGameCreateEvent(BuildGame game) {
        super(game);
        this.arena = BuildBattleAPI.getArenaManager().getByServer(game.getServer());
        this.cancelled = false;
    }

    public World getSpawnWorld() {
        return arena.getArenaLobby().getWorld();
    }
}
