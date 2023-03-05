package dixu.deckard.server;

import dixu.deckard.server.event.ActionEvent;
import dixu.deckard.server.event.ActionEventName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dixu.deckard.server.GameParams.*;
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
        Minion minion = secondTeam.getMinions().get(0);
        List<Card> minionHand = minion.getHand();
        Card cardToSteal = minionHand.get(0);

        bus.post(ActionEvent.builder()
                .name(ActionEventName.LEADER_SPECIAL_STEAL)
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
    @DisplayName("When leader use upgrade special he spends energy, and change minion card to new one")
    public void test3() {
        Minion minion = firstTeam.getMinions().get(0);
        List<Card> minionHand = minion.getHand();
        Card minionCardToReplace = minionHand.get(0);
        CardFactory factory = new CardFactory();
        Card leaderCard = factory.createCard(CardType.BASIC_ATTACK);
        firstLeader.addCard(leaderCard);

        bus.post(ActionEvent.builder()
                .name(ActionEventName.LEADER_SPECIAL_UPGRADE)
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
        Minion minion = firstTeam.getMinions().get(0);
        List<Card> minionHand = minion.getHand();
        Card minionFirstCard = minionHand.get(0);

        bus.post(ActionEvent.builder()
                .name(ActionEventName.LEADER_SPECIAL_MOVE_HAND)
                .leader(firstLeader)
                .minion(minion)
                .build()
        );

        assertFalse(minionHand.contains(minionFirstCard));
        assertSame(minion.getDiscarded().get(0), minionFirstCard);
        assertEquals(
                initialMinionDeckSize() - MINION_DRAW_PER_TURN - 1,
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

        assertEquals(INITIAL_ENERGY,firstLeader.getEnergy());
    }
}
