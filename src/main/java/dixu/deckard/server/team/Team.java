package dixu.deckard.server.team;

import dixu.deckard.server.event.bus.Bus;
import dixu.deckard.server.game.MyRandom;
import dixu.deckard.server.card.Card;
import dixu.deckard.server.card.CardContext;
import dixu.deckard.server.card.effect.AttackEffect;
import dixu.deckard.server.card.effect.EnemySelection;
import dixu.deckard.server.event.*;
import dixu.deckard.server.combat.Combat;
import dixu.deckard.server.leader.Leader;
import dixu.deckard.server.minion.Minion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dixu.deckard.server.game.GameParams.MINION_DRAW_PER_TURN;

/**
 * {@link Team} is group of {@link Minion}s, controlled by {@link Leader}, spawning on one side of the {@link Combat}.
 * When {@link Leader} ends his turn {@link Minion}s of all {@link Team}s play their cards automatically.
 * {@link Team}s have {@link Team#block} attribute that lasts one turn and protects them from the attacks.
* */

public class Team implements EventHandler {
    private final List<Minion> minions;
    private int block;
    private boolean clearBlockEnabled = true;

    public Team(List<Minion> minions) {
        this.minions = new ArrayList<>(minions);
        for (Minion minion : minions) {
            minion.setTeam(this);
        }
        Bus.register(this, ActionEventType.MINION_DIED);
    }

    //draws
    public void minionsDrawCards() {
        for (Minion minion : minions) {
            minion.drawCards(MINION_DRAW_PER_TURN);
        }
    }

    public void playAllCards(CardContext cardContext) {
        for (Minion minion : minions) {
            cardContext.setMinion(minion);
            minion.playAllCards(cardContext);
        }
    }

    //damage / block

    public void applyDmgTo(int dmg, AttackEffect effect) {
        EnemySelection type = effect.getType();

        if (!effect.isPiercing()) {
            dmg = applyDmgToBlock(dmg);
            if (dmg <= 0) return;
        }
        if (type == EnemySelection.RANDOM) {
            Optional<Minion> optionalMinion = getRandomMinion();
            if (optionalMinion.isEmpty()) return;
            optionalMinion.get().applyDamage( dmg);
        }

        if (type == EnemySelection.AREA) {
            for (Minion minion : new ArrayList<>(minions)) {
                minion.applyDamage(dmg);
            }
        }

    }

    private int applyDmgToBlock(int dmg) {
        if (block == 0) return dmg;
        if (block <= dmg) {
            dmg -= block;
            block = 0;
        } else { // >
            block -= dmg;
            dmg = 0;
        }
        postBlockChangedEvent(block);
        return dmg;
    }

    public void clearBlock() {
        if (!clearBlockEnabled) {
            return;
        }
        block = 0;
        postBlockChangedEvent(0);
    }

    public void addBlock(int value) {
        block += value;
        postBlockChangedEvent(block);
    }

    private void postBlockChangedEvent(int newValue) {
        Bus.post(ActionEvent.builder()
                .type(ActionEventType.TEAM_BLOCK_CHANGED)
                .value(newValue)
                .ownTeam(this)
                .build()
        );
    }

    //character death
    @Override
    public void handle(ActionEvent event) {
        if (event.getType() == ActionEventType.MINION_DIED) {
            onMinionDied(event);
        }
    }

    private void onMinionDied(ActionEvent event) {
        if (event.getOwnTeam() == this) {
            characterDied(event.getMinion());
        }
    }

    private void characterDied(Minion minion) {
        minions.remove(minion);
        if (minions.isEmpty()) {
            Bus.post(CoreEvent.of(CoreEventType.COMBAT_OVER));
        }
    }

    //minion access
    public Optional<Minion> getRandomMinion() {
        return MyRandom.getRandomElement(minions);
    }

    public List<Card> getMinionsHands() {
        return minions.stream()
                .flatMap(minion -> minion.getHand().stream())
                .toList();
    }

    public List<Minion> getMinions() {
        return minions;
    }
    //getters setters n stuff

    public int getBlock() {
        return block;
    }

    public void setClearBlockEnabled(boolean clearBlockEnabled) {
        this.clearBlockEnabled = clearBlockEnabled;
    }

    public void setBlock(int block) {
        this.block = block;
    }
}
