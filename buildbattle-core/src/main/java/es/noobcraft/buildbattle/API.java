package es.noobcraft.buildbattle;

import com.google.common.base.Preconditions;
import es.noobcraft.buildbattle.api.BuildBattle;
import es.noobcraft.buildbattle.api.ServerType;
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
import es.noobcraft.buildbattle.configuration.SQLDatabase;
import es.noobcraft.buildbattle.game.MapGameManager;
import es.noobcraft.buildbattle.game.arenas.RedisArenaListener;
import es.noobcraft.buildbattle.game.arenas.RedisArenaManager;
import es.noobcraft.buildbattle.game.arenas.RedisArenaRegister;
import es.noobcraft.buildbattle.game.arenas.RedisLobbyListener;
import es.noobcraft.buildbattle.game.arenas.region.RegionFactory;
import es.noobcraft.buildbattle.game.gui.theme.ThemeFactory;
import es.noobcraft.buildbattle.game.player.SetPlayerCache;
import es.noobcraft.buildbattle.ranks.RankFactory;
import es.noobcraft.buildbattle.top.SqlTopManager;

public class API implements BuildBattle {
    ServerType serverType;
    MapGameManager gameFactory = new MapGameManager();
    ArenaManager arenaManager = new RedisArenaManager();
    ArenaListener arenaListener;
    RegionManager regionManager = new RegionFactory();
    PlayerCache playerCache = new SetPlayerCache();
    ThemeManager themeManager = new ThemeFactory();
    RankManager rankFactory = new RankFactory();
    TopManager topManager = new SqlTopManager();
    DataBase dataBase = new SQLDatabase();
    ArenaRegister arenaRegister = new RedisArenaRegister();

    public API(ServerType serverType) {
        Preconditions.checkNotNull(serverType, "serverType");
        this.serverType = serverType;
    }

    @Override
    public ServerType getServerType() {
        return serverType;
    }

    @Override
    public GameManager getGameManager() {
        return gameFactory;
    }

    @Override
    public ArenaListener getArenaListener() {
        if (arenaListener == null)
            arenaListener = serverType == ServerType.ARENA ? new RedisArenaListener() : new RedisLobbyListener();
            return arenaListener;
    }

    @Override
    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    @Override
    public RegionManager getRegionManager() {
        return regionManager;
    }

    @Override
    public PlayerCache getPlayerCache() {
        return playerCache;
    }

    @Override
    public ThemeManager getThemeManager() {
        return themeManager;
    }

    @Override
    public RankManager getRankManager() {
        return rankFactory;
    }

    @Override
    public TopManager getTopManager() {
        return topManager;
    }

    @Override
    public DataBase getDatabase() {
        return dataBase;
    }

    @Override
    public ArenaRegister getArenaRegister() {
        return arenaRegister;
    }
}
