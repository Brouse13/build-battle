package es.noobcraft.buildbattle.game.player;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.rank.Rank;
import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.buildbattle.configuration.Yaml;
import es.noobcraft.core.api.Core;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BuildPlayerImpl implements BuildPlayer {
    @Getter
    private final String name;
    @Getter
    private int experience;
    @Getter
    private int victories;
    @Getter
    private int defeats;
    @Getter
    private final int gamesPlayed;
    @Getter
    private int victoriesStrike;

    private Rank rank;

    public BuildPlayerImpl(String name, int experience, int victories, int defeats, int gamesPlayed, int victoriesStrike) {
        this.name = name;
        this.experience = experience;
        this.victories = victories;
        this.defeats = defeats;
        this.gamesPlayed = gamesPlayed;
        this.victoriesStrike = victoriesStrike;
        this.rank = getRank();
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(name);
    }

    @Override
    public void setExperience(int amount) {
        this.experience = amount;
    }

    @Override
    public void setVictories(int amount) {
        this.victories = amount;
    }

    @Override
    public void setDefeats(int amount) {
        this.defeats = amount;
    }

    @Override
    public void setGamesPlayed(int amount) {
        this.victoriesStrike = amount;
    }

    @Override
    public void setVictoriesStrike(int amount) {
        this.victoriesStrike = amount;
    }

    @Override
    public void setRank(Rank rank) {
        this.rank = rank;
    }

    @Override
    public Rank getRank() {
        BuildBattleAPI.getRankManager().updateRank(this);
        return rank;
    }

    @Override
    public void returnPlayer() {
        Player player = getPlayer();
        Yaml yml = new Yaml("config", false);
        player.closeInventory();
        player.getInventory().clear();
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        Core.getPlayerCache().getPlayer(player.getPlayer().getName()).connect(yml.getFile().getString("settings.lobby"));
    }
}
