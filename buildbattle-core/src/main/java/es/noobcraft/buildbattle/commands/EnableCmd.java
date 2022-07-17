package es.noobcraft.buildbattle.commands;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.configuration.Yaml;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import lombok.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EnableCmd implements PlayerCommand {
    @Override
    public @NonNull String[] getAliases() {
        return new String[]{"enable"};
    }

    @Override
    public @NonNull Set<Group> getGroups() {
        return new HashSet<>(Arrays.asList(Group.CO_CREATOR));
    }

    @Override
    public void run(@NonNull BukkitNoobPlayer player, @NonNull String label, @NonNull String[] args) {
        if (args.length < 1) {
            Logger.player(player, "build-battle.arena.commands.enable.usage");
            return;
        }
        BuildArena arena = BuildBattleAPI.getArenaManager().getByServer(args[0]);

        if (arena == null) {
            player.sendMessage("Arena not found");
            return;
        }
        Yaml yml = new Yaml("arenas/"+ arena.getName()+ "/arena", false);
        yml.getFile().set("arenas.enabled", true);
        yml.saveFile();
        arena.setEnabled(true);
        Logger.player(player, "build-battle.arena.commands.enable.enabling",
                args[0]);
    }
}
