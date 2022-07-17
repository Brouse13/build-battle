package es.noobcraft.buildbattle.api.game.themes;

import es.noobcraft.core.api.player.NoobPlayer;

public interface Theme {
    /**
     * Get the unique identifier of this theme.
     * It's used to get the translation and to
     * identify the theme
     * @return the theme identifier
     */
    String getIdentifier();

    /**
     * Gets the translation from the theme.
     * It depends on the player language
     * @param player player to get the translation from
     * @return the theme translation
     */
    String getTranslate(NoobPlayer player);
}
