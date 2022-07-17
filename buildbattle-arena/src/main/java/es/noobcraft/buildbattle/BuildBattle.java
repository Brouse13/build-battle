package es.noobcraft.buildbattle;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.ServerType;
import es.noobcraft.buildbattle.commands.*;
import es.noobcraft.buildbattle.game.gui.particles.ParticlesScheduler;
import es.noobcraft.buildbattle.game.inventories.GameInv;
import es.noobcraft.buildbattle.game.inventories.LobbyInv;
import es.noobcraft.buildbattle.game.inventories.VoteInv;
import es.noobcraft.buildbattle.listeners.BuildListeners;
import es.noobcraft.buildbattle.listeners.GriffinListeners;
import es.noobcraft.buildbattle.listeners.LoginListeners;
import es.noobcraft.buildbattle.listeners.ScheduleListeners;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.buildbattle.logger.LoggerType;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.command.PlayerCommand;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BuildBattle extends BuildJavaPlugin {

    @Override
    public void enable() {
        BuildBattleAPI.getThemeManager().loadThemes();
        ParticlesScheduler.start(this);
        Logger.log(LoggerType.CONSOLE, "Enabling BuildBattle arena module");
        Bukkit.getScheduler().runTaskLater(this, () -> BuildBattleAPI.getArenaRegister().register(Core.getServerId()), 10L);
    }

    @Override
    public void onDisable() {
        BuildBattleAPI.getArenaRegister().delete(Core.getServerId());
    }

    @Override
    public @NonNull ServerType getServerType() {
        return ServerType.ARENA;
    }

    @Override
    public Set<Listener> registerListeners() {
        return new HashSet<>(Arrays.asList(new LoginListeners(this), new GriffinListeners(), new BuildListeners(),
                new ScheduleListeners(this), new VoteInv(), new GameInv(), new LobbyInv()));
    }

    @Override
    public Set<PlayerCommand> loadCommand() {
        return new HashSet<>(Arrays.asList(new DisableCmd(), new EnableCmd(), new RankCmd(), new SpawnCmd(), new LeaveCmd()));
    }
}
