package es.noobcraft.buildbattle.hologram;

import es.noobcraft.core.api.player.BukkitNoobPlayer;
import es.noobcraft.holographicdisplays.api.Hologram;
import es.noobcraft.holographicdisplays.api.line.TextLine;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public final class PlayerHologram {
    private final BukkitNoobPlayer player;
    private final Hologram hologram;
    private final List<TextLine> lines;
}
