package dixu.deckard.server;

import java.util.List;
import java.util.Random;

public class Team {
    private List<Minion> minions;
    private int block;
    private TeamSide side;

    public Team(List<Minion> minions, int block, TeamSide side) {
        this.minions = minions;
        this.block = block;
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

    public void addBlock(int value) {
        block += value;
    }

    public int getBlock() {
        return block;
    }

    public TeamSide getSide() {
        return side;
    }

    public void applyRandomDmg(int value) {
        if (block <= value) {
            value -= block;
            block = 0;
        } else {
            block -= value;
            return;
        }
        Random random = new Random();
        minions.get(random.nextInt(minions.size())).obtainDamage(this,value);
    }

    public void drawCards() {
        for (Minion minion : minions) {
            minion.drawTwo();
        }
    }

    public void clearBlock() {
        block = 0;
    }

    public void characterDied(Minion minion) {
        minions.remove(minion);
        if (minions.isEmpty()) {
            EventBus.getInstance().post(new GameOverEvent(this));
        }

    }
}
