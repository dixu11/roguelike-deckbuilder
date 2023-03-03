package dixu.deckard.server;

import dixu.deckard.server.event.*;

import java.util.*;

import static dixu.deckard.server.GameParams.MINION_INITIAL_HP;

/**
 * {@link Minion}s form a {@link Team} to take a part in a {@link Fight} for their {@link Leader}, controlled by player
 * or a game. Every {@link Minion} has its deck of {@link Card}s and plays {@link GameParams#MINION_DRAW_PER_TURN} number of
 * them every turn automatically. Minion die and leaves a {@link Team} when its {@link Minion#hp} reaches 0.
* */

public class Minion implements ActionEventHandler{
    private static int nextId = 1;
    private final BusManager bus = BusManager.instance();
    private int hp = MINION_INITIAL_HP;
    private final Card minionCard;
    private final LinkedList<Card> draw = new LinkedList<>();
    private List<Card> hand = new LinkedList<>();
    private final List<Card> discard = new LinkedList<>();
    private Team team;


    public Minion(LeaderType type) {
        String name = "Minion " + nextId++;
        minionCard = new Card(CardType.MINION, name, hp);
        CardFactory cardFactory = new CardFactory();
        draw.addAll(cardFactory.createDeck(type));
        Collections.shuffle(draw);

        bus.register(this,ActionEventName.LEADER_SPECIAL_UPGRADE);
    }

    //card draw
    public void drawCards(int count, CardContext context) { //todo simplify context couse we now have reference to team
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
            Fight.delayForAnimation();
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

    @Override
    public void handle(ActionEvent event) {
        if (event.getName() == ActionEventName.LEADER_SPECIAL_UPGRADE && event.getMinion() == this) {
            int index = hand.indexOf(event.getOldCard());
            hand.set(index, event.getCard());
            bus.post(ActionEvent.builder()
                    .name(ActionEventName.MINION_HAND_CHANGED)
                    .minion(this)
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

    public void setHand(List<Card> newHand) {
        hand = newHand;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
