package es.noobcraft.buildbattle.game.gui.theme;

import es.noobcraft.buildbattle.api.game.themes.Theme;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.player.NoobPlayer;
import lombok.Getter;

public class BuildTheme implements Theme {
    @Getter
    String identifier;

    public BuildTheme(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getTranslate(NoobPlayer player) {
        return Core.getTranslator().getLegacyText(player, "build-battle.arena.theme."+ identifier.toLowerCase());
    }
}
