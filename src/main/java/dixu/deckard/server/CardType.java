package dixu.deckard.server;

public enum CardType {
    BASIC_ATTACK(CardCategory.ATTACK, "Attack " + GameParams.BASIC_ATTACK_VALUE),
    UPGRADED_ATTACK(CardCategory.ATTACK, "Attack " + GameParams.UPGRADED_ATTACK_VALUE),
    BASIC_BLOCK(CardCategory.BLOCK, "Block " + GameParams.BASIC_BLOCK_VALUE),
    UPGRADED_BLOCK(CardCategory.BLOCK, "Block " + GameParams.UPGRADED_BLOCK_VALUE),
    BASIC_MINION(CardCategory.MINION,"Minion");

    private final CardCategory category;
    private final String name;

    CardType(CardCategory category, String name) {
        this.category = category;
        this.name = name;
    }

    public CardCategory getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
}
