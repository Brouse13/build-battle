package es.noobcraft.buildbattle.hologram;

import es.noobcraft.core.api.player.BukkitNoobPlayer;
import org.bukkit.Location;

import java.util.Map;


public interface TopHologram {
    String getIdentifier();

    Map<BukkitNoobPlayer, PlayerHologram> getHolograms();

    void addHologram(PlayerHologram playerHologram);

    void removeHologram(BukkitNoobPlayer bukkitNoobPlayer);

    Location getLoacation();
}
