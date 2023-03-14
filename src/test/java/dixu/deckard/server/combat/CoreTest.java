package dixu.deckard.server.combat;

import dixu.deckard.server.FunctionalTest;
import dixu.deckard.server.card.CardFactory;
import dixu.deckard.server.card.CardType;
import dixu.deckard.server.event.*;
import dixu.deckard.server.minion.Minion;
import org.junit.jupiter.api.*;

import java.util.concurrent.atomic.AtomicBoolean;

import static dixu.deckard.server.game.GameParams.*;
import static org.junit.jupiter.api.Assertions.*;

//tests for core game functionalities
public class CoreTest extends FunctionalTest {


    @Test
    @DisplayName("On start teams have correct block")
    public void test1() {
      disableBasicBlock();
        assertEquals(SECOND_TEAM_INITIAL_BLOCK, secondTeam.getBlock());
    }

    @Test
    @DisplayName("On start all minions draw two cards, has 0 discarded and 2 in draw deck")
    public void test2() {
        for (Minion minion : allMinions()) {
            assertEquals(MINION_DRAW_PER_TURN, minion.getHand().size());
            assertEquals(CardFactory.INITIAL_MINION_DECK_SIZE - MINION_DRAW_PER_TURN,
                    minion.getDraw().size());
            assertEquals(0, minion.getDiscarded().size());
        }
    }

    @Test
    @DisplayName("On start all minions have correct hp")
    public void test3() {
        for (Minion minion : allMinions()) {
            assertEquals(minionInitialHp(), minion.getHealth());
        }
    }

    @Test
    @DisplayName("Block card gives block to team")
    public void test4() {
        disableBlockClear();
        giveAllMinionsBlockCard();
        int blockFromCards = CardType.BASIC_BLOCK.getValue() * MINION_PER_TEAM;

        executeTurn();

        assertEquals(blockFromCards, firstTeam.getBlock());
        assertEquals(blockFromCards + SECOND_TEAM_INITIAL_BLOCK, secondTeam.getBlock());
    }

    @Test
    @DisplayName("Block is cleared for every team, first team - after plays, second - before plays")
    public void test5() {
        giveAllMinionsBlockCard();
        int blockFromCards = CardType.BASIC_BLOCK.getValue() * MINION_PER_TEAM;

        executeTurn();

        assertEquals(0, firstTeam.getBlock());
        assertEquals(blockFromCards, secondTeam.getBlock());
    }

    @Test
    @DisplayName("After slaying all minions enemy has empty team and game ends")
    public void test6() {
        disableBasicBlock();
        changeCardBaseValueTo(CardType.BASIC_ATTACK, 3);
        reloadGame();
        giveMinionsCards(firstTeam, CardType.BASIC_ATTACK, CardType.BASIC_ATTACK);
        giveMinionsCards(secondTeam, CardType.BASIC_BLOCK);
        AtomicBoolean wasPosted = listenEventPosted(CoreEventType.COMBAT_OVER);

        executeTurn();

        assertTrue(secondTeam.getMinions().isEmpty());
        failIfWasNotPosted(wasPosted);
    }

    @Test
    @DisplayName("After character died proper event is post and it's no longer in team")
    public void test7() {
        changeCardBaseValueTo(CardType.BASIC_ATTACK, 3);
        reloadGame();
        giveMinionsCards(firstTeam, CardType.BASIC_ATTACK);
        clearMinionsHand(secondTeam);
        AtomicBoolean wasPosted = listenEventPosted(ActionEventType.MINION_DIED);

        executeTurn();

        failIfWasNotPosted(wasPosted);
        assertEquals(MINION_PER_TEAM - 1, secondTeam.getMinions().size());
    }

    @Test
    @DisplayName("Block is reduced after attack")
    public void test8() {
        MINION_PER_TEAM = 1;
        changeCardBaseValueTo(CardType.BASIC_ATTACK, 2);
        SECOND_TEAM_INITIAL_BLOCK = 3;
        reloadGame();
        disableBlockClear();
        giveMinionsCards(firstTeam, CardType.BASIC_ATTACK);
        clearMinionsHand(secondTeam);

        executeTurn();

        assertEquals(1, secondTeam.getBlock());
    }

    @Test
    @DisplayName("Attack over block hit minion")
    public void test9() {
        MINION_PER_TEAM = 1;
        changeCardBaseValueTo(CardType.BASIC_ATTACK, 5);
        reloadGame();
        giveMinionsCards(firstTeam, CardType.BASIC_ATTACK);
        clearMinionsHand(secondTeam);

        executeTurn();

        Minion theOnlyOneMinion = secondTeam.getRandomMinion().get();
        assertEquals(minionInitialHp() + SECOND_TEAM_INITIAL_BLOCK - CardType.BASIC_ATTACK.getValue(),
                theOnlyOneMinion.getHealth());
    }

    @Test
    @DisplayName("After all minion cards were played discard deck is shuffled and put as draw deck")
    public void test10() {
        disableBasicAttack();
        MINION_DRAW_PER_TURN = 1;
        reloadGame();

        for (int i = 0; i < CardFactory.INITIAL_MINION_DECK_SIZE; i++) {
            executeTurn();
        }

        for (Minion minion : allMinions()) {
            assertEquals(MINION_DRAW_PER_TURN, minion.getHand().size());
            assertEquals(CardFactory.INITIAL_MINION_DECK_SIZE - MINION_DRAW_PER_TURN,
                    minion.getDraw().size());
            assertEquals(0, minion.getDiscarded().size());
        }
    }

    @Test
    @DisplayName("Minions can have small number of cards")
    public void test11() {
      disableBasicAttack();
        Minion minionWithTwoCards = secondTeam.getRandomMinion().get();
        minionWithTwoCards.clearDraw();
        Minion minionWithOneCard = firstTeam.getMinions().get(0);
        minionWithOneCard.clearDraw();
        composeMinionHand(minionWithOneCard, CardType.BASIC_ATTACK);
        Minion minionWithNoCards = firstTeam.getMinions().get(1);
        composeMinionHand(minionWithNoCards);
        minionWithNoCards.clearDraw();

        for (int i = 0; i < 20; i++) {
            executeTurn();
        }

        //no fail? success!
    }
}
