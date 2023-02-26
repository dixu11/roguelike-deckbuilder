package dixu.deckard.server;

public class Card {
    private final String name;
    private  int value;
    private CardType type;
    private static int nextNr = 1;

    public Card(CardType type) {
        this.type = type;
        value = 1;
        if (type == CardType.ATTACK) {
            value = 2;
        } else if (type == CardType.MINION) {
            value = nextNr;
        }
        nextNr++;

        name = type.name()+ " "+ value;
    }

    public void play(CardContext context) {
        if (type == CardType.BLOCK) {
            context.getActionTeam().addBlock(value);
        }
        if (type == CardType.ATTACK) {
            context.getEnemyTeam().applyDmgTo(value,context.getEnemyTeam().getRandomMinion());
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
