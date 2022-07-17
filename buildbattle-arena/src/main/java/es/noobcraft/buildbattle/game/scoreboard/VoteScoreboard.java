package es.noobcraft.buildbattle.game.scoreboard;

import es.noobcraft.buildbattle.api.BuildBattleAPI;
import es.noobcraft.buildbattle.api.game.BuildGame;
import es.noobcraft.buildbattle.api.game.arena.BuildArena;
import es.noobcraft.buildbattle.api.game.arena.region.CuboidRegion;
import es.noobcraft.buildbattle.api.scoreboard.BuildScoreboard;
import es.noobcraft.buildbattle.game.arenas.votes.BuildVoteType;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.SpigotCore;
import es.noobcraft.core.api.lang.Translator;
import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.core.api.scoreboard.ScoreBoard;

public class VoteScoreboard implements BuildScoreboard {
    private final Translator translator = Core.getTranslator();
    private ScoreBoard scoreBoard;

    private final NoobPlayer noobPlayer;
    private final BuildGame game;
    private final CuboidRegion region;

    public VoteScoreboard(BuildGame buildGame, CuboidRegion region, NoobPlayer noobPlayer) {
        this.game = buildGame;
        this.region = region;
        this.noobPlayer = noobPlayer;
        for (ScoreBoard loadedScoreboard : SpigotCore.getScoreBoardManager().getScoreBoards()) {
            if (loadedScoreboard.getPlayer().getUsername().equals(noobPlayer.getUsername()))
                this.scoreBoard = loadedScoreboard;
        }

        if (scoreBoard == null) this.scoreBoard = SpigotCore.getScoreBoardManager().createScoreBoard(noobPlayer);
    }

    @Override
    public void update() {
        this.scoreBoard.setTitle(translator.getLegacyText(noobPlayer, "build-battle.arena.scoreboard.vote.title"));
        BuildArena buildArena = BuildBattleAPI.getArenaManager().getByServer(game.getServer());
        this.scoreBoard.set(translator.getLegacyTextList(noobPlayer, "build-battle.arena.scoreboard.vote.content",
                getVote(region),
                game.getPlayers().size(),
                buildArena.getArenaSettings().getMaxPlayers()
        ));
    }

    private String getVote(CuboidRegion region) {
        return region.getRegionVotes().getVotes().getOrDefault(noobPlayer.getUsername(), -1) == -1 ?
                translator.getLegacyText(noobPlayer, "build-battle.arena.scoreboard.no-voted") :
                translator.getLegacyText(noobPlayer, "build-battle.arena.items.vote.options."+
                        BuildVoteType.values()[(region.getRegionVotes().getVotes().get(noobPlayer.getUsername())/10)-1].toString().toLowerCase());
    }
}
