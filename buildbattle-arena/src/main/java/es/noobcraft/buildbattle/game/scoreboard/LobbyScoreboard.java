package es.noobcraft.buildbattle.game.scoreboard;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.scoreboard.BuildScoreboard;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.core.api.scoreboard.ScoreBoard;

public class LobbyScoreboard implements BuildScoreboard {
    private final Translator translator = Core.getTranslator();
    private ScoreBoard scoreBoard;

    private final NoobPlayer noobPlayer;
    private final BuildGame game;
    private final BuildArena buildArena;

    public LobbyScoreboard(BuildGame buildGame, NoobPlayer noobPlayer) {
        this.game = buildGame;
        this.noobPlayer = noobPlayer;
        this.buildArena = BuildBattleAPI.getArenaManager().getByServer(game.getServer());
        for (ScoreBoard loadedScoreboard : SpigotCore.getScoreBoardManager().getScoreBoards()) {
            if (loadedScoreboard.getPlayer().getUsername().equals(noobPlayer.getUsername()))
                this.scoreBoard = loadedScoreboard;
        }

        if (scoreBoard == null) this.scoreBoard = SpigotCore.getScoreBoardManager().createScoreBoard(noobPlayer);
    }

    @Override
    public void update() {
        this.scoreBoard.setTitle(translator.getLegacyText(noobPlayer, "build-battle.arena.scoreboard.lobby.title"));
        this.scoreBoard.set(translator.getLegacyTextList(noobPlayer, "build-battle.arena.scoreboard.lobby.content",
                game.getPlayers().size(),
                buildArena.getArenaSettings().getMaxPlayers()
        ));
    }
}
