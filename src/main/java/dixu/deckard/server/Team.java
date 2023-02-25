package dixu.deckard.server;

import java.util.List;
import java.util.Random;

public class Team {
    private List<Minion> minions;
    private int block;
    private TeamSide side;

    public Team(List<Minion> minions, TeamSide side) {
        this.minions = minions;
        this.side = side;
    }

    public List<Minion> getCharacters() {
        return minions;
    }

    public void playCards() {
        for (Minion minion : minions) {
            minion.playCards(this);
        }
    }

    public TeamSide getSide() {
        return side;
    }

    public void applyRandomDmg(int value) {
        int oldBlock = block;
        if (block <= value) {
            value -= block;
            block = 0;
        } else {
            block -= value;
            return;
        }
        EventBus.getInstance().post(new TeamBlockEvent(block, oldBlock, this));
        Random random = new Random();
        minions.get(random.nextInt(minions.size())).obtainDamage(this,value);
    }

    public void drawCards() {
        for (Minion minion : minions) {
            minion.drawTwo();
        }
    }

    public void clearBlock() {
        EventBus.getInstance().post(new TeamBlockEvent(0, block, this));
        block = 0;
    }

    public void characterDied(Minion minion) {
        minions.remove(minion);
        if (minions.isEmpty()) {
            EventBus.getInstance().post(new GameOverEvent(this));
        }
    }

    public void addBlock(int value) {
        EventBus.getInstance().post(new TeamBlockEvent(value+block, block, this));
        block += value;
    }
}
