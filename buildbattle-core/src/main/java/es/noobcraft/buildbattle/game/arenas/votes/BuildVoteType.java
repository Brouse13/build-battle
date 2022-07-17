package es.noobcraft.buildbattle.game.arenas.votes;

import lombok.Getter;

public enum BuildVoteType {
    SUPER_POOP(10, 14),
    POOP(20, 12),
    MINI_POOP(30, 7),
    MEH(40, 4),
    OKAY( 50, 5),
    GOOD(60, 13),
    REALLY_GOOD(70, 9),
    HYPE(80, 3),
    LEGENDARY(90, 11);

    @Getter
    private final int value;
    @Getter
    private final int durability;

    BuildVoteType(int value, int durability) {
        this.value = value;
        this.durability = durability;
    }
}
