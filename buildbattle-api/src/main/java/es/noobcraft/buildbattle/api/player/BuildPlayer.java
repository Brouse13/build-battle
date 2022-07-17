package es.noobcraft.buildbattle.api.player;

import es.noobcraft.buildbattle.api.game.rank.Rank;
import org.bukkit.entity.Player;

public interface BuildPlayer {
    /**
     * Get the BukkitPlayer associated to the
     * current BuildPlayer. It can be null if player is still
     * connecting
     * @return the BukkitPlayer
     */
    Player getPlayer();

    /**
     * Get the name of the BuildPlayer
     * @return the player name
     */
    String getName();

    /**
     * Represents the amount of experience that
     * the player has won on the BuildBattle
     * @return the player amount exp
     */
    int getExperience();

    /**
     * Set the player experience to a concrete amount
     * @param amount player new experience
     */
    void setExperience(int amount);

    /**
     * Get the amount of games that the
     * player has won on BuildBattle
     * @return the player victories
     */
    int getVictories();

    /**
     * Set the player victories to a concrete amount
     * @param amount player new victories
     */
    void setVictories(int amount);

    /**
     * Get the amount of games that the
     * player has defeated on BuildBattle
     * @return the player defeats
     */
    int getDefeats();

    /**
     * Set the player defeats to a concrete amount
     * @param amount player new defeats
     */
    void setDefeats(int amount);

    /**
     * Get the amount of games that the
     * player has won without loosing.
     * When the player loose the strike is set to 0
     * @return the victories strike
     */
    int getVictoriesStrike();

    /**
     * Set the consecutive victories that the player has
     * @param amount player new victoriesStrike
     */
    void setVictoriesStrike(int amount);

    /**
     * Get the amount of games that the
     * player has played on BuildBattle
     * @return the amount of games played
     */
    int getGamesPlayed();

    /**
     * Set the amount of games that the player has played
     * @param amount player new gamesPlayed
     */
    void setGamesPlayed(int amount);

    /**
     * Get the player current Rank
     * depending on the amount of experience
     * @return the player current Rank
     */
    Rank getRank();

    /**
     * Set the player rank to a new value
     * @param rank player new rank
     */
    void setRank(Rank rank);

    /**
     * Return the player to the BuildBattle lobby
     * when the game has finished
     */
    void returnPlayer();
}
