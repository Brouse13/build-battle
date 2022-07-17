package es.noobcraft.buildbattle.events;

import es.noobcraft.buildbattle.api.event.BuildGameEvent;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.player.BuildPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

import java.util.List;

public class BuildGameStartEvent extends BuildGameEvent implements Cancellable {
    @Getter @Setter
    private boolean cancelled = false;

    public BuildGameStartEvent(BuildGame game) {
        super(game);
    }
    public List<BuildPlayer> getPlayers() {
        return getGame().getPlayers();
    }
}
