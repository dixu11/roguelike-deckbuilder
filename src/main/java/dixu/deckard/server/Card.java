package dixu.deckard.server;

import dixu.deckard.server.effect.*;

import java.util.ArrayList;
import java.util.List;

import static dixu.deckard.server.CardType.*;

/**
 * {@link Card} is an action that {@link Minion}s can store in their decks and play every turn if it's drawn.
 * {@link Card}s can also be held by {@link Leader}s and given to their {@link Minion}s to modify their decks.
 * {@link Card}'s effect is determined by its {@link Card#category}.
 */
public class Card {
    private String name;
    private CardCategory category;
    private CardType type;
    private final List<CardEffect> effects = new ArrayList<>();

    public Card(CardType type) {
        setupCard(type);
    }

    private void setupCard(CardType type) {
        this.type = type;
        this.name = this.type.getName();
        category = this.type.getCategory();

        switch (category) {
            case ATTACK -> setupAttackCard();
            case BLOCK -> setupBlockCard();
        }
    }

    private void setupAttackCard() {
        AttackEffect attackEffect;
        EnemySelection enemySelection = EnemySelection.RANDOM;
        int attackValue = type.getValue();



        if (type == AREA_ATTACK) {
            enemySelection = EnemySelection.AREA;
        }

        final AttackEffect baseAttack = new AttackEffect(attackValue, enemySelection);
        attackEffect = baseAttack;

        if (type == PIERCING_ATTACK) {
            attackEffect.setPiercing(true);
        }
        if (type == UNSTABLE_ATTACK) {
            attackEffect = new ChangeValueEffect(baseAttack, -1);
        }

        effects.add(attackEffect);

        if (type == GIFT_ATTACK) {
            effects.add(new GiftCardEffect(this));
        }
    }

    private void setupBlockCard() {
        int blockValue = 0;
        if (type == BASIC_BLOCK) {
            blockValue = BASIC_BLOCK.getValue();
        } else if (type == UPGRADED_BLOCK) {
            blockValue = UPGRADED_BLOCK.getValue();
        }

        BlockEffect blockEffect = new BlockEffect(blockValue);
        effects.add(blockEffect);
    }

    public void play(CardContext context) {
        for (CardEffect effect : effects) {
            effect.execute(context);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", type=" + category +
                '}';
    }

    public String getDescription() {
        StringBuilder desc = new StringBuilder();
        for (CardEffect effect : effects) {
            desc.append(effect.getDescription()).append(" ");
        }
        return desc.toString();
    }

    int getApproximateDamage() {
        int attack = 0;
        for (CardEffect effect : effects) {
            attack += effect.getAttack();
        }
        return attack;
    }

    int getApproximateBlock() {
        int block = 0;
        for (CardEffect effect : effects) {
            block += effect.getBlock();
        }
        return block;
    }

}
