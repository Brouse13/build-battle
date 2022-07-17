package es.noobcraft.buildbattle.api.game.arena.region;

import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.arena.ArenaSettings;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public interface RegionManager {
    /**
     * Used to create a list of regions (amount is set on settings)
     * and return it in a List<CuboidRegion>
     * @param location location to start the regions
     * @param settings arena setting
     * @return the list of the regions
     */
    List<CuboidRegion> createRegions(Location location, ArenaSettings settings);

    /**
     * Get the region that is set to the given
     * BuildPlayer, if there's no CubicRegion it will
     * return null
     * @param game game where to search
     * @param player player to search
     * @return the player CuboidRegion
     */
    CuboidRegion getPlayerRegion(BuildGame game, Player player);

    /**
     * Get the center of the given region
     * @param region region to search center
     * @param settings settings of the arena
     * @return the center of the region
     */
    Location getCenter(CuboidRegion region, ArenaSettings settings);

    /**
     * Get where players should spawn when they're
     * going to vote the region
     * @param region region to check
     * @param settings settings of the arena
     * @return the vote Location
     */
    Location getVoteTeleport(CuboidRegion region, ArenaSettings settings);

    /**
     * Used to get if a block is in the given region
     * @param region region where we should check for the block
     * @param location location to check for
     * @param settings settings of the given arena
     * @return if the block is in this region
     */
    boolean containsBlock(CuboidRegion region, Location location, ArenaSettings settings);
}
