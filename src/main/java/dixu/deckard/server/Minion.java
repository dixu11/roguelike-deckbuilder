package dixu.deckard.server;

import dixu.deckard.server.event.*;

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

    public void drawTwo(Team team) { //refactor to method that draws 1
        if (draw.size() < 2) {
            Collections.shuffle(discard);
            draw.addAll(discard);
            discard.clear();
            EventBus.getInstance().post(new ShuffleEvent(this));
        }
        Card card1 = draw.remove();
        hand.add(card1);
        EventBus.getInstance().post(new DrawCardEvent(card1,this,team));
        Card card2 = draw.remove();
        hand.add(card2);
        EventBus.getInstance().post(new DrawCardEvent(card2,this,team));
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
            EventBus.getInstance().post(new CardPlayedEvent(team, card, this));
            card.play(team, this,game);
            Game.animate();
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
            EventBus.getInstance().post(new MinionDiedEvent(team, this));
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }
}
