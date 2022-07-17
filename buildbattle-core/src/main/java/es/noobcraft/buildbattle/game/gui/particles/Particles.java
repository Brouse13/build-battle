package es.noobcraft.buildbattle.game.gui.particles;


import lombok.Getter;
import org.bukkit.Material;

public enum Particles {
    MOBSPAWNER_FLAMES(Material.MOB_SPAWNER),
    PORTAL(Material.OBSIDIAN),
    EXPLOSION(Material.TNT),
    NOTE(Material.NOTE_BLOCK),
    SMOKE(Material.FURNACE),
    LAVADRIP(Material.STONE),
    CRIT(Material.DIAMOND_SWORD),
    SPELL(Material.ENCHANTMENT_TABLE),
    SNOW_SHOVEL(Material.SNOW_BALL),
    FLAME(Material.STONE),
    LAVA_POP(Material.LAVA_BUCKET),
    COLOURED_DUST(Material.REDSTONE),
    SLIME(Material.SLIME_BALL),
    HEART(Material.GOLDEN_APPLE),
    VILLAGER_THUNDERCLOUD(Material.STONE),
    HAPPY_VILLAGER(Material.EMERALD);

    @Getter
    private final Material material;

    Particles(Material material) {
        this.material = material;
    }
}
