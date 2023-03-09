package dixu.deckard.server.minion;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.card.CardContext;
import dixu.deckard.server.card.CardFactory;
import dixu.deckard.server.card.CardType;
import dixu.deckard.server.event.*;
import dixu.deckard.server.fight.Fight;
import dixu.deckard.server.game.GameParams;
import dixu.deckard.server.leader.Leader;
import dixu.deckard.server.leader.LeaderType;
import dixu.deckard.server.team.Team;

import java.util.*;
import java.util.stream.Stream;

import static dixu.deckard.server.game.GameParams.*;

/**
 * {@link Minion}s form a {@link Team} to take a part in a {@link Fight} for their {@link Leader}, controlled by player
 * or a game. Every {@link Minion} has its deck of {@link Card}s and plays {@link GameParams#MINION_DRAW_PER_TURN} number of
 * them every turn automatically. Minion die and leaves a {@link Team} when its {@link Minion#hp} reaches 0.
 */

public class Minion implements ActionEventHandler {
    private final BusManager bus = BusManager.instance();
    private final Card minionCard;
    //deck
    private final MinionDeck deck;
    //stats
    private final int maxHp = CardType.BASIC_MINION.getValue();
    private int hp = maxHp;
    //other
    private Team team;

    public Minion(MinionDeck deck) {
        minionCard = new Card(CardType.BASIC_MINION);
        minionCard.setOwner(this);

        this.deck = deck;
        deck.setMinion(this);

        bus.register(this, ActionEventName.LEADER_SPECIAL_STEAL);
        bus.register(this, ActionEventName.LEADER_SPECIAL_UPGRADE);
        bus.register(this, ActionEventName.LEADER_SPECIAL_MOVE_HAND);
    }

    //card draw
    public void drawCards(int count) {
        for (int i = 0; i < count; i++) {
            deck.drawCard();
        }
    }

    public void setHand(List<Card> newHand) {
        deck.setHand(newHand);
    }

    public void setDiscard(List<Card> cards) {
     deck.setDiscard(cards);
    }

    public void setDraw(List<Card> cards) {
     deck.setDraw(cards);
    }
    public void clearDraw() {
       deck.clearDraw();
    }

    public List<Card> getAllCards() {
        return deck.getAllCards();
    }
    //play cards

    void discard(Card card) {
     deck.discard(card);
    }

    public void playAllCards(CardContext cardContext) {
     deck.playAllCards(cardContext);
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
            case LEADER_SPECIAL_UPGRADE -> deck.onUpgradeSpecial(event);
            case LEADER_SPECIAL_STEAL -> deck.onStealSpecial(event);
            case LEADER_SPECIAL_MOVE_HAND -> deck.onMoveHand();
        }
    }


    //for tests
    public boolean isWounded() {
        return hp != maxHp;
    }

    //getters / setters

    public int getHealth() {
        return hp;
    }

    public List<Card> getHand() {
        return deck.getHand();
    }

    public List<Card> getDraw() {
        return deck.getDraw();
    }

    public List<Card> getDiscarded() {
        return deck.getDiscarded();
    }

    public Card getMinionCard() {
        return minionCard;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }
}
