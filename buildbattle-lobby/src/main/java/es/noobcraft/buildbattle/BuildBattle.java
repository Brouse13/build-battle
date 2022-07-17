package es.noobcraft.buildbattle;

import es.noobcraft.buildbattle.api.ServerType;
import es.noobcraft.buildbattle.commands.*;
import es.noobcraft.buildbattle.hologram.HashSetTopHologram;
import es.noobcraft.buildbattle.listeners.LobbyListeners;
import es.noobcraft.buildbattle.listeners.LoginListeners;
import es.noobcraft.buildbattle.listeners.SignListeners;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.buildbattle.logger.LoggerType;
import es.noobcraft.buildbattle.signs.SignLoader;
import es.noobcraft.buildbattle.signs.SignUpdater;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.command.PlayerCommand;
import es.noobcraft.core.api.register.ConnectMode;
import es.noobcraft.core.api.register.ConstantSupplier;
import es.noobcraft.core.api.register.PropertyConstants;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BuildBattle extends BuildJavaPlugin {
    @Override
    public void enable() {
        registerServer();
        SignLoader.loadSigns();
        SignUpdater.update(this);
        new HashSetTopHologram(this).update();
        Logger.log(LoggerType.CONSOLE, "Enabling buildBattle lobby module");
    }

    @Override
    public void onDisable() {
        SignLoader.saveSigns();
    }

    @Override
    public @NonNull ServerType getServerType() {
        return ServerType.LOBBY;
    }

    public Set<Listener> registerListeners() {
        return new HashSet<>(Arrays.asList(new SignListeners(), new LobbyListeners(this),
                new LoginListeners()));
    }

    @Override
    public Set<PlayerCommand> loadCommand() {
        return new HashSet<>(Arrays.asList(new DisableCmd(), new EnableCmd(),
                new RankCmd(), new SetSpawnCmd(), new SpectateCmd()));
    }

    private void registerServer() {
        Core.getServerRegistryManager().registryProperty(PropertyConstants.GAME_MODE_PROPERTY_SPECIAL,
                new ConstantSupplier<>(Boolean.FALSE.toString()));
        Core.getServerRegistryManager().registryProperty(PropertyConstants.GAME_MODE_PROPERTY_CONNECT_MODE,
                new ConstantSupplier<>(ConnectMode.SELECTION.toString()));
        Core.getServerRegistryManager().registryProperty(PropertyConstants.GAME_MODE_ITEM_PROPERTY_MATERIAL,
                new ConstantSupplier<>(Material.GRASS.toString()));
        Core.getServerRegistryManager().registryProperty(PropertyConstants.GAME_MODE_ITEM_PROPERTY_AMOUNT,
                new ConstantSupplier<>("1"));
        Core.getServerRegistryManager().registryProperty(PropertyConstants.GAME_MODE_ITEM_PROPERTY_DAMAGE,
                new ConstantSupplier<>("0"));
        Core.getServerRegistryManager().registryProperty(PropertyConstants.GAME_MODE_SERVER_PROPERTY_SUPPORTED_VERSIONS,
                new ConstantSupplier<>("1.8.8 - 1.16.5"));
        Core.getServerRegistryManager().registryProperty(PropertyConstants.GAME_MODE_SERVER_PROPERTY_PLAYERS_ONLINE,
                () -> getServer().getOnlinePlayers().size());
        Core.getServerRegistryManager().registryProperty(PropertyConstants.GAME_MODE_SERVER_PROPERTY_PLAYERS_LIMIT,
                () -> Bukkit.getServer().getMaxPlayers());
        Core.getServerRegistryManager().registryProperty(PropertyConstants.SERVER_PROPERTY_RESTRICTED,
                new ConstantSupplier<>(Boolean.FALSE.toString()));
        Core.getServerRegistryManager().registryProperty(PropertyConstants.SERVER_PROPERTY_ALLOWED_GROUPS,
                new ConstantSupplier<>(Integer.toString(-1)));
    }
}
