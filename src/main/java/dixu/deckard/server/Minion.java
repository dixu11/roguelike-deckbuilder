package dixu.deckard.server;

import dixu.deckard.server.event.*;

import java.util.*;
import java.util.stream.Stream;

import static dixu.deckard.server.GameParams.*;

/**
 * {@link Minion}s form a {@link Team} to take a part in a {@link Fight} for their {@link Leader}, controlled by player
 * or a game. Every {@link Minion} has its deck of {@link Card}s and plays {@link GameParams#MINION_DRAW_PER_TURN} number of
 * them every turn automatically. Minion die and leaves a {@link Team} when its {@link Minion#hp} reaches 0.
 */

public class Minion implements ActionEventHandler {
    private final BusManager bus = BusManager.instance();
    private int maxHp =CardType.BASIC_MINION.getValue();
    private int hp = maxHp;
    private final Card minionCard;
    //deck
    private LinkedList<Card> draw = new LinkedList<>();
    private List<Card> hand = new LinkedList<>();
    private List<Card> discarded = new LinkedList<>();
    private volatile Team team;


    public Minion(LeaderType type) {
        minionCard = new Card(CardType.BASIC_MINION);
        minionCard.setOwner(this);
        CardFactory cardFactory = new CardFactory();
        draw.addAll(cardFactory.createDeck(type));
        draw.forEach(card -> card.setOwner(this));
        Collections.shuffle(draw);

        bus.register(this, ActionEventName.LEADER_SPECIAL_STEAL);
        bus.register(this, ActionEventName.LEADER_SPECIAL_UPGRADE);
        bus.register(this, ActionEventName.LEADER_SPECIAL_MOVE_HAND);
    }

    //card draw
    public void drawCards(int count) {
        for (int i = 0; i < count; i++) {
            drawCard();
        }
    }

    private void drawCard() {
        shuffleIfEmpty();

        Card card = draw.poll();
        if (card == null) return;
        Fight.delayForAnimation(DRAW_DELAY_SECONDS);
        addCardToHand(card);
    }

    private void shuffleIfEmpty() {
        if (!draw.isEmpty()) return;

        Collections.shuffle(discarded);
        draw.addAll(discarded);
        discarded.clear();
        postShuffleEvent();
    }

    private void postShuffleEvent() {
        bus.post(ActionEvent.builder()
                .name(ActionEventName.MINION_SHUFFLE)
                .minion(this)
                .source(this)
                .build()
        );
    }

    private void addCardToHand(Card card) {
        card.setOwner(this);
        hand.add(card);
        bus.post(ActionEvent.builder()
                .name(ActionEventName.MINION_CARD_DRAW)
                .ownTeam(team)
                .minion(this)
                .card(card)
                .build()
        );
        postMinionHandChanged();
    }

    //play cards
    public void playAllCards(CardContext cardContext) {
        for (Card card : new ArrayList<>(hand)) {
            cardContext.setCard(card);
            card.play(cardContext);
            discard(card);
            Fight.delayForAnimation(PLAY_DELAY_SECONDS);
        }
    }

    private void discard(Card card) {
        hand.remove(card);
        discarded.add(card);
        bus.post(ActionEvent.builder()
                .name(ActionEventName.MINION_CARD_DISCARDED)
                .ownTeam(team)
                .minion(this)
                .card(card)
                .build()
        );
        postMinionHandChanged();
    }

    //fight
    public void applyRegen(int value) {
        if (hp == maxHp) {
            return;
        }
        int oldValue = hp;
        if (hp < maxHp) {
            hp += value;
        }
        if (hp > maxHp) {
            hp = maxHp;
        }
        bus.post(ActionEvent.builder()
                .name(ActionEventName.MINION_REGENERATED)
                .value(hp)
                .oldValue(oldValue)
                .minion(this)
                .ownTeam(team)
                .build()
        );
    }

    public void applyDamage(int value) {
        if (value <= 0) {
            return;
        }
        int oldValue = hp;
        hp -= value;
        if (hp < 0) {
            hp = 0;
        }
        bus.post(ActionEvent.builder()
                .name(ActionEventName.MINION_DAMAGED)
                .oldValue(oldValue)
                .value(hp)
                .minion(this)
                .build()
        );
        if (hp <= 0) {
            bus.post(ActionEvent.builder()
                    .name(ActionEventName.MINION_DIED)
                    .minion(this)
                    .ownTeam(team)
                    .build()
            );
        }
    }
    //specials handling

    @Override
    public void handle(ActionEvent event) {
        if (event.getMinion() != this) {
            return;
        }

        switch (event.getName()) {
            case LEADER_SPECIAL_UPGRADE -> onUpgradeSpecial(event);
            case LEADER_SPECIAL_STEAL -> onStealSpecial(event);
            case LEADER_SPECIAL_MOVE_HAND -> onMoveHand();
        }
    }

    private void onUpgradeSpecial(ActionEvent event) {
        Card oldCard = event.getOldCard();
        oldCard.setOwner(null);
        int index = hand.indexOf(oldCard);
        Card newCard = event.getCard();
        newCard.setOwner(this);
        hand.set(index, newCard);
        postMinionHandChanged();
    }

    private void onStealSpecial(ActionEvent event) {
        Card card = event.getCard();
        card.setOwner(null);  // todo because cards have to have their owners for their effects i have to update this every time card changes owner... can i avoid this??
        hand.remove(card);
        postMinionHandChanged(); //todo make hand separate object to post hand change every time?
        drawCard();
    }

    private void onMoveHand() {
        if (!hand.isEmpty()) {
            discard(hand.remove(0));
            drawCard();
        }
    }
    //for tests

    public void clearDraw() {
        draw.clear();
    }
    //getters / setters

    public int getHealth() {
        return hp;
    }

    public List<Card> getHand() {
        return hand;
    }

    public Queue<Card> getDraw() {
        return draw;
    }

    public List<Card> getDiscarded() {
        return discarded;
    }

    public Card getMinionCard() {
        return minionCard;
    }

    public void setHand(List<Card> newHand) {
        newHand.forEach(card -> card.setOwner(this));
        hand = newHand;
        postMinionHandChanged();
    }

    public void setDiscard(List<Card> cards) {
        cards.forEach(card -> card.setOwner(this));
        discarded = cards;
        //todo post discard changed?
        //todo move it to new "deck" entity for all this complexity?
    }

    public void setDraw(List<Card> cards) {
        cards.forEach(card -> card.setOwner(this));
        draw =new LinkedList<>(cards);
        //todo post discard changed?
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void postMinionHandChanged() {
        bus.post(ActionEvent.builder()
                .name(ActionEventName.MINION_HAND_CHANGED)
                .minion(this)
                .ownTeam(team)
                .build()
        );
    }

    public List<Card> getAllCards() {
        return Stream.of(hand, discarded, draw)
                .flatMap(Collection::stream)
                .toList();
    }
}
