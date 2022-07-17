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

public class BuildPlayerLeaveEvent extends BuildGameEvent {
    @Getter
    private final BuildPlayer player;
    @Getter @Setter
    private String leaveMessage;

    public BuildPlayerLeaveEvent(BuildGame game, BuildPlayer player, NoobPlayer noobPlayer) {
        super(game);
        this.player = player;
        BuildArena arena = BuildBattleAPI.getArenaManager().getByServer(game.getServer());
        this.leaveMessage = Core.getTranslator().getLegacyText(noobPlayer,
                "build-battle.arena.messages.player-leave",
                player.getName(),
                game.getPlayers().size(),
                arena.getArenaSettings().getMaxPlayers());
    }
}
