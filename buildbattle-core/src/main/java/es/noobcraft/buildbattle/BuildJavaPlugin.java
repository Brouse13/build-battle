package es.noobcraft.buildbattle;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.ServerType;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.buildbattle.logger.LoggerType;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.command.PlayerCommand;
import lombok.NonNull;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public abstract class BuildJavaPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        BuildBattleAPI.getArenaListener().updateData(this);

        BuildBattleAPI.getRankManager().loadRanks();
        Logger.log(LoggerType.CONSOLE, "Api loaded successfully");

        enable();
        loadCommand().forEach(command -> SpigotCore.getCommandManager().add(command));
        registerListeners().forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    @Override
    public void onLoad() {
        new Logger(this);
        BuildBattleAPI.setBuildBattle(new API(getServerType()));

        Core.getRedisClient().subscribe("build-battle", BuildBattleAPI.getArenaListener());
        Logger.log(LoggerType.CONSOLE, "Using  "+  BuildBattleAPI.getArenaListener().getClass().getSimpleName()+ " as controller");
    }

    public abstract void enable();

    @Override
    public abstract void onDisable();

    @NonNull
    public abstract ServerType getServerType();

    public abstract Set<PlayerCommand> loadCommand();

    public abstract Set<Listener> registerListeners();
}
