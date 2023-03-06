package dixu.deckard.server;

import dixu.deckard.server.event.CardRarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class CardFactory {

    public List<Card> createDeck(LeaderType type) {
        List<Card> cards = new ArrayList<>();
        if (type == LeaderType.PLAYER) {
            cards.addAll(createCards(2, CardType.BASIC_ATTACK));
            cards.addAll(createCards(2, CardType.BASIC_BLOCK));
            cards.addAll(createCards(1, CardType.UPGRADED_ATTACK));
            cards.addAll(createCards(1, CardType.UPGRADED_BLOCK));
        } else if (type == LeaderType.SIMPLE_BOT) {
//            cards.addAll(createCards(1, CardType.UPGRADED_ATTACK));
//            cards.addAll(createCards(1, CardType.UPGRADED_BLOCK));
            cards.addAll(createRandomCards(4, CardRarity.COMMON));
            cards.addAll(createCards(1, CardType.BASIC_ATTACK));
            cards.addAll(createCards(1, CardType.BASIC_BLOCK));
        } else {
            throw new IllegalStateException("INVALID DECK TYPE");
        }
        return cards;
    }

    public List<Card> createCards(int count, CardType type) {
        return IntStream.range(0, count)
                .boxed()
                .map(n -> new Card(type))
                .toList();
    }

    private List<Card> createRandomCards(int count, CardRarity rarity) {
        return IntStream.range(0, count)
                .boxed()
                .map(num -> new Card(CardType.getRandom(rarity)))
                .toList();
    }

    public Card createCard(CardType type) {
        return new Card(type);
    }
}
