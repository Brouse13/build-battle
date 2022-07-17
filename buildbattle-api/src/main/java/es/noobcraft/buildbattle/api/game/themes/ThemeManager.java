package es.noobcraft.buildbattle.api.game.themes;

import java.util.List;

public interface ThemeManager {
    /**
     * Load all the available themes and
     * translations from the DataBase
     */
    void loadThemes();

    /**
     * Get all the available themes
     * that have been loaded
     * @return all the available themes
     */
    List<Theme> getThemes();

    /**
     * Create a new GameTheme to set it
     * on a BuildGame
     * @return a new GameTheme
     */
    GameTheme createGameTheme();

    /**
     * Get a theme with a given identifier
     * @param identifier theme identifier
     * @return the theme with the identifier
     */
    Theme getTheme(String identifier);

}
