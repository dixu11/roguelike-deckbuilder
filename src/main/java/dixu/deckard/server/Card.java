package dixu.deckard.server;

import java.util.Optional;

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
            case ATTACK -> {
                Optional<Minion> optionalMinion = context.getEnemyTeam().getRandomMinion();
                if(optionalMinion.isEmpty()) return;

                context.getEnemyTeam()
                        .applyDmgTo(value, optionalMinion.get());
            }
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
