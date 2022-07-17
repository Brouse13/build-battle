package es.noobcraft.buildbattle.game.gui.options;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.arena.region.options.RegionBiome;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class BiomeGUI {
    private final NoobInventory inventory;
    private final Translator translator = Core.getTranslator();
    private final NoobPlayer player;

    public BiomeGUI(NoobPlayer player) {
        this.player = player;

        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(player, "build-battle.arena.inventory.biome.title"))
                .closeable(true)
                .rows(4)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update)
        );
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < RegionBiome.values().length; i++) {
            RegionBiome biome = RegionBiome.values()[i];
            inventory.set(i, ItemBuilder.from(biome.getMaterial()).damage(biome.getDamage())
                    .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.biome."+ biome.toString().toLowerCase()))
                    .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.biome.lore")).build(),
                    event -> {
                        BuildGame game = BuildBattleAPI.getGameManager().getGame();

                        BuildBattleAPI.getRegionManager()
                                .getPlayerRegion(game, ((Player) event.getWhoClicked())).setBiome(biome);
                        event.getWhoClicked().closeInventory();
                    });
        }
        update(inventory);
    }

    private void update(NoobInventory inventory) {
        inventory.set(35, ItemBuilder.from(Material.BARRIER)
                .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.biome.close.name"))
                .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.biome.close.lore")).build(),
                event -> event.getWhoClicked().closeInventory());
    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(player, inventory);
    }
}
