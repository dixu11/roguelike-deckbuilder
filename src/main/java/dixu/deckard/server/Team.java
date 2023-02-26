package dixu.deckard.server;

import dixu.deckard.server.event.*;

import java.util.List;

public class Team implements EventHandler {
    private static final int START_TURN_CARD_DRAW_COUNT_PER_MINION = 2;
    private final EventBus bus = EventBus.getInstance();
    private final List<Minion> minions;
    private int block;

    public Team(List<Minion> minions) {
        this.minions = minions;

        bus.register(this, MinionDiedEvent.class);
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

        int oldBlock = block;
        if (block <= dmg) {
            dmg -= block;
            block = 0;
        } else { // >
            block -= dmg;
            dmg = 0;
        }

        bus.post(new TeamBlockChangedEvent(block, oldBlock, this));
        return dmg;
    }

    public void clearBlock() {
        bus.post(new TeamBlockChangedEvent(0, block, this));
        block = 0;
    }

    public void addBlock(int value) {
        bus.post(new TeamBlockChangedEvent(value + block, block, this));
        block += value;
    }

    //character death
    @Override
    public void handle(Event event) {
        if (event instanceof MinionDiedEvent) {
            MinionDiedEvent minionDiedEvent = (MinionDiedEvent) event;
            if (minionDiedEvent.getTeam() == this) {
                characterDied(minionDiedEvent.getMinion());
            }
        }
    }

    private void characterDied(Minion minion) {
        minions.remove(minion);
        if (minions.isEmpty()) {
            bus.post(new GameOverEvent(this));
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
