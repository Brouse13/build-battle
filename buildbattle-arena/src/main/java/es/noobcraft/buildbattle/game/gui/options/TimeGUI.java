package es.noobcraft.buildbattle.game.gui.options;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import es.noobcraft.buildbattle.api.game.arena.region.options.RegionTime;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Objects;

public class TimeGUI {
    private final NoobInventory inventory;
    private final Translator translator = Core.getTranslator();
    private final NoobPlayer player;

    public TimeGUI(NoobPlayer player) {
        this.player = player;
        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(player, "build-battle.arena.inventory.time.title"))
                .closeable(true)
                .rows(4)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update)
        );
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++)
            inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());

        inventory.set(10,
                ItemBuilder.from(Material.STAINED_CLAY).damage(1)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.time.day.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.time.day.lore")).build(),
                event -> setTime(event, RegionTime.DAY)
        );
        inventory.set(11,
                ItemBuilder.from(Material.STAINED_CLAY).damage(2)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.time.noon.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.time.noon.lore")).build(),
                event -> setTime(event, RegionTime.NOON)
        );
        inventory.set(12,
                ItemBuilder.from(Material.STAINED_CLAY).damage(3)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.time.sunset.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.time.sunset.lore")).build(),
                event -> setTime(event, RegionTime.SUNSET));
        inventory.set(14,
                ItemBuilder.from(Material.STAINED_CLAY).damage(4)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.time.night.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.time.night.lore")).build(),
                event -> setTime(event, RegionTime.NIGHT));
        inventory.set(15,
                ItemBuilder.from(Material.STAINED_CLAY).damage(5)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.time.midnight.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.time.midnight.lore")).build(),
                event -> setTime(event, RegionTime.MIDNIGHT)
        );
        inventory.set(16,
                ItemBuilder.from(Material.STAINED_CLAY).damage(6)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.time.sunrise.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.time.sunrise.lore")).build(),
                event -> setTime(event, RegionTime.SUNRISE)
        );
        inventory.set(35,
                ItemBuilder.from(Material.BARRIER)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.time.close.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.time.close.lore")).build(),
                event -> event.getWhoClicked().closeInventory()
        );
    }

    private void update(NoobInventory inventory) {
    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(player, inventory);
    }

    private void setTime(InventoryClickEvent event, RegionTime time) {
        BuildGame game = BuildBattleAPI.getGameManager().getGame();

        CuboidRegion region = BuildBattleAPI.getRegionManager()
                .getPlayerRegion(Objects.requireNonNull(game), (Player) event.getWhoClicked());

        ((Player) event.getWhoClicked()).setPlayerTime(time.getDuration(), false);
        event.getWhoClicked().closeInventory();
        region.setTime(time);
        Logger.player(player, "build-battle.arena.inventory.time.change-time", event.getCurrentItem().getItemMeta().getDisplayName());
    }
}
