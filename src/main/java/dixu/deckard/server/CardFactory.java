package dixu.deckard.server;

import java.util.List;
import java.util.stream.IntStream;

public class CardFactory {

    public List<Card> createCards(int count, CardType type) {
        return IntStream.range(0, count)
                .boxed()
                .map(n -> createCard(type))
                .toList();
    }

    private Card createCard(CardType type) {
        return switch (type) {
            case ATTACK -> new Card(CardType.ATTACK, "Attack 2", 2);
            case BLOCK -> new Card(CardType.BLOCK, "Block 1", 1);
            case MINION -> throw new IllegalStateException("USE DEDICATED METHOD FOR THIS TYPE");
        };
    }
}
