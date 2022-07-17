package es.noobcraft.buildbattle.commands;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.configuration.Yaml;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import lombok.NonNull;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SpawnCmd implements PlayerCommand {
    @Override
    public @NonNull String[] getAliases() {
        return new String[]{"spawn"};
    }

    @Override
    public @NonNull Set<Group> getGroups() {
        return new HashSet<>(Arrays.asList(Group.CO_CREATOR));
    }

    @Override
    public void run(@NonNull BukkitNoobPlayer player, @NonNull String label, @NonNull String[] args) {
        for (BuildArena arena : BuildBattleAPI.getArenaManager().getArenas()) {
            if (arena.getServer().equals(Core.getServerId())) {
                if (arena.isEnabled()) {
                    player.sendMessage("Arena must be disabled first");
                    return;
                }
                arena.setArenaLobby(player.getLocation());
                Yaml yml = new Yaml("arenas/"+ arena.getName()+ "/arena", false);
                DecimalFormat decimalFormat = new DecimalFormat("###.##");

                yml.getFile().set("arenas.spawn.world", player.getLocation().getWorld().getName());
                yml.getFile().set("arenas.spawn.x", decimalFormat.format(player.getLocation().getX()));
                yml.getFile().set("arenas.spawn.y", decimalFormat.format(player.getLocation().getY()));
                yml.getFile().set("arenas.spawn.z", decimalFormat.format(player.getLocation().getZ()));
                yml.saveFile();
                player.sendMessage("Spawnpoint set successfully");
                return;
            }
        }
        player.sendMessage("Couldn't find arena in server "+ Core.getServerId());
    }
}
