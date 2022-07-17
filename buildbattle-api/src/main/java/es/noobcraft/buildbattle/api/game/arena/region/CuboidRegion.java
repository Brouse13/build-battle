package es.noobcraft.buildbattle.api.game.arena.region;

import es.noobcraft.buildbattle.api.game.arena.region.options.RegionBiome;
import es.noobcraft.buildbattle.api.game.arena.region.options.RegionTime;
import es.noobcraft.buildbattle.api.game.arena.region.options.RegionWeather;
import es.noobcraft.buildbattle.api.game.arena.votes.RegionVotes;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public interface CuboidRegion {
    /**
     * Get the vector location of the minimum block of this region
     * @return the min point of the region
     */
    Vector getMin();

    /**
     * Get the vector location of the maximum block of the region
     * @return the max point of the region
     */
    Vector getMax();

    /**
     * Get the world where the regions will be located
     * @return the world of the region
     */
    World getWorld();

    /**
     * Get the list of all the owners of this region.
     *
     * This list will be a singletonList in case teamMode
     * is set to false
     * @return all the owners of the region
     */
    List<Player> getOwners();

    /**
     * Add a new owner to the current region
     * @param player player to add
     */
    void addOwner(Player player);

    /**
     * Get the RegionTime of this region
     * @return the RegionTime of the region
     */
    RegionTime getTime();

    /**
     * Set the RegionTime to this region
     * @param time RegionTime to set
     */
    void setTime(RegionTime time);

    /**
     * Get the RegionWeather of this region
     * @return the RegionWeather of the region
     */
    RegionWeather getWeather();

    /**
     * Set the RegionWeather of this region
     * @param weather Weather to set
     */
    void setWeather(RegionWeather weather);

    /**
     * Get the RegionBiome of this region
     * @return the RegionBiome of the region
     */
    RegionBiome getBiome();

    /**
     * Set the RegionBiome of this region
     * @param biome RegionBiome to set
     */
    void setBiome(RegionBiome biome);

    /**
     * Set if the owners can build on this region
     * @param bool bool to set
     */
    void setCanBuild(boolean bool);

    /**
     * Get the RegionVotes that manages all the
     * players that have voted on this region
     * @return the region RegionVotes
     */
    RegionVotes getRegionVotes();

    /**
     * Get if a player can build on this
     * region
     * @param player player to check
     * @return if the player can build
     */
    boolean canBuild(Player player);

    /**
     * Get the ItemStack that forms the region floor
     * @return the floor ItemStack
     */
    ItemStack getFloor();

    /**
     * Set the floor of the given region
     * @param itemStack new ItemStack for the floor
     */
    void setFloor(ItemStack itemStack);

    /**
     * Cleans the region and set it values to default
     */
    void cleanArena();
}
