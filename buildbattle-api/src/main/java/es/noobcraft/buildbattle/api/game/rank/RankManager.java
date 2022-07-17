package es.noobcraft.buildbattle.api.game.rank;

import es.noobcraft.buildbattle.api.player.BuildPlayer;

public interface RankManager {
    /**
     * Load all the ranks on the game lobby
     */
    void loadRanks();

    /**
     * Update the player rank
     * @param player player to update
     * @return the new player Rank
     */
    Rank updateRank(BuildPlayer player);
}
