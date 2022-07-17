package es.noobcraft.buildbattle.api.scoreboard;

import com.google.common.collect.Maps;
import es.noobcraft.buildbattle.api.player.BuildPlayer;

import java.util.HashMap;

public class ScoreboardManager {
    private static final HashMap<BuildPlayer, BuildScoreboard> playerScoreboards = Maps.newHashMap();

    /**
     * Create a new scoreboard to the player and set it a BuildScoreboard
     * @param player player to set the scoreboard
     * @param buildScoreboard scoreboard to add
     */
    public static void create(BuildPlayer player, BuildScoreboard buildScoreboard) {
        playerScoreboards.put(player, buildScoreboard);
        buildScoreboard.update();
    }

    public static void remove(BuildPlayer buildPlayer) {
        playerScoreboards.remove(buildPlayer);
    }

    public static void clear() {
        playerScoreboards.clear();
    }

    public static void update() {
        playerScoreboards.values().forEach(BuildScoreboard::update);
    }
}
