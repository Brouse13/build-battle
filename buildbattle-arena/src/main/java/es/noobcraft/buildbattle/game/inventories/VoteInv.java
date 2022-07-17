package es.noobcraft.buildbattle.game.inventories;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.GameStatus;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.buildbattle.game.arenas.votes.BuildVoteType;
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

public class VoteInv implements Listener {
    public static void giveInventory(BuildPlayer buildPlayer) {
        Translator translator = Core.getTranslator();
        NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(buildPlayer.getName());

        for (BuildVoteType vote : BuildVoteType.values()) {
            buildPlayer.getPlayer().getInventory().setItem((vote.getValue()/10)-1, ItemBuilder.from(Material.STAINED_CLAY)
                    .damage(vote.getDurability())
                    .displayName(translator.getLegacyText(noobPlayer, "build-battle.arena.items.vote.options."+ vote.toString().toLowerCase()))
                    .metadata("event", "vote")
                    .metadata("vote", vote+ "")
                    .lore(translator.getLegacyTextList(noobPlayer, "build-battle.arena.items.vote.options.lore"))
                    .build()
            );
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        NBTTagHelper nbtHelper = SpigotCore.getNBTTagHelper();

        if(nbtHelper.hasKey(event.getItem(), "event") && nbtHelper.getValue(event.getItem(), "event").equals("vote")) {
            NoobPlayer noobPlayer = Core.getPlayerCache().getPlayer(event.getPlayer().getName());
            BuildGame game = BuildBattleAPI.getGameManager().getGame();
            BuildVoteType vote = BuildVoteType.valueOf(nbtHelper.getValue(event.getItem(), "vote"));

            if (game == null) return;
            BuildArena buildArena = BuildBattleAPI.getArenaManager().getByServer(game.getServer());

            if (!buildArena.isEnabled() && !buildArena.getStatus().equals(GameStatus.BUILDING_VOTING)) return;
            if (game.getCurrentRegion() == null) return;

            if (game.getCurrentRegion().getOwners().contains(event.getPlayer()))
                Logger.player(noobPlayer, "build-battle.arena.messages.vote-own-construction");

            else if (game.getCurrentRegion().getRegionVotes().addVote(event.getPlayer().getName(), vote.getValue()))
                Logger.player(noobPlayer, "build-battle.arena.messages.add-vote",
                      event.getPlayer().getItemInHand().getItemMeta().getDisplayName());
            event.setCancelled(true);
        }
    }
}
