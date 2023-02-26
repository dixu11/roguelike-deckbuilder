package dixu.deckard.server;

import dixu.deckard.server.event.*;

import java.util.*;

public class Minion {
    private static int nextId = 1;
    private final EventBus bus = EventBus.getInstance();
    private int hp = 3;
    private final Card minionCard;
    private final LinkedList<Card> draw = new LinkedList<>();
    private final List<Card> hand = new LinkedList<>();
    private final List<Card> discard = new LinkedList<>();


    public Minion() {
        String name = "Minion " + nextId++;
        minionCard = new Card(CardType.MINION, name, hp);
        CardFactory cardFactory = new CardFactory();
        draw.addAll(cardFactory.createCards(2, CardType.ATTACK));
        draw.addAll(cardFactory.createCards(2, CardType.BLOCK));
        Collections.shuffle(draw);
    }

    //card draw
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
            bus.post(new ShuffleEvent(this));
        }
        Card card = draw.remove();
        hand.add(card);
        context.setCard(card);
        bus.post(new DrawCardEvent(context));
    }

    //cards play
    public void playAllCards(CardContext cardContext) {
        for (Card card : new ArrayList<>(hand)) {
            cardContext.setCard(card);
            bus.post(new CardPlayedEvent(cardContext));
            card.play(cardContext);
            remove(card);
            Game.animate();
        }
    }

    private void remove(Card card) {
        hand.remove(card);
        discard.add(card);
    }

    //fight
    public void applyDamage(Team team, int value) {
        bus.post(new MinionDamagedEvent(hp-value,hp,this));
        hp -= value;
        if (hp <= 0) {
            bus.post(new MinionDiedEvent(team, this));
        }
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

    public Card getMinionCard() {
        return minionCard;
    }
}
