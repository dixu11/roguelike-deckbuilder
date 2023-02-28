package dixu.deckard.server;

import java.util.List;
import java.util.stream.IntStream;

import static dixu.deckard.server.GameParams.DEFAULT_ATTACK_VALUE;
import static dixu.deckard.server.GameParams.DEFAULT_BLOCK_VALUE;

public class CardFactory {

    public List<Card> createCards(int count, CardType type) {
        return IntStream.range(0, count)
                .boxed()
                .map(n -> createCard(type))
                .toList();
    }

    private Card createCard(CardType type) {
        return switch (type) {
            case ATTACK -> new Card(CardType.ATTACK, "Attack "+ DEFAULT_ATTACK_VALUE, DEFAULT_ATTACK_VALUE);
            case BLOCK -> new Card(CardType.BLOCK, "Block " + DEFAULT_BLOCK_VALUE, DEFAULT_BLOCK_VALUE);
            case MINION -> throw new IllegalStateException("USE DEDICATED METHOD FOR THIS TYPE");
        };
    }
}
