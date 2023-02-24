package dixu.deckard.server;

public class Card {
    private final String name;
    private final int value;
    private CardType type;
    private static int nextNr = 1;

    public Card(CardType type) {
        this.type = type;
        name = type.name()+ " "+ nextNr++;
        value = 1;
    }

    public Card() {
        this(CardType.BLOCK);
    }

    public void play(Team team, Character character) {
        if (type == CardType.BLOCK) {
            team.addBlock(value); // as event?
        }
    }

    public String getName() {
        return name;
    }

    public CardType getType() {
        return type;
    }
}
