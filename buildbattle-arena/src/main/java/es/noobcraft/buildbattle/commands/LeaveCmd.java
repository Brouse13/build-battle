package es.noobcraft.buildbattle.commands;

import es.noobcraft.buildbattle.configuration.Yaml;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.permission.Group;
import es.noobcraft.core.api.player.BukkitNoobPlayer;
import lombok.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LeaveCmd implements PlayerCommand {
    @Override
    public @NonNull String[] getAliases() {
        return new String[]{"leave"};
    }

    @Override
    public @NonNull Set<Group> getGroups() {
        return new HashSet<>(Arrays.asList(Group.HELPER));
    }

    @Override
    public void run(@NonNull BukkitNoobPlayer player, @NonNull String label, @NonNull String[] args) {
        Yaml yml = new Yaml("config", false);
        player.connect(yml.getFile().getString("settings.lobby"));
    }
}
