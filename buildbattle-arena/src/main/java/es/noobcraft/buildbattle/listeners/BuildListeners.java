package es.noobcraft.buildbattle.listeners;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import es.noobcraft.buildbattle.configuration.Yaml;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.player.NoobPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;
import java.util.stream.Collectors;

public class BuildListeners implements Listener {
    private final Yaml yml = new Yaml("config", false);
    List<Material> griffin = yml.getFile().getStringList("griffin").stream()
            .map(Material::valueOf).collect(Collectors.toList());

    @EventHandler(priority = EventPriority.HIGHEST)
    public void placeBlock(BlockPlaceEvent event) {
        BuildGame game = BuildBattleAPI.getGameManager().getGame();
        if (game == null) return;

        for (Material material : griffin) {
            if (event.getBlock().getType().equals(material)) {
                NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(event.getPlayer().getName());
                Logger.player(noobPlayer, "build-battle.arena.messages.cant-place");
                event.setCancelled(true);
            }
        }

        for (CuboidRegion region : game.getBuildSpawns()) {
            BuildArena buildArena = BuildBattleAPI.getArenaManager().getByServer(game.getServer());

            if (!region.canBuild(event.getPlayer())) continue;

            if (BuildBattleAPI.getRegionManager().containsBlock(region, event.getBlock().getLocation(),
                    buildArena.getArenaSettings())) {
                event.setCancelled(false);
                return;
            }
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void breakBlock(BlockBreakEvent event) {
        BuildGame game = BuildBattleAPI.getGameManager().getGame();
        if (game == null) return;

        for (Material material : griffin) {
            if (event.getBlock().getType().equals(material)) {
                NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(event.getPlayer().getName());
                Logger.player(noobPlayer, "build-battle.arena.messages.cant-break");
                event.setCancelled(true);
            }
        }
        for (CuboidRegion region : game.getBuildSpawns()) {
            BuildArena buildArena = BuildBattleAPI.getArenaManager().getByServer(game.getServer());

            if (!region.canBuild(event.getPlayer())) continue;
            if (BuildBattleAPI.getRegionManager().containsBlock(region, event.getBlock().getLocation(),
                    buildArena.getArenaSettings())) {
                event.setCancelled(false);
                return;
            }
        }
        event.setCancelled(true);
    }
}
