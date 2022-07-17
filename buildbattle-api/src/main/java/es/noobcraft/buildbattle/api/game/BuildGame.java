package es.noobcraft.buildbattle.api.game;

import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import es.noobcraft.buildbattle.api.game.themes.GameTheme;
import es.noobcraft.buildbattle.api.player.BuildPlayer;

import java.util.List;

public interface BuildGame {
    String getServer();

    /**
     * Represents the theme handler that is used
     * to manage the themes, winner theme...
     * @return the gameTheme handler
     */
    GameTheme getGameTheme();

    /**
     * Set the gameTheme handler
     * @param gameTheme theme to set
     */
    void setGameTheme(GameTheme gameTheme);

    /**
     * Represent the game scheduler that is
     * used to run and stop the game
     * @return the game scheduler
     */
    GameScheduler getScheduler();

    /**
     * Set the GameScheduler to this game
     * if this is unset, the game wont run
     * @param scheduler scheduler to set
     */
    void setScheduler(GameScheduler scheduler);

    /**
     * Get the list of all the available regions
     * for this arena, there are calculated with
     * a ArenaSetting
     * @return all the available regions
     */
    List<CuboidRegion> getBuildSpawns();

    /**
     * Represents if the current game
     * has team mode enabled
     * @return if the game support teams
     */
    boolean teamMode();

    /**
     * Get a list of all the current spectators
     * on this game
     * @return the game player list
     */
    List<BuildPlayer> getSpectators();

    /**
     * Add a new spectator to the game
     * @param player player to add
     */
    void addSpectator(BuildPlayer player);

    /**
     * Remove a spectators from the game
     * @param player player to add
     */
    void removeSpectator(BuildPlayer player);

    /**
     * Get a list of all the current players
     * on this game
     * @return the game player list
     */
    List<BuildPlayer> getPlayers();

    /**
     * Add a new player to the game
     * @param player player to add
     */
    void addPlayer(BuildPlayer player);

    /**
     * Remove a player from the game
     * @param player player to add
     */
    void removePlayer(BuildPlayer player);

    /**
     * Gets the current time of the game
     * @return game time
     */
    int getTime();

    /**
     * Set the game left time to a value,
     * it's used when the game countdown
     * is running
     * @param time new time
     */
    void setTime(int time);

    /**
     * Get the current region that's being
     * voted
     * @return the current region
     */
    CuboidRegion getCurrentRegion();

    /**
     * Set the current region that's being
     * voted
     */
    void setCurrentRegion(CuboidRegion region);

    /**
     * get if this game accept more players
     * @return if the game is full
     */
    boolean isFull();
}
