package dixu.deckard.server;

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
