package dixu.deckard.server.team;

import dixu.deckard.server.game.GameParams;
import dixu.deckard.server.leader.LeaderType;
import dixu.deckard.server.minion.Minion;

import java.util.stream.IntStream;

public class TeamFactory {

    public Team create(LeaderType type) {
        return new Team(IntStream.range(0, GameParams.MINION_PER_TEAM)
                .boxed()
                .map(num -> new Minion(type))
                .toList()
        );
    }
}
