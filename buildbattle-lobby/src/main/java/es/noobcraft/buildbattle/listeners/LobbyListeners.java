package es.noobcraft.buildbattle.listeners;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.buildbattle.api.scoreboard.ScoreboardManager;
import es.noobcraft.buildbattle.configuration.Yaml;
import es.noobcraft.buildbattle.scoreboard.InfoScoreboard;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.event.NoobPlayerJoinEvent;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.tag.TagManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class LobbyListeners implements Listener {
    JavaPlugin plugin;

    public LobbyListeners(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoin(NoobPlayerJoinEvent event) {
        final BuildPlayer buildPlayer = BuildBattleAPI.getPlayerCache().getPlayer(event.getNoobPlayer().getUsername());

        event.getNoobPlayer().setGameMode(GameMode.ADVENTURE);
        event.getNoobPlayer().getInventory().setItem(8, ItemBuilder.from(Material.NETHER_STAR)
                        .metadata("event", "lobby")
                        .displayName(Core.getTranslator().getLegacyText(event.getNoobPlayer(), "build-battle.lobby.item.lobby.name"))
                        .lore(Core.getTranslator().getLegacyTextList(event.getNoobPlayer(), "build-battle.lobby.item.lobby.lore"))
                        .build());

        ScoreboardManager.create(buildPlayer, new InfoScoreboard(buildPlayer));

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            TagManager tagManager = SpigotCore.getNameTagManager().getTagManager(event.getNoobPlayer());

            if (Objects.nonNull(tagManager)) {//Can disconnect before update
                tagManager.appendTag(player -> BuildBattleAPI.getRankManager().updateRank(buildPlayer).getName());

                SpigotCore.getBukkitPlayerCache().getPlayers().forEach(tagManager::spawn);
            }
        },10L);

        Yaml yaml = new Yaml("config", false);
        event.getNoobPlayer().getPlayer().teleport(
                new Location(Bukkit.getWorld(yaml.getFile().getString("spawn.world")),
                        yaml.getFile().getInt("spawn.x"),
                        yaml.getFile().getInt("spawn.y"),
                        yaml.getFile().getInt("spawn.z"),
                        yaml.getFile().getInt("spawn.yaw"),
                        yaml.getFile().getInt("spawn.pitch")));
    }

    @EventHandler
    public void itemThrow(PlayerDropItemEvent event) {
        if (event.getItemDrop() == null) return;
        if(SpigotCore.getNBTTagHelper().hasKey(event.getItemDrop().getItemStack(), "event") &&
                SpigotCore.getNBTTagHelper().getValue(event.getItemDrop().getItemStack(), "event").equals("lobby"))
            event.setCancelled(true);
    }

    @EventHandler
    public void itemMove(InventoryMoveItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void itemClick(PlayerInteractEvent event) {
        if (event.getItem() == null) return;

        if(SpigotCore.getNBTTagHelper().hasKey(event.getItem(), "event") &&
                SpigotCore.getNBTTagHelper().getValue(event.getItem(), "event").equals("lobby")) {
            Core.getLobbyManager().connectToLobby(Core.getPlayerCache().getPlayer(event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void entitySpawn(EntitySpawnEvent event) {
        if(!(event.getEntity() instanceof Player))
            event.setCancelled(true);
    }

    @EventHandler
    public void worldChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void entityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }
}
