package dixu.deckard.server;

import dixu.deckard.server.effect.BasicAttackEffect;
import dixu.deckard.server.effect.EnemySelection;
import dixu.deckard.server.event.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dixu.deckard.server.GameParams.MINION_DRAW_PER_TURN;

/**
 * {@link Team} is group of {@link Minion}s, controlled by {@link Leader}, spawning on one side of the {@link Fight}.
 * When {@link Leader} ends his turn {@link Minion}s of all {@link Team}s play their cards automatically.
 * {@link Team}s have {@link Team#block} attribute that lasts one turn and protects them from the attacks.
* */

public class Team implements ActionEventHandler {
    private final BusManager bus = BusManager.instance();
    private final List<Minion> minions;
    private int block;
    private boolean clearBlockEnabled = true;

    public Team(List<Minion> minions) {
        this.minions = new ArrayList<>(minions);
        for (Minion minion : minions) {
            minion.setTeam(this);
        }

        bus.register(this, ActionEventName.MINION_DIED);
    }

    //draws
    public void executeStartTurnCardDraws() {
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

    public void applyDmgTo(int dmg, BasicAttackEffect effect) {
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
            for (Minion minion : minions) {
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
        postBlockChangedEvent(0);
        block = 0;
    }

    public void addBlock(int value) {
        postBlockChangedEvent(value + block);
        block += value;
    }

    private void postBlockChangedEvent(int newValue) {
        bus.post(ActionEvent.builder()
                .name(ActionEventName.TEAM_BLOCK_CHANGED)
                .value(newValue)
                .ownTeam(this)
                .build()
        );
    }

    //character death
    @Override
    public void handle(ActionEvent event) {
        if (event.getName() == ActionEventName.MINION_DIED) {
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
            bus.post(CoreEvent.of(CoreEventName.GAME_OVER));
        }
    }

    //minion access
    public Optional<Minion> getRandomMinion() {
        return MyRandom.getRandomElement(minions);
    }

    public List<Minion> getMinions() {
        return minions;
    }

    //getters setters n stuff
    public int getBlock() {
        return block;
    }

    //for tests
    public void setClearBlockEnabled(boolean clearBlockEnabled) {
        this.clearBlockEnabled = clearBlockEnabled;
    }

    public void setBlock(int block) {
        this.block = block;
    }
}
