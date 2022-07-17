package es.noobcraft.buildbattle.hologram;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.core.api.event.NoobPlayerJoinEvent;
import es.noobcraft.core.api.event.NoobPlayerQuitEvent;
import es.noobcraft.holographicdisplays.api.Hologram;
import es.noobcraft.holographicdisplays.api.HologramsAPI;
import es.noobcraft.holographicdisplays.api.line.TextLine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HashSetTopHologram implements Listener {
    private static final Set<TopHologram> loadedHolograms  = Sets.newHashSet(new VictoriesTopHolograms(), new DefeatsTopVictories());

    private static JavaPlugin plugin;

    public HashSetTopHologram(JavaPlugin plugin) {
        HashSetTopHologram.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void HologramSpawn(NoobPlayerJoinEvent event) {
        loadedHolograms.forEach(topHologram -> {
            Hologram hologram = HologramsAPI.createHologram(plugin, topHologram.getLoacation());
            hologram.getVisibilityManager().setVisibleByDefault(false);
            hologram.getVisibilityManager().showTo(event.getNoobPlayer().getPlayer());
            topHologram.addHologram(PlayerHologram.builder()
                    .player(event.getNoobPlayer()).hologram(hologram).lines(Lists.newArrayList()).build());
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void HologramRemove(NoobPlayerQuitEvent event) {
        loadedHolograms.forEach(topHologram -> topHologram.removeHologram(event.getNoobPlayer()));
    }

    public void update() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () ->
            loadedHolograms.forEach(topHologram -> {
                if (!topHologram.getHolograms().isEmpty()) {
                    Map<String, Integer> top = BuildBattleAPI.getTopManager().getTop(10, topHologram.getIdentifier());
                    topHologram.getHolograms().values().forEach(playerHologram -> {
                        Hologram hologram = playerHologram.getHologram();
                        List<TextLine> lines = playerHologram.getLines();

                        if (lines.isEmpty()) {
                            lines.add(hologram.appendTextLine("BuildBattle - Top 10 "+ topHologram.getIdentifier()));
                            top.entrySet().stream()
                                    .map(s -> s.getKey() + ": " + s.getValue())
                                    .map(hologram::appendTextLine)
                                    .forEach(lines::add);
                        }else {
                            List<String> newTop = top.entrySet().stream().map(entryset -> entryset.getKey() + ": " + entryset.getValue())
                                    .collect(Collectors.toList());

                            lines.get(0).setText("BuildBattle - Top 10 "+ topHologram.getIdentifier());

                            for (int i = 0; i < newTop.size(); i++) {
                                final TextLine textLine = lines.get(i+1);

                                textLine.setText(newTop.get(i));
                            }
                        }
                    });
                }
        }), 0L,  10 * 20L);
    }
}
