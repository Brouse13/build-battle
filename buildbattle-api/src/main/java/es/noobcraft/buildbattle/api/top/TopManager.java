package es.noobcraft.buildbattle.api.top;

import java.util.Map;

public interface TopManager {
    /**
     * Get a map with the amount of player on the specific top
     * @param amount amount of players
     * @param type type of top
     * @return top games won (Player name, Amount)
     */
    Map<String, Integer> getTop(int amount, String type);
}