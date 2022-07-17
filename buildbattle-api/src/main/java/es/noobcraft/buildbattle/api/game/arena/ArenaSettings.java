package es.noobcraft.buildbattle.api.game.arena;

public interface ArenaSettings {
    /**
     * Get the max players that can be playing
     * on this game at the same time
     * @return the game max players
     */
    int getMaxPlayers();

    /**
     * Get the time that the game will be
     * doing countdown
     * @return the game countdown duration
     */
    int getCountdown();

    /**
     * Get the time that the game will be
     * in votingTheme stage
     * @return the game vote theme duration
     */
    int getVoteTheme();

    /**
     * Get the time that the game will last
     * @return the game duration
     */
    int getGameDuration();

    /**
     * Get the time that the game will be
     * voting each player construction
     * @return the game vote construction duration
     */
    int getVoteConstruction();

    /**
     * Get the time that the game will
     * wait until the players are returned
     * to the lobby
     * @return the game stop duration
     */
    int getStopTime();

    /**
     * Get the arena size without counting the borders
     * @return the arena size
     */
    int getArenaSize();

    /**
     * Get the arena border size in each direction
     * @return the arena border size
     */
    int getBorderSize();
}
