package es.noobcraft.buildbattle.game.arenas.region;

import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.arena.ArenaSettings;
import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import es.noobcraft.buildbattle.api.game.arena.region.RegionManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class RegionFactory implements RegionManager {
    @Override
    public List<CuboidRegion> createRegions(Location location, ArenaSettings settings) {
        List<CuboidRegion> regions = new ArrayList<>();
        Vector origin = location.toVector();
        for (int i = 0; i < settings.getMaxPlayers(); i++) {
            Vector bound1 = origin.clone();
            Vector bound2 = bound1.clone().add(
                    new Vector(settings.getArenaSize()+ settings.getBorderSize()-1,36,
                            settings.getArenaSize()+ settings.getBorderSize()-1));
            regions.add(new BaseCuboidRegion(location.getWorld(), bound1, bound2, settings));

            origin = bound1.clone().add(new Vector(
                    settings.getArenaSize()+settings.getBorderSize(),0,0));
        }
        return regions;
    }

    @Override
    public CuboidRegion getPlayerRegion(BuildGame game, Player player) {
        return game.getBuildSpawns().stream()
                .filter(region -> region.getOwners().stream()
                        .anyMatch(p -> p.getUniqueId().equals(player.getUniqueId())))
                .findFirst().orElse(null);
    }

    @Override
    public Location getCenter(CuboidRegion region, ArenaSettings settings) {
        int half = (settings.getArenaSize() + settings.getBorderSize()) / 2;
        return region.getMin().clone().add(new Vector(half, 2, half)).toLocation(region.getWorld());
    }

    @Override
    public Location getVoteTeleport(CuboidRegion region, ArenaSettings settings) {
        return region.getMin().clone().add(
                new Vector((settings.getArenaSize() + settings.getBorderSize()) / 2, 10, settings.getBorderSize()))
                .toLocation(region.getWorld());
    }

    @Override
    public boolean containsBlock(CuboidRegion region, Location location, ArenaSettings settings) {
        int substact = settings.getBorderSize()/2;

        return location.toVector().isInAABB(
                region.getMin().clone().add(new Vector(substact,2, substact)),
                region.getMax().clone().subtract(new Vector(substact,0, substact)));
    }
}
