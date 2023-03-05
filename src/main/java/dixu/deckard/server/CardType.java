package dixu.deckard.server;

import dixu.deckard.server.event.CardRarity;

import java.util.Arrays;

import static dixu.deckard.server.GameParams.*;
import static dixu.deckard.server.event.CardRarity.BASIC;
import static dixu.deckard.server.event.CardRarity.COMMON;

public enum CardType {
    BASIC_ATTACK(CardCategory.ATTACK, BASIC, "Attack " + BASIC_ATTACK_VALUE),
    UPGRADED_ATTACK(CardCategory.ATTACK, COMMON, "Attack " + UPGRADED_ATTACK_VALUE),
    BASIC_BLOCK(CardCategory.BLOCK, BASIC, "Block " + BASIC_BLOCK_VALUE),
    UPGRADED_BLOCK(CardCategory.BLOCK, COMMON, "Block " + UPGRADED_BLOCK_VALUE),
    BASIC_MINION(CardCategory.MINION, BASIC, "Minion"),
    UNSTABLE_ATTACK(CardCategory.ATTACK, COMMON, "Unstable attack");

    private final CardCategory category;
    private final CardRarity rarity;
    private final String name;

    CardType(CardCategory category, CardRarity rarity, String name) {
        this.category = category;
        this.rarity = rarity;
        this.name = name;
    }

    public static CardType getRandom(CardRarity rarity) {
        return MyRandom.getRandomElement(
                        Arrays.stream(CardType.values())
                                .filter(type -> type.getRarity() == rarity)
                                .toList())
                .orElseThrow();
    }

    public CardCategory getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public CardRarity getRarity() {
        return rarity;
    }
}
