package es.noobcraft.buildbattle.api.event;

import es.noobcraft.buildbattle.api.game.BuildGame;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BuildGameEvent extends Event {
    /**
     * Store all the handlers on a BukkitEvent
     */
    public static final HandlerList handlers = new HandlerList();

    /**
     * Represent the game that has thrown the event
     * This game is final and can't be modified
     */
    @Getter private final BuildGame game;

    public BuildGameEvent(BuildGame game) {
        this.game = game;
    }

    /**
     * Inherit from BukkitEvent
     * Manage all the handlers from the BukkitEvent
     * @return a list with all the handler
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Inherit from BukkitEvent
     * Manage all the handlers from the BukkitEvent
     * @return a list with all the handler in a static way
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
