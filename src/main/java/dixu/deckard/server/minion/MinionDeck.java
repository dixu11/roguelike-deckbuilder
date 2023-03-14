package dixu.deckard.server.minion;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.card.CardContext;
import dixu.deckard.server.event.ActionEvent;
import dixu.deckard.server.event.ActionEventSubtype;
import dixu.deckard.server.event.ActionEventType;
import dixu.deckard.server.event.bus.Bus;
import dixu.deckard.server.combat.Combat;

import java.util.*;
import java.util.stream.Stream;

import static dixu.deckard.server.game.GameParams.DRAW_DELAY_SECONDS;
import static dixu.deckard.server.game.GameParams.PLAY_DELAY_SECONDS;

/**
 * All {@link Minion}'s cards. This class hold rules about how {@link Minion}s {@link Card}s should behave. When card is
 * drawn goes to the hand. When card is played goes to the discard. When draw is empty discard is shuffled and put to
 * draw.
 * <p>
 * On every deck change proper event should be posted.
 */
public class MinionDeck {
    private LinkedList<Card> draw = new LinkedList<>();
    private List<Card> hand = new LinkedList<>();
    private List<Card> discarded = new LinkedList<>();
    private Minion minion;

    public MinionDeck(List<Card> cards) {
        draw.addAll(cards);
    }

    void setMinion(Minion minion) {
        this.minion = minion;
        draw.forEach(card -> card.setOwner(minion));
    }

    void drawCard() {
        shuffleIfEmpty();
        Card card = draw.poll();
        if (card == null) return;
        Combat.delayForAnimation(DRAW_DELAY_SECONDS);
        addCardToHand(card);
    }

    private void addCardToHand(Card card) {
        card.setOwner(minion);
        hand.add(card);
        Bus.post(ActionEvent.builder()
                .type(ActionEventType.MINION_CARD_DRAW)
                .ownTeam(minion.getTeam())
                .minion(minion)
                .card(card)
                .build()
        );
        postMinionHandChanged();
    }

    private void shuffleIfEmpty() {
        if (!draw.isEmpty()) return;
        Collections.shuffle(discarded);
        draw.addAll(discarded);
        discarded.clear();
        postProportionsChanged();
    }

    void postMinionHandChanged() {
        Bus.post(ActionEvent.builder()
                .type(ActionEventType.MINION_HAND_CHANGED)
                .minion(minion)
                .ownTeam(minion.getTeam())
                .build()
        );
        postProportionsChanged();
    }

    void setHand(List<Card> newHand) {
        newHand.forEach(card -> card.setOwner(minion));
        hand = newHand;
        postMinionHandChanged();
    }

    void setDiscard(List<Card> cards) {
        cards.forEach(card -> card.setOwner(minion));
        discarded = cards;
        postProportionsChanged();
        //todo post discard changed?
        //todo move it to new "deck" entity for all this complexity?
    }

    void setDraw(List<Card> cards) {
        cards.forEach(card -> card.setOwner(minion));
        draw = new LinkedList<>(cards);
        postProportionsChanged();
        //todo post discard changed?
    }

    void clearDraw() {
        draw.clear();
        postProportionsChanged();
    }

    List<Card> getAllCards() {
        return Stream.of(hand, discarded, draw)
                .flatMap(Collection::stream)
                .toList();
    }


    void discardAction(Card card) {
        Bus.post(ActionEvent.builder()
                .type(ActionEventType.MINION_CARD_DISCARDED)
                .minion(minion)
                .ownTeam(minion.getTeam())
                .card(card)
                .build()
        );
        discard(card);
    }

    private void discard(Card card) {
        hand.remove(card);
        discarded.add(card);
        postMinionHandChanged();
    }

    private void replaceHandCard(Card card, Card oldCard) {
        hand.set(hand.indexOf(oldCard), card);
        postMinionHandChanged();
    }

    public void playAllCards(CardContext cardContext) {
        for (Card card : new ArrayList<>(hand)) {
            cardContext.setCard(card);
            card.play(cardContext);
            discardAction(card);
            Combat.delayForAnimation(PLAY_DELAY_SECONDS);
        }
    }

    void onGiveSpecial(ActionEvent event) {
        if (event.getTargetMinion() != minion) {
            return;
        }
        if (event.getSubtype() == ActionEventSubtype.GIVE_TO_HAND) {
            Card oldCard = event.getOldCard();
            oldCard.setOwner(null);
            int index = hand.indexOf(oldCard);
            Card newCard = event.getCard();
            newCard.setOwner(minion);
            hand.set(index, newCard);
            postMinionHandChanged();
        } else if (event.getSubtype() == ActionEventSubtype.GIVE_TO_DECK) {
            discarded.add(event.getCard());
            postProportionsChanged();
            //todo but if it is block? will Deck Block card react?
        }

    }

    void onStealSpecial(ActionEvent event) {
        if (event.getMinion().equals(minion) && event.getSubtype() == ActionEventSubtype.STEAL_TO_LEADER) {
            Card card = event.getCard();
            card.setOwner(null);
            hand.remove(card);
            postMinionHandChanged();
            drawCard();
        }
        if (event.getSubtype() == ActionEventSubtype.STEAL_TO_DECK) {
            if (event.getTargetMinion().equals(minion)) {
                discarded.add(event.getCard());
                postProportionsChanged();
                //todo but if it is block? will Deck Block card react?
            } else {
                Card card = event.getCard();
                hand.remove(card);
                postMinionHandChanged();
                drawCard();
            }
        }
        if (event.getSubtype() == ActionEventSubtype.STEAL_TO_SWAP) {
            if (event.getTargetMinion().equals(minion)) { //enemy minion
                replaceHandCard(event.getOldCard(), event.getCard());
            } else {
                replaceHandCard(event.getCard(), event.getOldCard());
            }
        }
    }

    void onMoveHand(ActionEvent event) {
        if (event.getTargetMinion() != minion) {
            return;
        }
        if (!hand.isEmpty()) {
            discardAction(hand.remove(0));
            drawCard();
        }
    }

    private void postProportionsChanged() {
        Bus.post(ActionEvent.builder()
                .type(ActionEventType.MINION_DECK_PROPORTIONS_CHANGED)
                .minion(minion)
                .ownTeam(minion.getTeam())
                .build()
        );
    }

    List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }

    List<Card> getDraw() {
        return Collections.unmodifiableList(draw);
    }

    List<Card> getDiscarded() {
        return Collections.unmodifiableList(discarded);
    }
}
