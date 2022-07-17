package es.noobcraft.buildbattle.game.gui.options.particles;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.game.gui.particles.ParticleManager;
import es.noobcraft.buildbattle.game.gui.particles.Particles;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class AddParticleGUI {
    private final Translator translator = Core.getTranslator();
    private final NoobInventory inventory;
    private final NoobPlayer player;

    public AddParticleGUI(NoobPlayer player) {
        this.player = player;

        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(player, "build-battle.arena.inventory.add-particles.title"))
                .closeable(true)
                .rows(4)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update)
        );
    }

    private void initialize(NoobInventory inventory) {
        inventory.set(31, ItemBuilder.from(Material.WOOL).damage(4)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.add-particles.list.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.add-particles.list.lore"))
                        .build(),
                event -> new ListParticlesGUI(((Player) event.getWhoClicked())).openInventory());
        inventory.set(35, ItemBuilder.from(Material.BARRIER)
                        .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.add-particles.close.name"))
                        .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.add-particles.close.lore"))
                        .build(),
                event -> event.getWhoClicked().closeInventory());
        update(inventory);
    }

    private void update(NoobInventory inventory) {
        for (int i = 0; i < Particles.values().length; i++) {
            int finalI = i;
            inventory.set(i, ItemBuilder.from(Particles.values()[i].getMaterial())
                            .displayName(translator.getLegacyText(player, "build-battle.arena.inventory.particles."+ Particles.values()[i].toString().toLowerCase()))
                            .lore(translator.getLegacyTextList(player, "build-battle.arena.inventory.add-particles.add.lore", Particles.values()[i]))
                            .build(),
                    event -> {
                        BuildGame game = BuildBattleAPI.getGameManager().getGame();

                        ParticleManager.addParticle(BuildBattleAPI.getRegionManager()
                                .getPlayerRegion(game, ((Player) event.getWhoClicked())),
                                Particles.values()[finalI], ((Player) event.getWhoClicked()));
                        event.getWhoClicked().closeInventory();
                    });
        }
    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(player, inventory);
    }
}
