package dixu.deckard.server;

import java.util.LinkedList;
import java.util.List;

public class Character {
    private String name;
    private int hp = 3;
    private static int nextChar = 1;
    private List<Card> draw = new LinkedList<>();
    private List<Card> hand  = new LinkedList<>();
    private List<Card> played = new LinkedList<>();

    public Character() {
        name = "Character " + nextChar++;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return hp;
    }
}
