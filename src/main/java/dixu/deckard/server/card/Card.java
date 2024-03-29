package dixu.deckard.server.card;

import dixu.deckard.server.event.bus.Bus;
import dixu.deckard.server.leader.Leader;
import dixu.deckard.server.minion.Minion;
import dixu.deckard.server.card.effect.*;
import dixu.deckard.server.event.ActionEvent;
import dixu.deckard.server.event.ActionEventType;

import java.util.ArrayList;
import java.util.List;

import static dixu.deckard.server.card.CardType.*;

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
    private Minion owner;

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
            case SKILL -> setupSkillCard();
        }
    }

    private void setupAttackCard() {
        AttackEffect attackEffect;
        EnemySelection enemySelection = EnemySelection.RANDOM;
        int attackValue = type.getValue();

        if (type == AREA_ATTACK) {
            enemySelection = EnemySelection.AREA;

        }

        final AttackEffect baseAttack = new BasicAttackEffect(attackValue, enemySelection, this);
        attackEffect = baseAttack;

        if (type == PIERCING_ATTACK) {
            attackEffect.setPiercing(true);
        }
        if (type == UNSTABLE_ATTACK) {
            attackEffect = new ChangeValueEffect(baseAttack, -1);
        }
        if (type == COMBO_ATTACK) {
            attackEffect = new ComboEffect(baseAttack);
        }
        if (type == SOLO_ATTACK) {
            attackEffect = new SoloEffect(baseAttack);
        }
        if (type == LIFE_LUST) {
            attackEffect = new LifeLustEffect(baseAttack);
        }

        effects.add(attackEffect);

        if (type == GIFT_ATTACK) {
            effects.add(new GiftCardEffect(this));
        }
    }

    private void setupBlockCard() {
        int blockValue = type.getValue();
        BlockEffect blockEffect = new BasicBlockEffect(blockValue, this);
        if (type == DECK_SHIELD) {
            blockEffect = new DeckShieldEffect(blockEffect);
        }
        if (type == BLOCK_BOOSTER) {
            blockEffect = new BlockBoosterEffect(blockEffect);
        }
        effects.add(blockEffect);
    }

    private void setupSkillCard() {
        if (type == HEAL) {
            effects.add(new HealEffect(HEAL.getValue(), this));
        }
    }

    public void play(CardContext context) {
        for (CardEffect effect : effects) {
            effect.execute(context);
        }
        Bus.post(ActionEvent.builder()
                .type(ActionEventType.MINION_CARD_PLAYED)
                .card(context.getCard())
                .minion(context.getMinion())
                .ownTeam(context.getOwnTeam())
                .enemyTeam(context.getEnemyTeam())
                .build());
    }

    public String getDescription() {
        StringBuilder desc = new StringBuilder();
        for (CardEffect effect : effects) {
            desc.append(effect.getDescription()).append(" ");
        }
        if (type == AREA_ATTACK) { //todo refactor
            desc.append(" damages every enemy minion");
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

    public String getName() {
        return name;
    }

    public CardCategory getCategory() {
        return category;
    }

    public Minion getOwner() {
        return owner;
    }

    public void setOwner(Minion owner) {
        this.owner = owner;
      /*  Bus.post(ActionEvent.builder()
                .type(ActionEventType.CARD_OWNER_CHANGED)
                .card(this)
                .minion(owner)
                .build()
        );*/
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", type=" + category +
                '}';
    }
}
