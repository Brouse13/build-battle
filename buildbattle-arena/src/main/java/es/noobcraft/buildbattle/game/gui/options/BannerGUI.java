package es.noobcraft.buildbattle.game.gui.options;

import es.noobcraft.buildbattle.game.gui.banner.Banner;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.Collections;

public class BannerGUI {
    private final Translator translator = Core.getTranslator();
    private final NoobInventory inventory;
    private final NoobPlayer player;
    private final Banner banner;

    public BannerGUI(NoobPlayer player) {
        this.player = player;
        this.banner = new Banner(DyeColor.WHITE, new ArrayList<>());

        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(player, "build-battle.arena.inventory.banner.title"))
                .closeable(true)
                .rows(6)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update)
        );
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < DyeColor.values().length; i++) {
            DyeColor color = DyeColor.values()[i];
            Banner clone = new Banner(color, new ArrayList<>());
            inventory.set(i, ItemBuilder.from(clone.toBanner())
                            .displayName(translator.getLegacyText(player, "build-battle.arena.messages.banner.color."+ color.toString().toLowerCase()))
                            .lore(translator.getLegacyTextList(player, "build-battle.arena.messages.banner.color.lore")).build(),
                    event -> {
                        this.banner.setColor(clone.getColor());
                        addPatterns();
                        update(inventory);
                    }
            );
        }
        update(inventory);
    }

    private void update(NoobInventory inventory) {
        inventory.set(53, ItemBuilder.from(Material.BARRIER)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.banner.clear.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.banner.clear.lore")).build(),
                event -> {
                    this.banner.clearLastPattern();
                    update(inventory);
                }
        );
        inventory.set(49, ItemBuilder.from(this.banner.toBanner())
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.banner.result.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.banner.result.lore")).build(),
                event -> {
                    event.getWhoClicked().getInventory().addItem(this.banner.toBanner());
                    event.getWhoClicked().closeInventory();
                }
        );
    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(player, inventory);
    }

    private void addColors(PatternType pattern) {
        clearLast();
        for (int i = 0; i < DyeColor.values().length; i++) {
            DyeColor color = DyeColor.values()[i];
            Banner clone = new Banner(banner.getColor(), Collections.singletonList(new Pattern(color, pattern)));
            inventory.set(i, ItemBuilder.from(clone.toBanner())
                            .displayName(translator.getLegacyText(player, "build-battle.arena.messages.banner.pattern."+ pattern.toString().toLowerCase()))
                            .lore(translator.getLegacyTextList(player, "build-battle.arena.messages.banner.pattern.lore")).build(),
                    event -> {
                        this.banner.addPattern(clone.getLastPattern());
                        addPatterns();
                        update(inventory);
                    }
            );
        }
    }

    private void addPatterns() {
        clearLast();
        for (int i = 0; i < PatternType.values().length; i++) {
            PatternType pattern = PatternType.values()[i];
            Banner clone = new Banner(banner.getColor(), new ArrayList<>());
            clone.addPattern(new Pattern(banner.getColor()  == DyeColor.WHITE ? DyeColor.BLACK : DyeColor.WHITE, pattern));
            inventory.set(i, ItemBuilder.from(clone.toBanner())
                            .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.banner.pattern." + pattern.toString().toLowerCase()))
                            .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.banner.pattern")).build(),
                    event -> addColors(pattern)

            );
        }
    }

    private void clearLast() {
        for (int i = 0; i < 45; i++)
            inventory.set(i, ItemBuilder.from(Material.AIR).build());
    }
}
