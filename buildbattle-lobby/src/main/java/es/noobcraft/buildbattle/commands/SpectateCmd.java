package es.noobcraft.buildbattle.commands;

import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import es.noobcraft.core.api.player.OfflineNoobPlayer;
import lombok.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SpectateCmd implements PlayerCommand {
    @Override
    public @NonNull String[] getAliases() {
        return new String[]{"spectate", "spect"};
    }

    @Override
    public @NonNull Set<Group> getGroups() {
        return new HashSet<>(Arrays.asList(Group.HELPER));
    }

    @Override
    public void run(@NonNull BukkitNoobPlayer player, @NonNull String label, @NonNull String[] args) {
        if (args.length < 1) {
            Logger.player(player, "");
            return;
        }

        final OfflineNoobPlayer target = Core.getPlayerSupplier().createOfflineNoobPlayer(args[0]);
        final Optional<String> server = Core.getOnlineManager().getServerName(target);

        if (!server.isPresent()) {
            Logger.player(player,"build-battle.lobby.commands.spectate.not-found", args[0]);
            return;
        }

        server.ifPresent(playerServer -> {
            Logger.player(player, "build-battle.lobby.commands.spectate.found", args[0]);
            player.connect(playerServer);
        });
    }
}
