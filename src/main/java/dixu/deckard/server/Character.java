package dixu.deckard.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

public class Character {
    private String name;
    private int hp = 3;
    private static int nextChar = 1;
    private Queue<Card> draw = new LinkedList<>();
    private List<Card> hand  = new LinkedList<>();
    private List<Card> played = new LinkedList<>();

    public Character() {
        name = "Character " + nextChar++;
        IntStream.range(0, 4)
                .forEach(n->draw.add(new Card()));
        drawTwo();
    }

    public void drawTwo() {
        hand.add(draw.remove());
        hand.add(draw.remove());
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return hp;
    }

    public List<Card> getHand() {
        return hand;
    }

    public Queue<Card> getDraw() {
        return draw;
    }

    public List<Card> getPlayed() {
        return played;
    }

    public void playBlocks(Team team) {
        for (Card card : new ArrayList<>(hand)) {
            if (card.getType()== CardType.BLOCK) {
                EventBus.getInstance().post(new CardPlayedEvent(team, card, this));
                card.play(team,this);
            }
        }
    }
}
