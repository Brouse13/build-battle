package es.noobcraft.buildbattle.api.game;

import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;

public interface GameManager {
    /**
     * Create a new game on a given server
     * @param server server name
     * @return the created arena
     */
    BuildGame createGame(String server);

    /**
     * Delete the game that it's currently running
     */
    void deleteGame();

    /**
     * Get the game that it's running currently
     * running it can be null if there's no game
     * running
     * @return the game
     */
    BuildGame getGame();

    /**
     * Get the region that have won the game
     * @param game game from where search
     * @return the winner region
     */
    CuboidRegion getWinner(BuildGame game);
}
