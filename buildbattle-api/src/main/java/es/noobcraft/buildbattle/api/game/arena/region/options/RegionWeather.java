package es.noobcraft.buildbattle.api.game.arena.region.options;

import org.bukkit.WeatherType;

public enum RegionWeather {
    SUN(WeatherType.CLEAR),
    RAIN(WeatherType.DOWNFALL),
    SNOW(WeatherType.DOWNFALL),
    THUNDER(WeatherType.DOWNFALL);

    private final WeatherType state;

    RegionWeather(WeatherType state) {
        this.state = state;
    }

    /**
     * Used to get the associated WeatherType on the WeatherRegion
     * @return the corresponding WeatherType
     */
    public WeatherType toWeatherType() {
        return this.state;
    }
}
