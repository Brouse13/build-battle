package es.noobcraft.buildbattle.api.game.arena.region.options;

import lombok.Getter;
import org.bukkit.Material;

public enum RegionBiome {
    FOREST(Material.SAPLING),
    BEACH(Material.SAND),
    SAVANNA(Material.SAPLING, 4),
    TAIGA(Material.SNOW),
    OCEAN(Material.RAW_FISH),
    DEEP_OCEAN(Material.WATER_BUCKET),
    DESERT(Material.DEAD_BUSH),
    HELL(Material.NETHERRACK),
    ICE_MOUNTAINS(Material.ICE),
    JUNGLE(Material.LOG, 3),
    MESA(Material.HARD_CLAY),
    MUSHROOM_ISLAND(Material.MUSHROOM_SOUP),
    PLAINS(Material.LONG_GRASS, 2),
    RIVER(Material.SUGAR_CANE),
    ROOFED_FOREST(Material.SAPLING, 5),
    SWAMPLAND(Material.SLIME_BALL);

    @Getter private final Material material;
    @Getter private final int damage;

    RegionBiome(Material material) {
        this.material = material;
        this.damage = 0;
    }

    RegionBiome(Material material, int damage) {
        this.material = material;
        this.damage = damage;
    }
}
