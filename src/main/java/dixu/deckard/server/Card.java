package dixu.deckard.server;

import java.util.Optional;

/**
 * {@link Card} is an action that {@link Minion}s can store in their decks and play every turn if it's drawn.
 * {@link Card}s can also be held by {@link Leader}s and given to their {@link Minion}s to modify their decks.
 * {@link Card}'s effect is determined by its {@link Card#type}.
* */
public class Card {
    private final String name;
    private final int value;

    private final String description;
    private final CardType type;

    public Card(CardType type, String name, int value) {
        this.type = type;
        this.name = name;
        this.value = value;
        description = generateSimpleDescription();
    }

    private String generateSimpleDescription() {
        if (type == CardType.ATTACK) {
            return " " + value + "⚔️ to random enemy minion";
        } else if(type == CardType.BLOCK){
            return " +" + value + "\uD83D\uDEE1";
        }else {
            return "Basic creature with basic cards";
        }

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

    public String getDescription() {
        return description;
    }
}
