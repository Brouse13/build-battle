package es.noobcraft.buildbattle.events;

import es.noobcraft.buildbattle.api.event.BuildGameEvent;
import es.noobcraft.buildbattle.api.game.BuildGame;

public class BuildGameStopEvent extends BuildGameEvent {

    public BuildGameStopEvent(BuildGame game) {
        super(game);
    }
}
