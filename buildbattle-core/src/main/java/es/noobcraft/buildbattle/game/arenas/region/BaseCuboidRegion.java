package es.noobcraft.buildbattle.game.arenas.region;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.arena.ArenaSettings;
import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import es.noobcraft.buildbattle.api.game.arena.region.options.RegionBiome;
import es.noobcraft.buildbattle.api.game.arena.region.options.RegionTime;
import es.noobcraft.buildbattle.api.game.arena.region.options.RegionWeather;
import es.noobcraft.buildbattle.api.game.arena.votes.RegionVotes;
import es.noobcraft.buildbattle.game.arenas.votes.HashRegionVote;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BaseCuboidRegion implements CuboidRegion {
    @Getter private final Vector min;
    @Getter private final Vector max;
    @Getter private final World world;
    @Getter private List<Player> owners;
    @Getter private RegionVotes regionVotes;
    @Getter @Setter private RegionTime time;
    @Getter @Setter private RegionWeather weather;
    @Getter private RegionBiome biome;
    @Setter private boolean canBuild;

    private final ArenaSettings settings;

    public BaseCuboidRegion(World world, Vector bound1, Vector bound2, ArenaSettings settings) {
        this.min = Vector.getMinimum(bound1, bound2);
        this.max = Vector.getMaximum(bound1, bound2);
        this.world = world;
        this.settings = settings;
        cleanArena();
    }

    @Override
    public void setBiome(RegionBiome biome) {
        this.biome = biome;
        for (int x = (int)min.getX()+ settings.getBorderSize()/2; x <= (int)max.getX()- settings.getBorderSize()/2; x++)
            for (int z = (int)min.getZ()+ settings.getBorderSize()/2; z <= (int)max.getZ()- settings.getBorderSize()/2; z++)
                for (int y = 0; y < max.getY(); y++)
                    world.setBiome(x, z, Biome.valueOf(biome.name()));

    }

    @Override
    public void addOwner(Player player) {
        owners.add(player);
    }

    @Override
    public boolean canBuild(Player player) {
        return canBuild && owners.contains(player);
    }
    @Override
    public ItemStack getFloor() {
        return BuildBattleAPI.getRegionManager().getCenter(this, settings).clone()
                .subtract(new Vector(0, 1, 0)).getBlock()
                .getState().getData().toItemStack(1);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setFloor(ItemStack itemStack) {
        int half = settings.getBorderSize() / 2;
        for (int x = (int)min.getX()+ half; x <= (int)max.getX()- half; x++)
            for (int z = (int)min.getZ()+ half; z <= (int)max.getZ()- half; z++) {
                Location location = new Location(world, x, min.getY()+1, z);
                location.getBlock().setType(itemStack.getType());
                location.getBlock().setData(itemStack.getData().getData());
            }
    }

    @Override
    public void cleanArena() {
        this.canBuild  = true;
        this.biome = RegionBiome.FOREST;
        this.weather = RegionWeather.SUN;
        this.time = RegionTime.DAY;
        this.regionVotes = new HashRegionVote();
        this.owners = new ArrayList<>();

        for (int x = (int)min.getX()+ settings.getBorderSize()/2; x <= (int)max.getX()- settings.getBorderSize()/2; x++)
            for (int y = (int)min.getY()+2; y < (int)max.getY(); y++)
                for (int z = (int)min.getZ()+ settings.getBorderSize()/2; z <= (int)max.getZ()- settings.getBorderSize()/2; z++) {
                    Location location = new Location(this.world, x, y, z);
                    location.getBlock().setType(Material.AIR);
                }
        setFloor(new ItemStack(Material.STAINED_CLAY));
    }
}
