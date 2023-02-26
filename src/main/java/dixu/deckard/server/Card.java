package dixu.deckard.server;

public class Card {
    private final String name;
    private  int value;
    private CardType type;

    public Card(CardType type, String name, int value) {
        this.type = type;
        this.name = name;
        this.value = value;
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
