package dixu.deckard.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CardFactory {

    public List<Card> createDeck(LeaderType type) {
        List<Card> cards = new ArrayList<>();
        if (type == LeaderType.PLAYER) {
            cards.addAll(createCards(3, CardType.BASIC_ATTACK));
            cards.addAll(createCards(3, CardType.BASIC_BLOCK));
        } else if (type == LeaderType.SIMPLE_BOT) {
            cards.addAll(createCards(2, CardType.UPGRADED_ATTACK));
            cards.addAll(createCards(2, CardType.UPGRADED_BLOCK));
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

    public Card createCard(CardType type) {
        return new Card(type);
    }
}
