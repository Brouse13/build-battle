package es.noobcraft.buildbattle.ranks;

import es.noobcraft.buildbattle.api.game.rank.Rank;
import es.noobcraft.buildbattle.api.game.rank.RankManager;
import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.buildbattle.configuration.Yaml;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.buildbattle.logger.LoggerType;

import java.util.ArrayList;
import java.util.List;

public class RankFactory implements RankManager {
    private static final List<Rank> ranks = new ArrayList<>();

    public void loadRanks() {
        Yaml yml = new Yaml("config", false);

        for (String rank : yml.getFile().getStringList("ranks"))
            ranks.add(new BaseRank(rank.split(",")[0],
                    Integer.parseInt(rank.split(",")[1])));
        Logger.log(LoggerType.CONSOLE, "Loaded "+ ranks.size()+ " ranks");
    }

    @Override
    public Rank updateRank(BuildPlayer player) {
        for (Rank rank : ranks)
            if (rank.getExperience() >= player.getExperience()) {
                player.setRank(rank);
                return rank;
            }
        player.setRank(ranks.get(ranks.size()-1));
        return ranks.get(ranks.size()-1);
    }
}
