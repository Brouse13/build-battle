package es.noobcraft.buildbattle.game.inventories;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.buildbattle.game.gui.options.OptionsGUI;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.item.NBTTagHelper;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameInv implements Listener {
    private final NBTTagHelper nbtHelper = SpigotCore.getNBTTagHelper();

    public static void giveInventory(BuildPlayer buildPlayer) {
        Translator translator = Core.getTranslator();
        NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(buildPlayer.getName());

        buildPlayer.getPlayer().getInventory().setItem(8, ItemBuilder.from(Material.NETHER_STAR)
                .displayName(translator.getLegacyText(noobPlayer, "build-battle.arena.items.options.name"))
                .metadata("event", "options")
                .lore(translator.getLegacyTextList(noobPlayer, "build-battle.arena.items.options.lore"))
                .build());
    }

    @EventHandler
    public void GameInventory(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (nbtHelper.hasKey(event.getItem(), "event") && nbtHelper.getValue(event.getItem(), "event").equals("options")) {
            BuildGame game = BuildBattleAPI.getGameManager().getGame();

            if (game == null) return;
            CuboidRegion region = BuildBattleAPI.getRegionManager().getPlayerRegion(game, event.getPlayer());

            new OptionsGUI(region, Core.getPlayerCache().getPlayer(event.getPlayer().getName())).openInventory();
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack() == null) return;
        if (nbtHelper.hasKey(event.getItemDrop().getItemStack(), "event")
                && nbtHelper.getValue(event.getItemDrop().getItemStack(), "event").equals("options")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void moveItem(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (nbtHelper.hasKey(event.getCurrentItem(), "event") && nbtHelper.getValue(event.getCurrentItem(), "event").equals("options"))
            event.setCancelled(true);

    }
}
