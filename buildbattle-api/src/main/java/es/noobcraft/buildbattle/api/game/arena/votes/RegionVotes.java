package es.noobcraft.buildbattle.api.game.arena.votes;

import java.util.Map;

public interface RegionVotes {
    /**
     * Get a map with all the players that
     * have voted on the region
     *
     * Params: Player name, Vote value
     *
     * @return map of all current votes
     */
    Map<String, Integer> getVotes();

    /**
     * Add the player vote to the region values
     * @param name name of the voter
     * @param value value of the vote
     * @return if the operation succeed
     */
    boolean addVote(String name, int value);

    /**
     * Get the total score of this region, that is
     * used to determine what region has won
     * @return the region score
     */
    int getTotalScore();
}
