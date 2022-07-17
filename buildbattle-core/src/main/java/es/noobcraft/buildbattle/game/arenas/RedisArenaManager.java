package es.noobcraft.buildbattle.game.arenas;

import com.avaje.ebeaninternal.server.lib.util.NotFoundException;
import com.google.common.collect.ImmutableSet;
import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.ServerType;
import es.noobcraft.buildbattle.api.game.GameStatus;
import es.noobcraft.buildbattle.api.game.arena.ArenaManager;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.game.arena.RedisArena;
import es.noobcraft.buildbattle.configuration.Yaml;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RedisArenaManager implements ArenaManager {
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Set<BuildArena> arenas = new HashSet<>();

    @Override
    public BuildArena createArena(String server) throws NotFoundException, IllegalAccessError {
        if (BuildBattleAPI.getServerType() == ServerType.LOBBY)
            throw new IllegalAccessError("You can't create arena on a lobby module");
        try {
            Yaml configuration = new Yaml("/arenas/"+ server+ "/arena", false);
            return new BuildArenaImpl(configuration.getFile().getString("arenas.name"),
                    server,
                    configuration.getFile().getBoolean("arenas.enabled"),
                    GameStatus.WAITING,
                    new Location(
                            Bukkit.getWorld(configuration.getFile().getString("arenas.spawn.world")),
                            configuration.getFile().getInt("arenas.spawn.x"),
                            configuration.getFile().getInt("arenas.spawn.y"),
                            configuration.getFile().getInt("arenas.spawn.z")),
                    new Location(
                            Bukkit.getWorld(configuration.getFile().getString("arenas.corner.world")),
                            configuration.getFile().getInt("arenas.corner.x"),
                            configuration.getFile().getInt("arenas.corner.y"),
                            configuration.getFile().getInt("arenas.corner.z")),
                    new BaseArenaSettings(configuration)
            );
        }catch (NotFoundException exception) {
            throw new NotFoundException("Server "+ server+ " does not contain arena.yml");
        }
    }

    @Override
    public BuildArena createArena(RedisArena redisArena) {
        if (BuildBattleAPI.getServerType().equals(ServerType.LOBBY))
            return new BuildArenaImpl(redisArena.getName(), redisArena.getServer(), redisArena.isEnabled(),
                  redisArena.getGameStatus(), null, null, new RedisArenaSettings(redisArena.getMaxPlayers()));
        else {
            BuildArena tempArena = createArena(redisArena.getServer());
            return new BuildArenaImpl(redisArena.getName(), redisArena.getServer(), redisArena.isEnabled(),
                    redisArena.getGameStatus(), tempArena.getArenaLobby(), tempArena.getCorner(), tempArena.getArenaSettings());
        }
    }

    @Override
    public BuildArena getByName(String name) {
        readWriteLock.readLock().lock();
        try {
            return arenas.stream().filter(arena ->  arena.getName().equals(name)).findFirst().orElse(null);
        }finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public BuildArena getByServer(String server) {
        readWriteLock.readLock().lock();
        try {
            return arenas.stream().filter(arena -> arena.getServer().equals(server)).findFirst().orElse(null);
        }finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public Set<BuildArena> getArenas() {
        readWriteLock.readLock().lock();
        try {
            return ImmutableSet.copyOf(arenas);
        }finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void addArena(BuildArena buildArena) {
        readWriteLock.writeLock().lock();
        try {
            arenas.add(buildArena);
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public void removeArena(BuildArena buildArena) {
        readWriteLock.writeLock().lock();
        try {
            arenas.remove(buildArena);
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void replaceArena(BuildArena arena, RedisArena redisArena) {
        readWriteLock.writeLock().lock();
        try {
            arena.setStatus(redisArena.getGameStatus());
            arena.setEnabled(redisArena.isEnabled());
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void clearArenas() {
        readWriteLock.writeLock().lock();
        try {
            arenas.clear();
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
