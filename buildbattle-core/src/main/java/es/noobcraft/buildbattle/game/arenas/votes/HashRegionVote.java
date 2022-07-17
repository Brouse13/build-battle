package es.noobcraft.buildbattle.game.arenas.votes;

import es.noobcraft.buildbattle.api.game.arena.votes.RegionVotes;
import lombok.Getter;

import java.util.HashMap;

public class HashRegionVote implements RegionVotes {
    @Getter
    private final HashMap<String, Integer> votes;

    public HashRegionVote() {
        votes = new HashMap<>();
    }

    @Override
    public boolean addVote(String name, int value) {
        if (votes.containsKey(name)) {
            if (votes.get(name) == value)
                return false;
            votes.replace(name, value);
        }
        votes.put(name, value);
        return true;
    }

    @Override
    public int getTotalScore() {
        int sum = 0;
        for (Integer value : getVotes().values()) {
            sum+=value;
        }
        return sum;
    }
}
