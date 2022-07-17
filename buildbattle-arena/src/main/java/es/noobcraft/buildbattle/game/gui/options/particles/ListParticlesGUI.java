package es.noobcraft.buildbattle.game.gui.options.particles;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import es.noobcraft.buildbattle.game.gui.particles.ParticleManager;
import es.noobcraft.buildbattle.game.gui.particles.Particles;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.inventory.NoobInventory;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;

public class ListParticlesGUI {
    private final Translator translator = Core.getTranslator();
    private final NoobInventory inventory;
    private final Player player;
    private final NoobPlayer noobPlayer;

    public ListParticlesGUI(Player player) {
        this.player = player;
        this.noobPlayer = Core.getPlayerCache().getPlayer(player.getName());
        this.inventory = SpigotCore.getInventoryManager().createInventory(inventoryBuilder -> inventoryBuilder
                .title(translator.getLegacyText(noobPlayer, "build-battle.arena.inventory.list-particles.title"))
                .closeable(true)
                .rows(4)
                .type(InventoryType.CHEST)
                .initializer(this::initialize)
                .updater(this::update)
        );
    }

    private void initialize(NoobInventory inventory) {
        inventory.set(31, ItemBuilder.from(Material.WOOL).damage(4)
                        .displayName(translator.getLegacyText(noobPlayer, "build-battle.arena.inventory.list-particles.add.name"))
                        .lore(translator.getLegacyTextList(noobPlayer, "build-battle.arena.inventory.list-particles.add.lore"))
                        .build(),
                event -> new AddParticleGUI(noobPlayer).openInventory());
        inventory.set(35, ItemBuilder.from(Material.BARRIER)
                        .displayName(translator.getLegacyText(noobPlayer, "build-battle.arena.inventory.list-particles.close.name"))
                        .lore(translator.getLegacyTextList(noobPlayer, "build-battle.arena.inventory.list-particles.close.lore"))
                        .build(),
                event -> event.getWhoClicked().closeInventory());
        update(inventory);
    }

    private void update(NoobInventory inventory) {
        BuildGame game = BuildBattleAPI.getGameManager().getGame();
        CuboidRegion region = BuildBattleAPI.getRegionManager().getPlayerRegion(game, player);

        clear(inventory);
        Map<Location, Particles> particlesMap = ParticleManager.getParticles(region);
        if (particlesMap == null || particlesMap.isEmpty()) {
            inventory.set(0, ItemBuilder.from(Material.BARRIER)
                    .displayName(translator.getLegacyText(noobPlayer, "build-battle.arena.inventory.list-particles.no-particles.name"))
                    .lore(translator.getLegacyTextList(noobPlayer, "build-battle.arena.inventory.list-particles.no-particles.lore")).build(),
                    event -> new AddParticleGUI(noobPlayer).openInventory());
            return;
        }

        for (int i = 0; i < particlesMap.size(); i++) {
            Particles particle = new ArrayList<>(particlesMap.values()).get(i);
            inventory.set(i, ItemBuilder.from(particle.getMaterial())
                            .displayName(translator.getLegacyText(noobPlayer, "build-battle.arena.inventory.particles."+ particle.toString().toLowerCase()))
                            .lore(translator.getLegacyTextList(noobPlayer, "build-battle.arena.inventory.list-particles.remove.lore"))
                            .build(),
                    event -> ParticleManager.removeParticle(region, event.getSlot()));
        }
    }

    public void openInventory() {
        SpigotCore.getInventoryManager().openInventory(noobPlayer, inventory);
    }

    private void clear(NoobInventory inventory) {
        for (int i = 0; i < 27; i++)
            inventory.set(i, new ItemStack(Material.AIR));
    }
}
