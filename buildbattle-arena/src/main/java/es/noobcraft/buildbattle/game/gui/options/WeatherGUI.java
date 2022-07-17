package es.noobcraft.buildbattle.game.gui.options;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import es.noobcraft.buildbattle.api.game.arena.region.options.RegionWeather;
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

public class WeatherGUI {
    private final Translator translator = Core.getTranslator();

    private final NoobPlayer player;
    private final NoobInventory inventory;

    public WeatherGUI(NoobPlayer player) {
        this.player = player;
        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(player, "build-battle.arena.inventory.weather.title"))
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
                ItemBuilder.from(Material.BLAZE_ROD)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.weather.thunder.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.weather.thunder.lore")).build(),
                event -> event.getWhoClicked().sendMessage("Not implemented yet")
        );
        inventory.set(12,
                ItemBuilder.from(Material.WATER_BUCKET)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.weather.rain.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.weather.rain.lore")).build(),
                event -> setWeather(event, RegionWeather.RAIN)
        );
        inventory.set(14,
                ItemBuilder.from(Material.DOUBLE_PLANT)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.sunny.npc.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.sunny.npc.lore")).build(),
                event -> setWeather(event, RegionWeather.SUN)
        );
        inventory.set(16,
                ItemBuilder.from(Material.SNOW_BALL)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.weather.snow.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.weather.snow.lore")).build(),
                event -> event.getWhoClicked().sendMessage("Not implemented yet")
        );
        inventory.set(35,
                ItemBuilder.from(Material.BARRIER)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.weather.close.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.weather.close.lore")).build(),
                event -> event.getWhoClicked().closeInventory()
        );

    }

    private void update(NoobInventory inventory) {
    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(player, inventory);
    }

    private void setWeather(InventoryClickEvent event, RegionWeather weather) {
        BuildGame game = BuildBattleAPI.getGameManager().getGame();

        CuboidRegion region = BuildBattleAPI.getRegionManager()
                .getPlayerRegion(game, ((Player) event.getWhoClicked()));

        ((Player) event.getWhoClicked()).setPlayerWeather(weather.toWeatherType());
        region.setWeather(weather);
        event.getWhoClicked().closeInventory();
    }
}
