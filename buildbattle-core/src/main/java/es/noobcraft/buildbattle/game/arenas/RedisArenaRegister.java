package es.noobcraft.buildbattle.game.arenas;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.GameStatus;
import es.noobcraft.buildbattle.api.game.arena.ArenaRegister;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.game.arena.RedisArena;
import es.noobcraft.core.api.Core;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static es.noobcraft.core.api.register.PropertyConstants.SERVER_PROPERTY_PLAYERS_ONLINE;

public class RedisArenaRegister implements ArenaRegister {
    private BuildArena registeredArena;

    @Override
    public void register(String server) {
        if(registeredArena != null) return;

        try(Jedis jedis  = Core.getRedisClient().getJedis()) {
            BuildArena buildArena = BuildBattleAPI.getArenaManager().createArena(server);

            registeredArena = buildArena;

            jedis.lpush("build-battle:online-arenas", server);
            jedis.hset("build-battle:arenas:"+ server, redisHash(buildArena));
            jedis.publish("build-battle", server);
        }
    }

    @Override
    public void delete(String server) {
        if (!existArena(server)) return;

        try(Jedis jedis  = Core.getRedisClient().getJedis()) {
            //We remove the arena from memory and from redis
            jedis.del("build-battle:arenas:"+ server);
            jedis.lrem("build-battle:online-arenas",1, server);
            //We update the status value to DELETING and delete it from cache
            registeredArena = null;
            //We publish the changes
            jedis.publish("build-battle", server);
        }
    }

    @Override
    public void update(String server) {
        if (!existArena(server)) return;

        try(Jedis jedis  = Core.getRedisClient().getJedis()) {
            RedisArena redisArena = get(server);//We get the redisArena from redis
            BuildArena buildArena = BuildBattleAPI.getArenaManager().getByServer(redisArena.getServer());//We get the registered arena from the cache

            final RedisArena arena = createArena(buildArena);
            if (redisArena.getGameStatus()  == arena.getGameStatus() && redisArena.isEnabled()  == arena.isEnabled()) return; //Data is equals not update

            jedis.del("build-battle:arenas:"+ server);//Delete from redis
            jedis.hset("build-battle:arenas:"+ server, redisHash(buildArena));//Set into redis
            jedis.publish("build-battle", server);//Publish changes
        }
    }

    @Override
    public RedisArena get(String server) {
        if (!existArena(server)) return null;

        try(Jedis jedis  = Core.getRedisClient().getJedis()) {
            Map<String, String> redisHash = jedis.hgetAll("build-battle:arenas:" + server);

            return new RedisArena(redisHash.get("name"),
                redisHash.get("server"),
                redisHash.get("enabled") != "0",
                GameStatus.valueOf(redisHash.get("status")),
                Integer.parseInt(redisHash.get("maxPlayers")),
                Integer.parseInt(redisHash.get("onlinePlayers")));
        }
    }

    @Override
    public boolean existArena(String server) {
        try(Jedis jedis  = Core.getRedisClient().getJedis()) {
            return jedis.hexists("build-battle:arenas:"+ server, "name");
        }
    }

    private Map<String, String> redisHash(BuildArena arena) {
        HashMap<String, String> map = new HashMap<>();
        Properties server = Core.getServerRegistryManager().getProperties(Core.getServerId());
        map.put("name", arena.getName());
        map.put("server", arena.getServer());
        map.put("enabled", arena.isEnabled() ? "1" : "0");
        map.put("status", arena.getStatus().toString());
        map.put("onlinePlayers", server.getProperty(SERVER_PROPERTY_PLAYERS_ONLINE));
        map.put("maxPlayers", arena.getArenaSettings().getMaxPlayers()+ "");
        return map;
    }

    private RedisArena createArena(BuildArena arena) {
        Properties server = Core.getServerRegistryManager().getProperties(Core.getServerId());
        return new RedisArena(arena.getName(),
                arena.getServer(),
                arena.isEnabled(),
                arena.getStatus(),
                arena.getArenaSettings().getMaxPlayers(),
                Integer.parseInt(server.getProperty(SERVER_PROPERTY_PLAYERS_ONLINE)));
    }
}
