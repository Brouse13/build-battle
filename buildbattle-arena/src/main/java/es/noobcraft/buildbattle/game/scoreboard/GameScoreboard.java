package es.noobcraft.buildbattle.game.scoreboard;

import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.scoreboard.BuildScoreboard;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.core.api.scoreboard.ScoreBoard;

public class GameScoreboard implements BuildScoreboard {
    private final Translator translator = Core.getTranslator();
    private ScoreBoard scoreBoard;

    private final NoobPlayer noobPlayer;
    private final BuildGame game;

    public GameScoreboard(BuildGame game, NoobPlayer noobPlayer) {
        this.game = game;
        this.noobPlayer = noobPlayer;
        for (ScoreBoard loadedScoreboard : SpigotCore.getScoreBoardManager().getScoreBoards()) {
            if (loadedScoreboard.getPlayer().getUsername().equals(noobPlayer.getUsername()))
                this.scoreBoard = loadedScoreboard;
        }

        if (scoreBoard == null) this.scoreBoard = SpigotCore.getScoreBoardManager().createScoreBoard(noobPlayer);
    }

    @Override
    public void update() {
        this.scoreBoard.setTitle(translator.getLegacyText(noobPlayer, "build-battle.arena.scoreboard.game.title"));
        this.scoreBoard.set(translator.getLegacyTextList(noobPlayer, "build-battle.arena.scoreboard.game.content",
                game.getTime(),
                game.getGameTheme().getWinner().getTranslate(noobPlayer),
                game.getPlayers().size(),
                game.teamMode() ? translator.getLegacyText(noobPlayer, "build-battle.arena.scoreboard.team") :
                        translator.getLegacyText(noobPlayer, "build-battle.arena.scoreboard.solo")
        ));
    }
}
