package dixu.deckard.cardloader;

import dixu.deckard.server.Minion;
import dixu.deckard.server.Team;

import java.util.ArrayList;
import java.util.List;

public class LoadedCard {
    private String name = "";
    private final List<CardEffect> effects = new ArrayList<>();

    public LoadedCard() { }

    public void play(Team team, Minion minion) {
        effects.forEach(effect -> effect.apply(team, minion));

        //minion.remove(this);
        //Game.animate();
    }

    @Override
    public String toString() {
        return "LoadedCard{" +
                "name='" + name + '\'' +
                ", effects=" + effects +
                '}';
    }
}
