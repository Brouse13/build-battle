package es.noobcraft.buildbattle.commands;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.rank.Rank;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.HashSet;
import java.util.Set;

public class RankCmd implements PlayerCommand {
    @Override
    public @NonNull String[] getAliases() {
        return new String[]{"top"};
    }

    @Override
    public @NonNull Set<Group> getGroups() {
        Set<Group> groups = new HashSet<>();
        groups.add(Group.USER);
        return groups;
    }

    @SneakyThrows
    @Override
    public void run(@NonNull BukkitNoobPlayer player, @NonNull String label, @NonNull String[] args) {
        Rank rank = BuildBattleAPI.getRankManager().updateRank(BuildBattleAPI.getPlayerCache().getPlayer(player.getName()));
        Logger.player(player, "build-battle.arena.commands.rank.your-rank",
                rank.getName());

    }
}
