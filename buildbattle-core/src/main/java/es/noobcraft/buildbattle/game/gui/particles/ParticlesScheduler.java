package es.noobcraft.buildbattle.game.gui.particles;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ParticlesScheduler {
    private static int taskId = -1;

    public static void start(JavaPlugin plugin) {
        if (taskId == -1)
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> ParticleManager.getParticles().forEach((region, particles) -> {
                if (ParticleManager.getParticles().isEmpty()) return;

                particles.forEach((location, particle) ->
                        Bukkit.getWorld(location.getWorld().getName()).playEffect(location.clone().add(new Vector(0, 1, 0)), Effect.valueOf(particle.name()), 3, 5));
            }), 0L, 10L);
    }

    public static void stop() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            ParticleManager.reset();
        }
    }
}
