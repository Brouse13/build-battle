package es.noobcraft.buildbattle.events;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.event.BuildGameEvent;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.player.NoobPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

public class BuildPlayerJoinEvent extends BuildGameEvent implements Cancellable {
    @Getter @Setter private boolean cancelled;
    @Getter private final BuildPlayer player;
    @Getter @Setter private String joinMessage;

    public BuildPlayerJoinEvent(BuildGame game, BuildPlayer player, NoobPlayer noobPlayer) {
        super(game);
        this.cancelled = false;
        this.player = player;
        BuildArena arena = BuildBattleAPI.getArenaManager().getByServer(game.getServer());
        this.joinMessage = Core.getTranslator().getLegacyText(noobPlayer,
                "build-battle.arena.messages.player-join",
                player.getName(),
                game.getPlayers().size(),
                arena.getArenaSettings().getMaxPlayers());
        }
}
