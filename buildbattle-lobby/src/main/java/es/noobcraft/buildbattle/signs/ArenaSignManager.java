package es.noobcraft.buildbattle.signs;

import com.google.common.collect.ImmutableList;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArenaSignManager {
    private static final List<ArenaSign> signs = new ArrayList<>();

    public static List<ArenaSign> getSigns() {
        return ImmutableList.copyOf(signs);
    }

    public static void addSign(ArenaSign sign) {
        signs.add(sign);
    }

    public static void removeSign(ArenaSign sign) {
        signs.remove(sign);
    }

    public static void addAll(ArenaSign[] arenaSigns) {
        signs.addAll(Arrays.asList(arenaSigns));
    }

    public static ArenaSign getSign(Location location){
        return signs.stream().filter(sign -> sign.getLocation().equals(location)).findFirst().orElse(null);
    }
}
