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

    public Card() {
        this(CardType.BLOCK);
    }

    public void play(Team team, Minion minion) {
        if (type == CardType.BLOCK) {
            team.addBlock(value);
            minion.remove(this);
        }
        if (type == CardType.ATTACK) {
            EventBus.getInstance().post(new RandomDmgEvent(team.getSide(),value));
            minion.remove(this);
        }
        Game.animate();
    }

    public String getName() {
        return name;
    }

    public CardType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "LoadedCard{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", type=" + type +
                '}';
    }
}
