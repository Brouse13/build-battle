package es.noobcraft.buildbattle.api.game.themes;

import org.bukkit.entity.Player;

import java.util.List;

public interface GameTheme {
    /**
     * Represents all the current themes
     * that are available for this game
     * @return all the game themes
     */
    List<Theme> getThemes();

    /**
     * Add or change a player vote
     * @param theme vote to add
     * @param player player who voted
     */
    void addVote(Theme theme, Player player);

    /**
     * Remove a vote from the list of all
     * the themes voted
     * @param player player to remove vote
     */
    void removeVote(Player player);

    /**
     * Get the amount of players that has voted
     * this theme
     * @param theme theme to get votes
     * @return the amount of votes
     */
    int getVotes(Theme theme);

    /**
     * Get the theme that has been most voted.
     * If theres a draft between two themes, it
     * must return one random.
     *
     * Every time it's called can return a new
     * theme
     * @return the most voted theme
     */
    Theme getMostVoted();

    /**
     * Get the winner theme from all the
     * voted themes
     *
     * Every time it's called must return the same
     * theme
     * @return the winner theme
     */
    Theme getWinner();

    /**
     * Returns the percentage of players
     * who has voted this theme
     * Ex: 30%
     * @param theme theme to get percentage
     * @return the theme percentage
     */
    String getPercentage(Theme theme);
}
