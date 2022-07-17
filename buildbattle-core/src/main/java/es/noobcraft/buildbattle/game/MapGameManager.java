package es.noobcraft.buildbattle.game;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.GameManager;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import es.noobcraft.buildbattle.events.BuildGameCreateEvent;
import es.noobcraft.core.api.Core;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

public class MapGameManager implements GameManager {
    @Getter
    private BuildGame game = null;

    @SneakyThrows
    @Override
    public BuildGame createGame(String server) {
       if (game != null && game.getServer().equals(Core.getServerId())) return game;

        final BuildArena buildArena = BuildBattleAPI.getArenaManager().getArenas().stream()
                .filter(arena -> arena.getServer().equals(Core.getServerId()))
                .findAny().orElseThrow(() -> new Exception("No arena available on server " + Core.getServerId()));

       game = new BaseGame(buildArena);

        BuildBattleAPI.getArenaRegister().register(game.getServer());
        BuildGameCreateEvent createGameEvent = new BuildGameCreateEvent(game);
        Bukkit.getServer().getPluginManager().callEvent(createGameEvent);

        if (createGameEvent.isCancelled()) game = null;

        return game;
    }

    @Override
    public void deleteGame() {
        this.game = null;
    }

    @Override
    public CuboidRegion getWinner(BuildGame game) {
        CuboidRegion winner = game.getBuildSpawns().get(0);
        for (CuboidRegion region : game.getBuildSpawns()) {
            if (region.getRegionVotes().getTotalScore() > winner.getRegionVotes().getTotalScore())
                winner = region;
        }
        return winner;
    }
}
