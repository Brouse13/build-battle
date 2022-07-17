package es.noobcraft.buildbattle.game.player;

import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.buildbattle.api.player.PlayerCache;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

public class SetPlayerCache implements PlayerCache {
    private final Set<BuildPlayer> playerCache = new HashSet<>();

    @Override
    public BuildPlayer getPlayer(@NonNull String name) {
        return playerCache.stream().filter(player -> player.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public boolean existPlayer(@NonNull String name) {
        return getPlayer(name) != null;
    }

    @Override
    public BuildPlayer loadPlayer(BuildPlayer player) {
        if (!existPlayer(player.getName()))
            playerCache.add(player);
        return getPlayer(player.getName());
    }

    @Override
    public void removePlayer(@NonNull String name) {
        playerCache.removeIf(player -> existPlayer(player.getName()));
    }
}
