package es.noobcraft.buildbattle.game.inventories;

import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.buildbattle.configuration.Yaml;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.item.ItemBuilder;
import es.noobcraft.core.api.item.NBTTagHelper;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class LobbyInv implements Listener {
    public static void giveInventory(BuildPlayer buildPlayer) {
        Translator translator = Core.getTranslator();
        NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(buildPlayer.getName());

        buildPlayer.getPlayer().getInventory().setItem(8, ItemBuilder.from(Material.NETHER_STAR)
                .displayName(translator.getLegacyText(noobPlayer, "build-battle.arena.items.lobby.name"))
                .metadata("event", "lobby")
                .lore(translator.getLegacyTextList(noobPlayer, "build-battle.arena.items.lobby.lore"))
                .build());
    }

    @EventHandler
    public void leaveHubEvent(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        NBTTagHelper nbtHelper = SpigotCore.getNBTTagHelper();

        if (nbtHelper.hasKey(event.getItem(), "event") && nbtHelper.getValue(event.getItem(), "event").equals("lobby")) {
            Yaml yml = new Yaml("config", false);
            NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(event.getPlayer().getName());

            Logger.player(noobPlayer, "build-battle.arena.messages.leaving-arena");
            Core.getPlayerCache().getPlayer(event.getPlayer().getName()).connect(yml.getFile().getString("settings.lobby"));
        }
    }
}
