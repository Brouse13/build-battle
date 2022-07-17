package es.noobcraft.buildbattle.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BuildSignCreateEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    @Getter @Setter
    private boolean cancelled;

    public BuildSignCreateEvent() {
        this.cancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
