package dixu.deckard.server;

import java.util.List;
import java.util.stream.IntStream;

import static dixu.deckard.server.GameParams.DEFAULT_ATTACK_VALUE;
import static dixu.deckard.server.GameParams.DEFAULT_BLOCK_VALUE;


public class CardFactory {


    public List<Card> createCards(int count, CardType type) {
        return createCards(count, type, null);
    }
    public List<Card> createCards(int count, CardType type,Integer value) {
        return IntStream.range(0, count)
                .boxed()
                .map(n -> createCard(type,value))
                .toList();
    }

    private Card createCard(CardType type){
        if (CardType.ATTACK == type) {
           return createCard(type, DEFAULT_ATTACK_VALUE);
        } else if (CardType.BLOCK == type) {
           return createCard(type, DEFAULT_BLOCK_VALUE);
        } else {
            throw new IllegalStateException("USE DEDICATED METHOD FOR THIS TYPE");
        }
    }
    private Card createCard(CardType type,Integer value) {
        if (value == null) {
           return createCard(type);
        }
        return switch (type) {
            case ATTACK -> new Card(CardType.ATTACK, "Attack "+ value, value);
            case BLOCK -> new Card(CardType.BLOCK, "Block " + value, value);
            case MINION -> throw new IllegalStateException("USE DEDICATED METHOD FOR THIS TYPE");
        };
    }
}
