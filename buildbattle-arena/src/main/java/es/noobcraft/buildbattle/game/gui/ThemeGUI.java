package es.noobcraft.buildbattle.game.gui;

import es.noobcraft.buildbattle.api.game.themes.GameTheme;
import es.noobcraft.buildbattle.api.game.themes.Theme;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class ThemeGUI {
    private final Translator translator = Core.getTranslator();

    private final NoobPlayer player;
    private final NoobInventory inventory;
    private final GameTheme themes;

    private Theme playerVoted;

    public ThemeGUI(GameTheme theme, NoobPlayer player) {
        this.themes = theme;
        this.player = player;
        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(player, "build-battle.arena.inventory.theme.title"))
                .closeable(false)
                .rows(6)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update)
        );
    }

    private void initialize(NoobInventory inventory) {
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++)
            inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());

        for (int i = 0; i < 6; i++) {
            int finalI = i;
            inventory.set(i*9, ItemBuilder.from(Material.SIGN)
                    .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.theme.sign.name", themes.getThemes().get(i).getTranslate(player)))
                    .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.theme.sign.lore", themes.getThemes().get(i).getTranslate(player))).build(),
                    event -> {
                        if (playerVoted  != null) themes.removeVote(((Player) event.getWhoClicked()));

                        playerVoted  = themes.getThemes().get(finalI);
                        themes.addVote(themes.getThemes().get(finalI), (Player) event.getWhoClicked());
                        event.setCancelled(true);
                        update(inventory);
            });
        }
    }

    private void update(NoobInventory inventory) {
        if (playerVoted == null) return;

        Theme maxVoted = themes.getMostVoted();

        for (int i = 0; i < themes.getThemes().size(); i++) {
            Theme currentVote = themes.getThemes().get(i);
            boolean hasVoted = playerVoted != null && currentVote == playerVoted;

            if (maxVoted == currentVote)
                fillLine(5, i, playerVoted, hasVoted);
            else if(hasVoted)
                fillLine(4, i, playerVoted, true);
            else if(themes.getVotes(currentVote)  > 0)
                fillLine(4, i, playerVoted, false);
            else
                fillLine(8, i , currentVote, false);
        }
    }

    private void fillLine(int durability, int start, Theme theme, boolean enchanted) {
        for (int i = start*9+1; i < (start*9)+9; i++) {
            ItemBuilder itemBuilder = ItemBuilder.from(Material.STAINED_GLASS_PANE)
                    .damage(durability)
                    .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.theme.background.name"))
                    .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.theme.background.lore",
                            theme.getTranslate(player), themes.getPercentage(theme)));
            if (enchanted) itemBuilder.enchantment(Enchantment.DURABILITY);

            inventory.set(i, itemBuilder.build());
        }
    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(player, inventory);
    }
}
