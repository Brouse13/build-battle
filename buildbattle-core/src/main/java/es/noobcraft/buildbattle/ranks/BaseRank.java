package es.noobcraft.buildbattle.ranks;

import es.noobcraft.buildbattle.api.game.rank.Rank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BaseRank implements Rank {
    @Getter
    String name;
    @Getter
    int experience;
}
