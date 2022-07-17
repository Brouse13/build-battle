package es.noobcraft.buildbattle.commands;

import es.noobcraft.buildbattle.configuration.Yaml;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import lombok.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SetSpawnCmd implements PlayerCommand {

    @Override
    public @NonNull String[] getAliases() {
        return new String[]{"setspawn"};
    }

    @Override
    public @NonNull Set<Group> getGroups() {
        return new HashSet<>(Arrays.asList(Group.CO_CREATOR));
    }

    @Override
    public void run(@NonNull BukkitNoobPlayer player, @NonNull String label, @NonNull String[] args) {
        Yaml yml = new Yaml("config", false);

        yml.getFile().set("spawn.world", player.getEyeLocation().getWorld().getName());
        yml.getFile().set("spawn.x", player.getEyeLocation().getX());
        yml.getFile().set("spawn.y", player.getEyeLocation().getY());
        yml.getFile().set("spawn.z", player.getEyeLocation().getZ());
        yml.getFile().set("spawn.yaw", player.getEyeLocation().getYaw());
        yml.getFile().set("spawn.pitch", player.getEyeLocation().getPitch());
        yml.saveFile();
        Logger.player(player, "build-battle.lobby.commands.setspawn");
    }

}
