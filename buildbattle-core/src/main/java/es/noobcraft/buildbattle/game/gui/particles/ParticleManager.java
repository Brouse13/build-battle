package es.noobcraft.buildbattle.game.gui.particles;

import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParticleManager {
    @Getter
    private static final HashMap<CuboidRegion, Map<Location, Particles>> particles = new HashMap<>();

    public static void addParticle(CuboidRegion region, Particles particle, Player player) {
        Map<Location, Particles> particlesMap = particles.containsKey(region) ? particles.get(region) : new HashMap<>();
        if(particlesMap.size() == 10) return;

        particlesMap.put(player.getLocation(), particle);
        ParticleManager.particles.put(region, particlesMap);
    }

    public static void removeParticle(CuboidRegion region, int index) {
        Map<Location, Particles> particlesMap = ParticleManager.particles.get(region);
        particlesMap.remove(new ArrayList<>(particlesMap.keySet()).get(index));
        if (!particlesMap.isEmpty())
            ParticleManager.particles.put(region, particlesMap);
        else
            ParticleManager.particles.remove(region);
    }

    public static Map<Location, Particles> getParticles(CuboidRegion region) {
        return particles.get(region);
    }

    public static void reset() {
        particles.clear();
    }
}

