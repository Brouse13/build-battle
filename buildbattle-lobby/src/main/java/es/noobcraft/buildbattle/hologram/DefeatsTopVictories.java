package es.noobcraft.buildbattle.hologram;

import com.google.common.collect.ImmutableMap;
import es.noobcraft.buildbattle.configuration.Yaml;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class DefeatsTopVictories implements TopHologram {
    private final Map<BukkitNoobPlayer, PlayerHologram> holograms = new HashMap<>();

    @Override
    public String getIdentifier() {
        return "defeats";
    }

    @Override
    public Map<BukkitNoobPlayer, PlayerHologram> getHolograms() {
        return ImmutableMap.copyOf(holograms);
    }

    @Override
    public void addHologram(PlayerHologram playerHologram) {
        holograms.put(playerHologram.getPlayer(), playerHologram);
    }

    @Override
    public void removeHologram(BukkitNoobPlayer bukkitNoobPlayer) {
        holograms.get(bukkitNoobPlayer).getHologram().delete();
        holograms.remove(bukkitNoobPlayer);
    }

    @Override
    public Location getLoacation() {
        Yaml yml = new Yaml("config", false);
        return new Location(Bukkit.getWorld(yml.getFile().getString("holograms.looses.world")),
                yml.getFile().getInt("holograms.looses.x"),
                yml.getFile().getInt("holograms.looses.y"),
                yml.getFile().getInt("holograms.looses.z"));
    }
}
