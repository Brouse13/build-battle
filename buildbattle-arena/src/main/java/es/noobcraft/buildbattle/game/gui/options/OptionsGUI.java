package es.noobcraft.buildbattle.game.gui.options;

import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import es.noobcraft.buildbattle.game.gui.options.particles.AddParticleGUI;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.item.SkullBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class OptionsGUI {
    @Getter private final NoobInventory inventory;
    private final NoobPlayer player;

    private final CuboidRegion region;
    private final Translator translator = Core.getTranslator();

    public OptionsGUI(CuboidRegion region, NoobPlayer player) {
        this.player = player;
        this.region = region;
        inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(player, "build-battle.arena.inventory.options.title"))
                .closeable(true)
                .rows(6)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update)
        );
    }

    @SuppressWarnings("deprecation")
    private void initialize(NoobInventory inventory) {
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTNjMWRmOTEwZmUwYTU4YTgzODNmMTBiMjc0ZWNkMzk5NTg3NDE0MDYyNDNmY2U2MTMyYjdmMjI3N2E0YThlNCJ9fX0=";
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++)
            inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());

        inventory.set(11,
                ItemBuilder.from(Material.DOUBLE_PLANT)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.options.weather.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.options.weather.lore")).build(),
                event -> new WeatherGUI(player).openInventory());
        inventory.set(12,
                ItemBuilder.from(Material.WATCH)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.options.time.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.options.time.lore")).build(),
                event -> new TimeGUI(player).openInventory());
        /*
        inventory.set(14,
                ItemBuilder.from(Material.MAP)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.options.biome.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.options.biome.lore")).build(),
                event -> new BiomeGUI(player).openInventory());
         */
        inventory.set(15,
                ItemBuilder.from(region.getFloor().getType())
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.options.floor.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.options.floor.lore")).build(),
                event -> {
                    ItemStack item = event.getCursor();
                    if (item.getType().isBlock() && item.getType().isSolid()) {
                        region.setFloor(item);
                        event.setCursor(new ItemStack(Material.AIR));
                        event.getWhoClicked().closeInventory();
                    }
                });
        inventory.set(21,
                ItemBuilder.from(Material.BLAZE_POWDER)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.options.particle.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.options.particle.lore")).build(),
                event -> new AddParticleGUI(player).openInventory());
        inventory.set(23,
                ItemBuilder.from(Material.BANNER)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.options.banner.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.options.banner.lore")).build(),
                event -> new BannerGUI(player).openInventory());
        inventory.set(31,
                SkullBuilder.create().textures(base64)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.options.skulls.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.options.skulls.lore")).build(),
                event -> new SkullGUI(player).openInventory());
        inventory.set(53,
                ItemBuilder.from(Material.BARRIER)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.options.close.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.options.close.lore")).build(),
                event -> event.getWhoClicked().closeInventory());
    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(player, inventory);
    }

    private void update(NoobInventory inventory) {

    }
}
