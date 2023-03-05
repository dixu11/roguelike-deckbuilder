package dixu.deckard.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static dixu.deckard.server.GameParams.*;


public class CardFactory {


    public List<Card> createDeck(LeaderType type) {
        List<Card> cards = new ArrayList<>();
        if (type == LeaderType.PLAYER) {
            cards.addAll(createCards(3, CardCategory.ATTACK));
            cards.addAll(createCards(3, CardCategory.BLOCK));
        } else if (type == LeaderType.SIMPLE_BOT) {
            cards.addAll(createCards(2, CardCategory.ATTACK,BETTER_ATTACK_VALUE));
            cards.addAll(createCards(2, CardCategory.BLOCK,BETTER_BLOCK_VALUE));
            cards.addAll(createCards(1, CardCategory.ATTACK));
            cards.addAll(createCards(1, CardCategory.BLOCK));
        } else {
            throw new IllegalStateException("INVALID DECK TYPE");
        }
        return cards;
    }
    public List<Card> createCards(int count, CardCategory type) {
        return createCards(count, type, null);
    }

    public List<Card> createCards(int count, CardCategory type, Integer value) {
        return IntStream.range(0, count)
                .boxed()
                .map(n -> createCard(type,value))
                .toList();
    }
    private Card createCard(CardCategory type){
        if (CardCategory.ATTACK == type) {
           return createCard(type, DEFAULT_ATTACK_VALUE);
        } else if (CardCategory.BLOCK == type) {
           return createCard(type, DEFAULT_BLOCK_VALUE);
        } else {
            throw new IllegalStateException("USE DEDICATED METHOD FOR THIS TYPE");
        }
    }

    public Card createCard(CardCategory type, Integer value) {
        if (value == null) {
           return createCard(type);
        }
        return switch (type) {
            case ATTACK -> new Card(CardCategory.ATTACK, "Attack "+ value, value);
            case BLOCK -> new Card(CardCategory.BLOCK, "Block " + value, value);
            case MINION -> throw new IllegalStateException("USE DEDICATED METHOD FOR THIS TYPE");
        };
    }
}
