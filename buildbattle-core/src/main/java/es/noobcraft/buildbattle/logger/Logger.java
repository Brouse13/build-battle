package es.noobcraft.buildbattle.logger;

import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import lombok.NonNull;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Logger {
    private static final Translator translator = Core.getTranslator();
    private static JavaPlugin plugin;

    public Logger(JavaPlugin plugin) {
        Logger.plugin = plugin;
    }

    public static void log(@NonNull LoggerType  type, @NonNull String message) {
        switch (type) {
            case ERROR:
                plugin.getLogger().log(Level.SEVERE, message);
                break;
            case CONSOLE:
                plugin.getLogger().log(Level.INFO, message);
                break;
        }
    }

    public static void player(NoobPlayer player, String message, Object... replace) {
        player.sendMessage(new TextComponent(translator.getLegacyText(player, message, replace)));
    }

    public static void broadcast(String message, BuildGame game, Object... replace) {
        for (BuildPlayer player : game.getPlayers())
            player(Core.getPlayerCache().getPlayer(player.getPlayer().getName()), message, replace);
    }

    public static void title(NoobPlayer player, String title, String subtitle, Object... replace) {
        player.sendTitle(translator.getLegacyText(player, title, replace), subtitle);
    }
}


