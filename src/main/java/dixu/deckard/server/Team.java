package dixu.deckard.server;

import dixu.deckard.server.event.*;

import java.util.List;
import java.util.Random;

public class Team implements EventHandler {
    private List<Minion> minions;
    private int block;
    private TeamSide side;

    public Team(List<Minion> minions, TeamSide side) {
        this.minions = minions;
        this.side = side;
        EventBus.getInstance().register(this, MinionDiedEvent.class);
    }

    public List<Minion> getCharacters() {
        return minions;
    }

    public void playCards(PlayContext playContext) {
        for (Minion minion : minions) {
            playContext.setMinion(minion);
            minion.playCards(playContext);
        }
    }

    public TeamSide getSide() {
        return side;
    }

    public void applyDmg(int dmg, Minion minion) {
        int dmgLeft = trashBlock(dmg);
        if (dmgLeft <= 0) {
            return;
        }
        minion.obtainDamage(this,dmgLeft);
    }

    private int trashBlock(int dmg) {
        if (block == 0) {
            return dmg;
        }
        int oldBlock = block;
        if (block <= dmg) {
            dmg -= block;
            block = 0;
        } else {
            block -= dmg;
            dmg = 0;
        }
        EventBus.getInstance().post(new TeamBlockEvent(block, oldBlock, this));
        return dmg;
    }

    public void drawCards() {
        for (Minion minion : minions) {
            minion.drawTwo(this);
        }
    }

    public void clearBlock() {
        EventBus.getInstance().post(new TeamBlockEvent(0, block, this));
        block = 0;
    }

    public void addBlock(int value) {
        EventBus.getInstance().post(new TeamBlockEvent(value+block, block, this));
        block += value;
    }

    @Override
    public void handle(Event event) {
        //handle character died
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
            EventBus.getInstance().post(new GameOverEvent(this));
        }
    }

    public Minion getRandomMinion() {
        return MyRandom.getRandomElement(minions);
    }
}
