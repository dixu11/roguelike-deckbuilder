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

    public void playCards(Game game) {
        for (Minion minion : minions) {
            minion.playCards(this,game);
        }
    }

    public TeamSide getSide() {
        return side;
    }

    public void applyRandomDmg(int dmg) {
        int dmgLeft = trashBlock(dmg);
        if (dmgLeft <= 0) {
            return;
        }
        Random random = new Random();
        minions.get(random.nextInt(minions.size())).obtainDamage(this,dmgLeft);

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
