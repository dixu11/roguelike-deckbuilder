package dixu.deckard.server.leader;

import dixu.deckard.server.FunctionalTest;
import dixu.deckard.server.card.Card;
import dixu.deckard.server.card.CardFactory;
import dixu.deckard.server.card.CardType;
import dixu.deckard.server.event.ActionEvent;
import dixu.deckard.server.event.ActionEventSubtype;
import dixu.deckard.server.event.ActionEventType;
import dixu.deckard.server.event.bus.Bus;
import dixu.deckard.server.minion.Minion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dixu.deckard.server.game.GameParams.*;
import static org.junit.jupiter.api.Assertions.*;

public class LeaderTest extends FunctionalTest {

    @Test
    @DisplayName("Player leader starts with 0 cards, computer with 0")
    public void test1() {
        assertEquals(0, firstLeader.getHand().size());
        assertEquals(0, secondLeader.getHand().size());
    }

    @Test
    @DisplayName("When leader use steal special - spends energy and steal a card, minion draws new one")
    public void test2() {
        Minion minion = firstMinion(secondTeam);
        List<Card> minionHand = minion.getHand();
        Card cardToSteal = minionHand.get(0);

        Bus.post(ActionEvent.builder()
                .type(ActionEventType.LEADER_SPECIAL_STEAL)
                .subtype(ActionEventSubtype.STEAL_TO_LEADER)
                .leader(firstLeader)
                .minion(minion)
                .card(cardToSteal)
                .build()
        );

        assertFalse(minionHand.contains(cardToSteal));
        assertEquals(firstLeader.getHand().get(0), cardToSteal);
        assertEquals(INITIAL_ENERGY - STEAL_SPECIAL_COST, firstLeader.getEnergy());
        assertEquals(MINION_DRAW_PER_TURN, minionHand.size());
    }

    @Test
    @DisplayName("When leader use give special he spends energy, and change minion card to new one")
    public void test3() {
        Minion minion = firstMinion(firstTeam);
        List<Card> minionHand = minion.getHand();
        Card minionCardToReplace = minionHand.get(0);
        CardFactory factory = new CardFactory();
        Card leaderCard = factory.createCard(CardType.BASIC_ATTACK);
        firstLeader.addCard(leaderCard);

        Bus.post(ActionEvent.builder()
                .type(ActionEventType.LEADER_SPECIAL_GIVE)
                .leader(firstLeader)
                .minion(minion)
                .card(leaderCard)
                .oldCard(minionCardToReplace)
                .build()
        );

        assertTrue(minionHand.contains(leaderCard));
        assertFalse(minionHand.contains(minionCardToReplace));
        assertTrue(firstLeader.getHand().isEmpty());
        assertEquals(INITIAL_ENERGY - UPGRADE_SPECIAL_COST, firstLeader.getEnergy());
        assertEquals(MINION_DRAW_PER_TURN, minionHand.size());
    }

    @Test
    @DisplayName("When leader uses move minion hand special, spends energy, and make minion draw card and discard card")
    public void test4() {
        Minion minion = firstMinion(firstTeam);
        List<Card> minionHand = minion.getHand();
        Card minionFirstCard = minionHand.get(0);

        Bus.post(ActionEvent.builder()
                .type(ActionEventType.LEADER_SPECIAL_MOVE_HAND)
                .leader(firstLeader)
                .minion(minion)
                .build()
        );

        assertFalse(minionHand.contains(minionFirstCard));
        assertSame(minion.getDiscarded().get(0), minionFirstCard);
        assertEquals(
                CardFactory.INITIAL_MINION_DECK_SIZE - MINION_DRAW_PER_TURN - 1,
                minion.getDraw().size());
        assertEquals(INITIAL_ENERGY - MOVE_SPECIAL_COST, firstLeader.getEnergy());
    }

    @Test
    @DisplayName("Leader has hand size limit")
    public void test5() {
        CardFactory factory = new CardFactory();
        Card firstCard = factory.createCard(CardType.BASIC_BLOCK);

        firstLeader.addCard(firstCard);
        for (int i = 0; i < LEADER_MAX_HAND_SIZE; i++) {
            firstLeader.addCard(factory.createCard(CardType.BASIC_ATTACK));
        }

        assertEquals(LEADER_MAX_HAND_SIZE, firstLeader.getHand().size());
        assertFalse(firstLeader.getHand().contains(firstCard));
    }

    @Test
    @DisplayName("after every turn leader regenerate energy")
    public void test6() {
        firstLeader.setEnergy(1);

        executeTurn();

        assertEquals(INITIAL_ENERGY, firstLeader.getEnergy());
    }

    @Test
    @DisplayName("When leader use swap steal special - spends energy and swap stolen card with selected old one")
    public void test7() {
        Minion ownMinion = firstMinion(firstTeam);
        Minion enemyMinion = firstMinion(secondTeam);
        List<Card> ownMinionHand = ownMinion.getHand();
        List<Card> enemyMinionHand = enemyMinion.getHand();
        Card cardToExchange = ownMinionHand.get(0);
        Card cardToSteal = enemyMinionHand.get(0);

        Bus.post(ActionEvent.builder()
                .type(ActionEventType.LEADER_SPECIAL_STEAL)
                .subtype(ActionEventSubtype.STEAL_TO_SWAP)
                .leader(firstLeader)
                .minion(ownMinion)
                .targetMinion(enemyMinion)
                .card(cardToSteal)
                .oldCard(cardToExchange)
                .build()
        );

        assertTrue(ownMinionHand.contains(cardToSteal));
        assertFalse(ownMinionHand.contains(cardToExchange));
        assertFalse(enemyMinionHand.contains(cardToSteal));
        assertTrue(enemyMinionHand.contains(cardToExchange));
        assertEquals(MINION_DRAW_PER_TURN,ownMinionHand.size());
        assertEquals(MINION_DRAW_PER_TURN,enemyMinionHand.size());
        assertEquals(0,firstLeader.getHand().size());
        assertEquals(INITIAL_ENERGY - STEAL_SPECIAL_COST, firstLeader.getEnergy());
    }
}
