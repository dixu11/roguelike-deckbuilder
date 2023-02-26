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

    public void drawCards(int count, CardContext context) {
        for (int i = 0; i < count; i++) {
            CardContext contextCopy = context.getCopy();
            drawCard(contextCopy);
        }
    }

    private void drawCard(CardContext context) {
        if (draw.isEmpty()) {
            Collections.shuffle(discard);
            draw.addAll(discard);
            discard.clear();
            EventBus.getInstance().post(new ShuffleEvent(this));
        }
        Card card = draw.remove();
        hand.add(card);
        context.setCard(card);
        EventBus.getInstance().post(new DrawCardEvent(context));
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

    public void playCards(CardContext cardContext) {
        for (Card card : new ArrayList<>(hand)) {
            cardContext.setCard(card);
            EventBus.getInstance().post(new CardPlayedEvent(cardContext));
            card.play(cardContext);
            remove(card);
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

    public void applyDamage(Team team, int value) {
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
