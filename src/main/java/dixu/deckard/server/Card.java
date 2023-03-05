package dixu.deckard.server;

import dixu.deckard.server.effect.AttackEffect;
import dixu.deckard.server.effect.BlockEffect;
import dixu.deckard.server.effect.CardEffect;

import java.util.ArrayList;
import java.util.List;

import static dixu.deckard.server.CardType.*;
import static dixu.deckard.server.GameParams.*;

/**
 * {@link Card} is an action that {@link Minion}s can store in their decks and play every turn if it's drawn.
 * {@link Card}s can also be held by {@link Leader}s and given to their {@link Minion}s to modify their decks.
 * {@link Card}'s effect is determined by its {@link Card#category}.
 */
public class Card {
    private String name;
    private String description = "";
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

        switch (category){
            case ATTACK -> setupAttackCard();
            case BLOCK -> setupBlockCard();
        }
    }

    private void setupAttackCard() {
        int attackValue = 0;
        if (type == BASIC_ATTACK) {
            attackValue = BASIC_ATTACK_VALUE;
        }else if (type == UPGRADED_ATTACK){
            attackValue = UPGRADED_ATTACK_VALUE;
        }
        AttackEffect attackEffect = new AttackEffect(attackValue);
        effects.add(attackEffect);
        description = " " + attackValue + "⚔️ to random enemy minion";
    }

    private void setupBlockCard() {
        int blockValue = 0;
        if (type == BASIC_BLOCK) {
            blockValue = BASIC_BLOCK_VALUE;
        }else if (type == UPGRADED_BLOCK){
            blockValue = UPGRADED_BLOCK_VALUE;
        }

        BlockEffect blockEffect = new BlockEffect(blockValue);
        effects.add(blockEffect);
        description = " +" + blockValue + "\uD83D\uDEE1";
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
        return description;
    }
}
