package dixu.deckard.server;

public class Card {
    private final String name;
    private final int value;
    private final CardType type;

    public Card(CardType type, String name, int value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public void play(CardContext context) {
        switch (type) {
            case ATTACK -> context.getEnemyTeam()
                    .applyDmgTo(value, context.getEnemyTeam().getRandomMinion());
            case BLOCK -> context.getOwnTeam().addBlock(value);
            case MINION -> throw new IllegalArgumentException("THIS TYPE CANNOT BE PLAYED");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", type=" + type +
                '}';
    }
}
