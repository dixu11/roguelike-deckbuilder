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
            team.addBlock(value);
            character.remove(this);
        }
        if (type == CardType.ATTACK) {
            EventBus.getInstance().post(new RandomDmgEvent(team.getSide(),value));
            character.remove(this);
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
        return "Card{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", type=" + type +
                '}';
    }
}
