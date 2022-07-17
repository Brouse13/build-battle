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

public interface BuildBattle {
    /**
     * Get the ServerType from the  BuildBattle module
     * it can't be null
     * @return server type
     */
    ServerType getServerType();

    /**
     * Get the GameManager that is used to manage
     * and create all the BuildGames. It's used
     * to store game vars and schedule the game
     * @return the BuildBattle GameManager
     */
    GameManager getGameManager();

    /**
     * Get the ArenaRegister that is used to
     * register the arenas into all the
     * lobbies
     * @return the ArenaManager
     */
    ArenaRegister getArenaRegister();

    /**
     * Get the ArenaListener that get all
     * the data from servers and is used to
     * load it into the lobbies
     * @return the ArenaListener
     */
    ArenaListener getArenaListener();

    /**
     * Get the ArenaManager that is used to manage
     * and create BuildArenas. It's used to store
     * and load the arena settings
     * @return the BuildBattle ArenaManager
     */
    ArenaManager getArenaManager();

    /**
     * Get the RegionManager that is used to manage
     * and create CuboidRegions. It's represents the
     * region where a player/team can build
     * @return the BuildBattle RegionManager
     */
    RegionManager getRegionManager();

    /**
     * Get the PlayerCache that is used to manage
     * and create BuildPlayers. It stores the player
     * BuildBattle stats.
     * @return the BuildBattle PlayerCache
     *
     */
    PlayerCache getPlayerCache();

    /**
     * Get the RankManager that is used to store
     * and manage BuildBattle ranks
     * @return the BuildBattle RankManager
     */
    RankManager getRankManager();

    /**
     * Get the TopManager that is used to
     * manage the BuildBattle top players
     * @return the BuildBattle TopManager
     */
    TopManager getTopManager();

    /**
     * Get the ThemeManager that is used to store
     * and manage game themes
     * @return the BuildBattle ThemeManager
     */
    ThemeManager getThemeManager();

    /**
     * Get the Database that represents a connection
     * where you can do CRUD operations
     * @return the BuildBattle Database
     */
    DataBase getDatabase();
}
