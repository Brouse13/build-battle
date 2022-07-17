package es.noobcraft.buildbattle.game.gui.theme;

import es.noobcraft.buildbattle.api.game.themes.GameTheme;
import es.noobcraft.buildbattle.api.game.themes.Theme;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BaseGameTheme implements GameTheme {
    private final HashMap<Theme, Integer> themes = new HashMap<>();
    private final HashMap<String, Theme> playerVotes = new HashMap<>();
    private Theme winner = null;

    public BaseGameTheme(List<Theme> themes) {
        for (Theme theme : themes)
            this.themes.put(theme, 0);
    }

    @Override
    public List<Theme> getThemes() {
        return new ArrayList<>(themes.keySet());
    }

    @Override
    public void addVote(Theme theme, Player player) {
        if (playerVotes.containsKey(player.getName()))
            if (!playerVotes.get(player.getName()).equals(theme)) {
                themes.replace(playerVotes.get(player.getName()),
                        themes.get(playerVotes.get(player.getName())) - 1);
                themes.replace(theme, themes.get(theme) + 1);
                playerVotes.replace(player.getName(), theme);
                return;
            }
        themes.replace(theme, themes.get(theme)+ 1);
        playerVotes.put(player.getName(), theme);
    }

    @Override
    public void removeVote(Player player) {
        final Theme remove = playerVotes.remove(player.getName());
        if (remove  != null) themes.computeIfPresent(remove, (theme, i) -> i-1);
    }

    @Override
    public int getVotes(Theme theme) {
        return themes.get(theme);
    }

    @Override
    public Theme getMostVoted() {
        return playerVotes.values().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);
    }

    @Override
    public Theme getWinner() {
        if (winner != null) return winner;
        final Theme mostVoted = getMostVoted();
        this.winner = mostVoted == null ? themes.entrySet().iterator().next().getKey() : mostVoted;
        return winner;
    }

    @Override
    public String getPercentage(Theme theme) {
        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        if (playerVotes.size() == 0) return "0";
        return decimalFormat.format((getVotes(theme) / themes.size()) * 100L);
    }
}
