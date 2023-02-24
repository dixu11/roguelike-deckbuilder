package dixu.deckard.server;

import java.util.LinkedList;
import java.util.List;

public class Character {
    private int name;
    private int hp = 3;
    private List<Card> draw = new LinkedList<>();
    private List<Card> hand  = new LinkedList<>();
    private List<Card> played = new LinkedList<>();
}
