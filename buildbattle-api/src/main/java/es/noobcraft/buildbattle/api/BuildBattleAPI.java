package es.noobcraft.buildbattle.api;

import es.noobcraft.buildbattle.api.configuration.DataBase;
import es.noobcraft.buildbattle.api.game.GameManager;
import es.noobcraft.buildbattle.api.game.arena.ArenaListener;
import es.noobcraft.buildbattle.api.game.arena.ArenaManager;
import es.noobcraft.buildbattle.api.game.arena.ArenaRegister;
import es.noobcraft.buildbattle.api.game.arena.region.RegionManager;
import es.noobcraft.buildbattle.api.game.rank.RankManager;
import es.noobcraft.buildbattle.api.game.themes.ThemeManager;
import es.noobcraft.buildbattle.api.player.PlayerCache;
import es.noobcraft.buildbattle.api.top.TopManager;
import lombok.NonNull;

public class BuildBattleAPI {
    private static BuildBattle buildBattle;

    //Private constructor to se a Singleton
    private BuildBattleAPI() {
    }

    /**
     * Get the correct ServerType instance
     * @return the ServerType
     */
    public static ServerType getServerType() {
        return buildBattle.getServerType();
    }

    /**
     * Get the api singleton
     * @return the Api singleton
     */
    public static BuildBattle getBuildBattle() {
        return buildBattle;
    }

    /**
     * Set the api singleton
     * @param buildBattle buildBattle api interface
     */
    public static void setBuildBattle(final @NonNull BuildBattle buildBattle) {
        if (BuildBattleAPI.buildBattle != null)
            throw new IllegalStateException("Cannot redefine singleton");
        BuildBattleAPI.buildBattle = buildBattle;
    }

    /**
     * Get the GameManager instance
     * @return the GameManager
     */
    public static GameManager getGameManager() {
        return buildBattle.getGameManager();
    }

    /**
     * Get the ArenaRegister instance
     * @return the ArenaRegister
     */
    public static ArenaRegister getArenaRegister() {
        return buildBattle.getArenaRegister();
    }

    /**
     * Get the ArenaListener instance
     * @return the ArenaListener
     */
    public static ArenaListener getArenaListener() {
        return buildBattle.getArenaListener();
    }

    /**
     * Get the ArenaManager instance
     * @return the ArenaManager
     */
    public static ArenaManager getArenaManager() {
        return buildBattle.getArenaManager();
    }

    /**
     * Get the RegionManager instance
     * @return the RegionManager
     */
    public static RegionManager getRegionManager() {
        return buildBattle.getRegionManager();
    }

    /**
     * Get the RegionManager instance
     * @return the PlayerCache
     */
    public static PlayerCache getPlayerCache() {
        return buildBattle.getPlayerCache();
    }

    /**
     * Get the RankManager instance
     * @return the RankManager
     */
    public static RankManager getRankManager() {
        return buildBattle.getRankManager();
    }

    /**
     * Get the TopManager instance
     * @return the TopManager
     */
    public static TopManager getTopManager() {
        return buildBattle.getTopManager();
    }

    /**
     * Get the ThemeManager instance
     * @return the ThemeManager
     */
    public static ThemeManager getThemeManager() {
        return buildBattle.getThemeManager();
    }


    /**
     * Get the DataBase instance
     * @return the Database
     */
    public static DataBase getDatabase() {
        return buildBattle.getDatabase();
    }
}
