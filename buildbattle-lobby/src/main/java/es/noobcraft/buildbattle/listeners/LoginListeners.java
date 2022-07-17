package es.noobcraft.buildbattle.listeners;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.core.api.event.NoobPlayerLoginEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.SQLException;

public class LoginListeners implements Listener {

    @EventHandler
    public void noobPlayerLogin(NoobPlayerLoginEvent event) throws SQLException {
        if (!event.getResult().equals(PlayerLoginEvent.Result.ALLOWED))
            return;

        if(!BuildBattleAPI.getPlayerCache().existPlayer(event.getPlayer().getName())) {
            BuildBattleAPI.getDatabase().createPlayer(event.getPlayer());
            BuildBattleAPI.getPlayerCache().loadPlayer(BuildBattleAPI.getDatabase().getPlayer(event.getPlayer()));
        }
    }
}
