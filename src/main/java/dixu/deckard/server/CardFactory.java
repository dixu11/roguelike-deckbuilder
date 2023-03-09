package dixu.deckard.server;

import dixu.deckard.server.event.CardRarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class CardFactory {

    public static int INITIAL_MINION_DECK_SIZE = 8;
    private final boolean testMode = true;  //for easier changing from test scenario to normal game

    private List<Card> testScenario(LeaderType type){
        List<Card> cards = new ArrayList<>();
        if (type == LeaderType.PLAYER) {
            cards.addAll(createCards(3, CardType.BASIC_ATTACK));
            cards.addAll(createCards(3, CardType.BASIC_BLOCK));
            cards.addAll(createCards(1, CardType.UPGRADED_ATTACK));
            cards.addAll(createCards(1, CardType.UPGRADED_BLOCK));
        } else if (type == LeaderType.SIMPLE_BOT) {
            cards.addAll(createCards(1, CardType.UPGRADED_BLOCK));
            cards.addAll(createCards(1, CardType.UPGRADED_ATTACK));
            cards.addAll(createRandomCards(4, CardRarity.COMMON));
            cards.addAll(createCards(1, CardType.BASIC_BLOCK));
            cards.addAll(createCards(1, CardType.BASIC_ATTACK));
        } else {
            throw new IllegalStateException("INVALID DECK TYPE");
        }
        return cards;
    }

    public List<Card> createDeck(LeaderType type) {
        List<Card> cards = new ArrayList<>();
        if (testMode) {
            return testScenario(type);
        }
        if (type == LeaderType.PLAYER) {
            cards.addAll(createCards(3, CardType.BASIC_ATTACK));
            cards.addAll(createCards(3, CardType.BASIC_BLOCK));
            cards.addAll(createCards(1, CardType.UPGRADED_ATTACK));
            cards.addAll(createCards(1, CardType.UPGRADED_BLOCK));
        } else if (type == LeaderType.SIMPLE_BOT) {
          /*  cards.addAll(createCards(1, CardType.BASIC_ATTACK));
            cards.addAll(createCards(1, CardType.BASIC_BLOCK));
            cards.addAll(createCards(1, CardType.AREA_ATTACK));
            cards.addAll(createCards(1, CardType.COMBO_ATTACK));
            cards.addAll(createCards(1, CardType.PIERCING_ATTACK));
            cards.addAll(createCards(1, CardType.UNSTABLE_ATTACK));
            cards.addAll(createCards(1, CardType.SOLO_ATTACK));
            cards.addAll(createCards(1, CardType.GIFT_ATTACK));
            cards.addAll(createCards(1, CardType.DECK_SHIELD));
            cards.addAll(createCards(1, CardType.BLOCK_BOOSTER));
            cards.addAll(createCards(1, CardType.LIFE_LUST));
            cards.addAll(createCards(1, CardType.HEAL));
            cards.addAll(createCards(1, CardType.UPGRADED_BLOCK));*/

            cards.addAll(createCards(1, CardType.UPGRADED_BLOCK));
            cards.addAll(createCards(1, CardType.UPGRADED_ATTACK));
            cards.addAll(createRandomCards(4, CardRarity.COMMON));
//            cards.addAll(createCards(3, CardType.GIFT_ATTACK));
            //cards.addAll(createCards(1, CardType.SOLO_ATTACK));
            cards.addAll(createCards(1, CardType.BASIC_BLOCK));
            cards.addAll(createCards(1, CardType.BASIC_ATTACK));
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

    public Card createRandomCard() {
        return new Card(CardType.getRandom());
    }

    public Card createCard(CardType type) {
        return new Card(type);
    }
}
