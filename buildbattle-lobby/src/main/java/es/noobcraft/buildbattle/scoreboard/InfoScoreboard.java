package es.noobcraft.buildbattle.scoreboard;

import es.noobcraft.buildbattle.api.player.BuildPlayer;
import es.noobcraft.buildbattle.api.scoreboard.BuildScoreboard;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.core.api.scoreboard.ScoreBoard;

public class InfoScoreboard implements BuildScoreboard {
    private final Translator translator = Core.getTranslator();
    private ScoreBoard scoreBoard;

    private final BuildPlayer buildPlayer;
    private final NoobPlayer noobPlayer;

    public InfoScoreboard(BuildPlayer buildPlayer) {
        this.buildPlayer = buildPlayer;
        this.noobPlayer = Core.getPlayerCache().getPlayer(buildPlayer.getName());

        for (ScoreBoard loadedScoreboard : SpigotCore.getScoreBoardManager().getScoreBoards()) {
            if (loadedScoreboard.getPlayer().getUsername().equals(noobPlayer.getUsername()))
                this.scoreBoard = loadedScoreboard;
        }

        if (scoreBoard == null) this.scoreBoard = SpigotCore.getScoreBoardManager().createScoreBoard(noobPlayer);
    }

    @Override
    public void update() {
        this.scoreBoard.setTitle(translator.getLegacyText(noobPlayer, "build-battle.lobby.scoreboard.stats.title"));
        this.scoreBoard.set(translator.getLegacyTextList(noobPlayer, "build-battle.lobby.scoreboard.stats.content",
                buildPlayer.getName(),
                buildPlayer.getRank().getName(),
                buildPlayer.getVictories(),
                buildPlayer.getVictoriesStrike()
        ));
    }
}
