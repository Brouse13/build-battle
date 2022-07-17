package es.noobcraft.buildbattle.api.game.arena;

import es.noobcraft.buildbattle.api.game.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class RedisArena {
    @Getter private final String name;
    @Getter private final String server;
    @Getter @Setter private boolean enabled;
    @Getter @Setter private GameStatus gameStatus;
    @Getter private final int maxPlayers;
    @Getter private final int onlinePlayers;
}