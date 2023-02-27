package dixu.deckard.server;

import dixu.deckard.server.event.*;

import java.util.List;

public class Team implements FightEventHandler {
    private static final int START_TURN_CARD_DRAW_COUNT_PER_MINION = 2;
    private final BusManager bus = BusManager.instance();
    private final List<Minion> minions;
    private int block;

    public Team(List<Minion> minions) {
        this.minions = minions;

        bus.register(this, FightEventName.MINION_DIED);
    }

    //draws
    public void executeStartTurnCardDraws(CardContext context) {
        for (Minion minion : minions) {
            context.setMinion(minion);
            minion.drawCards(START_TURN_CARD_DRAW_COUNT_PER_MINION, context);
        }
    }

    public void playAllCards(CardContext cardContext) {
        for (Minion minion : minions) {
            cardContext.setMinion(minion);
            minion.playAllCards(cardContext);
        }
    }

    //damage / block

    public void applyDmgTo(int dmg, Minion minion) {
        int dmgLeft = applyDmgToBlock(dmg);
        if (dmgLeft <= 0) return;

        minion.applyDamage(this, dmgLeft);
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
        postBlockChangedEvent(0);
        block = 0;
    }

    public void addBlock(int value) {
        postBlockChangedEvent(value);
        block += value;
    }

    private void postBlockChangedEvent(int newValue) {
        bus.post(FightEvent.builder()
                .name(FightEventName.TEAM_BLOCK_CHANGED)
                .value(newValue)
                .source(this)
                .build()
        );
    }

    //character death
    @Override
    public void handle(FightEvent event) {
        if (event.getName() == FightEventName.MINION_DIED) {
            onMinionDied(event);
        }
    }

    private void onMinionDied(FightEvent event) {
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

    public Minion getRandomMinion() {
        return MyRandom.getRandomElement(minions);
    }

    public List<Minion> getMinions() {
        return minions;
    }
}
