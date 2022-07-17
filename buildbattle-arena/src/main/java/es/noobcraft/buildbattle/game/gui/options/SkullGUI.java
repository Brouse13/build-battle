package es.noobcraft.buildbattle.game.gui.options;

import es.noobcraft.buildbattle.configuration.Yaml;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.item.SkullBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;

public class SkullGUI {
    private final NoobInventory inventory;
    private final Translator translator = Core.getTranslator();
    private final NoobPlayer player;
    private final Yaml yml = new Yaml("skulls", false);

    private final List<String> skulls = new ArrayList<>();

    public SkullGUI(NoobPlayer player) {
        this.player = player;
        this.skulls.addAll(yml.getFile().getConfigurationSection("skulls").getKeys(false));

        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(player, "build-battle.arena.inventory.skulls.title"))
                .closeable(true)
                .rows(6)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update)
        );
    }

    private void initialize(NoobInventory inventory) {
        createPage(0, inventory);
    }

    private void update(NoobInventory inventory) {
    }

    public void createPage(int page, NoobInventory inventory) {
        for (int i = 0; i < inventory.getRows() * inventory.getColumns(); i++)
            inventory.set(i, ItemBuilder.from(Material.STAINED_GLASS_PANE).damage(8).build());

        //Inventory contains more than 1 page
        if (skulls.size() / 45 > 0) {
            //Current  Page
            if (page < (skulls.size() / 45) && page != 0)
                inventory.set(45,
                        ItemBuilder.from(Material.ARROW)
                                .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.skulls.next.name"))
                                .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.skulls.next.lore"))
                                .build(),
                        event -> createPage(page + 1, inventory)
                );
            //Next Page
            if (page > (skulls.size() / 45) && page != (skulls.size() / 45))
                inventory.set(45,
                        ItemBuilder.from(Material.ARROW)
                                .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.skulls.previous.name"))
                                .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.skulls.previous.lore"))
                                .build(),
                        event -> createPage(page - 1, inventory)
                );
        }
        for (int i = page*45; i < (page+1)*45; i++) {
            try {
                inventory.set(i,
                        SkullBuilder.create().textures(yml.getFile().getString("skulls."+ skulls.get(i)+ ".texture"))
                                .displayName(yml.getFile().getString("skulls."+ skulls.get(i)+ ".name"))
                                .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.skulls.skull.lore"))
                                .build(),
                        event -> {
                            event.getWhoClicked().getInventory().addItem(event.getCurrentItem());
                            event.getWhoClicked().closeInventory();
                        }
                );
            }catch (Exception e) {
                break;
            }
        }
    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(player, inventory);
    }
}
