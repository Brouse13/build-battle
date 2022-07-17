package es.noobcraft.buildbattle.listeners;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.GameStatus;
import es.noobcraft.buildbattle.api.game.arena.ArenaSettings;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class GriffinListeners implements Listener {
    @EventHandler
    public void cancelSaturation(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void cancelHealth(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player)
            event.setCancelled(true);
    }

    @EventHandler
    public void pickupItem(PlayerPickupItemEvent event) {
        BuildGame game = BuildBattleAPI.getGameManager().getGame();
        if (game == null) return;

        BuildArena arena = BuildBattleAPI.getArenaManager().getByServer(game.getServer());

        if (!arena.getStatus().equals(GameStatus.INPROGRESS)) event.setCancelled(true);
    }

    @EventHandler
    public void cancelExplosion(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void throwItem(PlayerDropItemEvent event) {
        BuildGame game = BuildBattleAPI.getGameManager().getGame();
        if (game == null) return;

        BuildArena arena = BuildBattleAPI.getArenaManager().getByServer(game.getServer());

        if (!arena.isEnabled()) return;
        if (!arena.getStatus().equals(GameStatus.INPROGRESS)) event.setCancelled(true);
    }

    @EventHandler
    public void cancelItemMove(InventoryClickEvent event) {
        BuildGame game = BuildBattleAPI.getGameManager().getGame();

        if (game == null) return;

        BuildArena arena = BuildBattleAPI.getArenaManager().getByServer(game.getServer());

        if (!arena.isEnabled()) return;

        if (!arena.getStatus().equals(GameStatus.INPROGRESS))
            if (event.getInventory().getType().equals(InventoryType.CRAFTING))
                event.setCancelled(true);
    }

    @EventHandler
    public void cancelTicks(BlockRedstoneEvent event) {
        event.setNewCurrent(0);
    }

    @EventHandler
    public void cancelSpread(BlockFromToEvent event) {
        BuildGame game = BuildBattleAPI.getGameManager().getGame();
        ArenaSettings settings = BuildBattleAPI.getArenaManager().getByServer(game.getServer()).getArenaSettings();

        for (CuboidRegion region : game.getBuildSpawns()) {
            if (BuildBattleAPI.getRegionManager().containsBlock(region, event.getBlock().getLocation(), settings)) {
                if (BuildBattleAPI.getRegionManager().containsBlock(region, event.getToBlock().getLocation(), settings)) {
                    event.setCancelled(false);
                    return;
                }
                break;
            }
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void leaveDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void entitySpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }
}
