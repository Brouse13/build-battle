package es.noobcraft.buildbattle.api.game.rank;

public interface Rank {
    /**
     * Get the name of this rank
     * @return the name of the rank
     */
    String getName();

    /**
     * Get the player experience that
     * the player needs to get this rank.
     * @return the rank experience
     */
    int getExperience();
}
