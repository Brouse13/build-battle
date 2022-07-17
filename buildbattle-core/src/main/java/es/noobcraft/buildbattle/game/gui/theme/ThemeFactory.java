package es.noobcraft.buildbattle.game.gui.theme;

import es.noobcraft.buildbattle.api.game.themes.GameTheme;
import es.noobcraft.buildbattle.api.game.themes.Theme;
import es.noobcraft.buildbattle.api.game.themes.ThemeManager;
import es.noobcraft.buildbattle.configuration.Yaml;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.buildbattle.logger.LoggerType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThemeFactory implements ThemeManager {
    @Getter
    List<Theme> themes = new ArrayList<>();

    @Override
    public void loadThemes() {
        Yaml yml = new Yaml("config", false);
        List<String> identifiers = new ArrayList<>();
        if (!yml.getFile().contains("themes")) {
            Logger.log(LoggerType.CONSOLE, "No themes were found");
            return;
        }
        for (String identifier : yml.getFile().getStringList("themes")) {
            if(!identifiers.contains(identifier.toUpperCase())) {
                themes.add(new BuildTheme(identifier.toUpperCase()));
                identifiers.add(identifier.toUpperCase());
            }else
                Logger.log(LoggerType.CONSOLE, "Duplicated key "+ identifier.toUpperCase());
        }
        Logger.log(LoggerType.CONSOLE, "Loaded "+ themes.size()+ " themes");
    }

    @Override
    public GameTheme createGameTheme() {
        Random random = new Random();
        List<Theme> themes = new ArrayList<>();

        while (themes.size() < 6) {
            Theme theme = this.themes.get(random.nextInt(this.themes.size()));

            if (!themes.contains(theme)) {
                themes.add(theme);
            }
        }
        return new BaseGameTheme(themes);
    }

    @Override
    public Theme getTheme(String identifier) {
        for (Theme theme : themes) {
            if (theme.getIdentifier().equals(identifier.toUpperCase()))
                return theme;
        }
        return null;
    }
}
