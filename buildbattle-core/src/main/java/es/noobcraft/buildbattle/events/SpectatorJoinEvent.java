package es.noobcraft.buildbattle.events;

import es.noobcraft.buildbattle.api.event.BuildGameEvent;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.player.BuildPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

public class SpectatorJoinEvent extends BuildGameEvent implements Cancellable {
    @Getter @Setter private boolean cancelled;
    @Getter private final BuildPlayer player;
    @Getter @Setter private String joinMessage;

    public SpectatorJoinEvent(BuildGame game, BuildPlayer player) {
        super(game);
        this.cancelled = false;
        this.player = player;
        this.joinMessage = "build-battle.arena.spectator-leave-msg";
    }
}
