package dixu.deckard.server;

import java.util.*;
import java.util.stream.IntStream;

public class Minion {
    private String name;
    private int hp = 3;
    private static int nextChar = 1;
    private Card minionCard = new Card(CardType.MINION);
    private LinkedList<Card> draw = new LinkedList<>();
    private List<Card> hand = new LinkedList<>();
    private List<Card> discard = new LinkedList<>();

    public Minion() {
        name = "Minion " + nextChar++;
        IntStream.range(0, 2)
                .forEach(n -> draw.add(new Card(CardType.BLOCK)));
        IntStream.range(0, 2)
                .forEach(n -> draw.add(new Card(CardType.ATTACK)));
        Collections.shuffle(draw);
    }

    public void drawTwo() {
        if (draw.size() < 2) {
            Collections.shuffle(discard);
            draw.addAll(discard);
            discard.clear();
        }
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

    public List<Card> getDiscard() {
        return discard;
    }

    public void playCards(Team team,Game game) {
        for (Card card : new ArrayList<>(hand)) {
            card.play(team, this,game);
            EventBus.getInstance().post(new CardPlayedEvent(team, card, this));
        }
    }

    public void remove(Card card) {
        hand.remove(card);
        discard.add(card);
    }

    public Card getMinionCard() {
        return minionCard;
    }

    public void obtainDamage(Team team, int value) {
        EventBus.getInstance().post(new MinionDamagedEvent(hp-value,hp,this));
        hp -= value;
        if (hp <= 0) {
            EventBus.getInstance().post(new MinionDiedEvent(team.getSide(), this));
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }
}
