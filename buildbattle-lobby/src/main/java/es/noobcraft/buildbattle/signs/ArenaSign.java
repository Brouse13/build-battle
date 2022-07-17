package es.noobcraft.buildbattle.signs;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Sign;

public class ArenaSign {
    @Getter
    private final Location location;
    @Getter
    private final String server;
    @Getter
    private final Sign sign;

    public ArenaSign(String server, Location location) {
        this.location = location;
        this.server = server;
        this.sign = (Sign) location.getBlock().getState();
    }
}
