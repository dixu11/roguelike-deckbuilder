package dixu.deckard.server;

import dixu.deckard.server.event.*;

import java.util.*;

import static dixu.deckard.server.GameParams.MINION_INITIAL_HP;

public class Minion {
    private static int nextId = 1;
    private final BusManager bus = BusManager.instance();
    private int hp = MINION_INITIAL_HP;
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
            CardContext contextCopy = context.getCopy(); //we play many cards so there are many contexts
            drawCard(contextCopy);
        }
    }

    private void drawCard(CardContext context) {
        if (draw.isEmpty()) {
            Collections.shuffle(discard);
            draw.addAll(discard);
            discard.clear();
            postShuffleEvent();

        }
        Card card = draw.remove();
        hand.add(card);
        context.setCard(card);
        bus.post(ActionEvent.of(ActionEventName.MINION_CARD_DRAW, context));
    }

    private void postShuffleEvent() {
        bus.post(ActionEvent.builder()
                .name(ActionEventName.MINION_SHUFFLE)
                .minion(this)
                .source(this)
                .build()
        );
    }

    //cards play
    public void playAllCards(CardContext cardContext) {
        for (Card card : new ArrayList<>(hand)) {
            cardContext.setCard(card);
            bus.post(ActionEvent.of(ActionEventName.MINION_CARD_PLAYED, cardContext));
            card.play(cardContext);
            remove(card);
            Game.delayForAnimation();
        }
    }

    private void remove(Card card) {
        hand.remove(card);
        discard.add(card);
    }

    //fight
    public void applyDamage(Team team, int value) {
        bus.post(ActionEvent.builder()   //todo refactor to factory method
                .name(ActionEventName.MINION_DAMAGED)
                .value(hp - value)
                .minion(this)
                .source(this) //todo czy na pewno potrzebujemy tego source?
                .build()
        );
        hp -= value;
        if (hp <= 0) {
            bus.post(ActionEvent.builder()
                    .name(ActionEventName.MINION_DIED)
                    .minion(this)
                    .ownTeam(team)
                    .source(this)
                    .build()
            );
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
