package es.noobcraft.buildbattle.api.game.arena.region.options;

import lombok.Getter;

public enum RegionTime {
    DAY(1000),
    NOON(6000),
    SUNSET(12000),
    NIGHT(13000),
    MIDNIGHT(18000),
    SUNRISE(23000);

    @Getter private final int duration;

    RegionTime(int duration) {
        this.duration = duration;
    }
}
