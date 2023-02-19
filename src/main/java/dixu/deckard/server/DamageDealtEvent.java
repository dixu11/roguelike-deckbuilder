package dixu.deckard.server;

public class DamageDealtEvent implements Event {
    private final int dmg;
    private final Character target;

    public DamageDealtEvent(int dmg, Character target) {
        this.dmg = dmg;
        this.target = target;
    }

    public int getDmg() {
        return dmg;
    }

    public Character getTarget() {
        return target;
    }
}
