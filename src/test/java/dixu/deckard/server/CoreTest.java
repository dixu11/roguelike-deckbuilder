package dixu.deckard.server;

import dixu.deckard.server.event.*;
import org.junit.jupiter.api.*;

import java.util.concurrent.atomic.AtomicBoolean;

import static dixu.deckard.server.GameParams.*;
import static org.junit.jupiter.api.Assertions.*;

//tests for core game functionalities
class CoreTest extends FunctionalTest{



    @Test
    @DisplayName("On start teams have correct block")
    public void test1() {
        assertEquals(0, firstTeam.getBlock());
        assertEquals(SECOND_TEAM_INITIAL_BLOCK, secondTeam.getBlock());
    }

    @Test
    @DisplayName("On start all minions draw two cards, has 0 discarded and 2 in draw deck")
    public void test2() {
        for (Minion minion : allMinions()) {
            assertEquals(MINION_DRAW_PER_TURN, minion.getHand().size());
            assertEquals(INITIAL_MINION_DECK_SIZE - MINION_DRAW_PER_TURN,
                    minion.getDraw().size());
            assertEquals(0, minion.getDiscarded().size());
        }
    }

    @Test
    @DisplayName("On start all minions have correct hp")
    public void test3() {
        for (Minion minion : allMinions()) {
            assertEquals(MINION_INITIAL_HP, minion.getHealth());
        }
    }

    @Test
    @DisplayName("Block card gives block to team")
    public void test4() {
        disableBlockClear();
        giveAllMinionsBlockCard();
        int blockFromCards = DEFAULT_BLOCK_VALUE * MINION_PER_TEAM;

        executeTurn();

        assertEquals(blockFromCards, firstTeam.getBlock());
        assertEquals(blockFromCards + SECOND_TEAM_INITIAL_BLOCK, secondTeam.getBlock());
    }

    @Test
    @DisplayName("Block is cleared for every team, first team - after plays, second - before plays")
    public void test5() {
        giveAllMinionsBlockCard();
        int blockFromCards = DEFAULT_BLOCK_VALUE * MINION_PER_TEAM;

        executeTurn();

        assertEquals(0, firstTeam.getBlock());
        assertEquals(blockFromCards, secondTeam.getBlock());
    }

    @Test
    @DisplayName("After slaying all minions enemy has empty team and game ends")
    public void test6() {
        DEFAULT_BLOCK_VALUE = 0;
        DEFAULT_ATTACK_VALUE = 3;
        reloadGame();
        giveMinionsCards(firstTeam, CardType.ATTACK, CardType.ATTACK);
        giveMinionsCards(secondTeam, CardType.BLOCK);
        AtomicBoolean wasPosted = listenEventPosted(CoreEventName.GAME_OVER);

        executeTurn();

        assertTrue(secondTeam.getMinions().isEmpty());
        failIfWasNotPosted(wasPosted);
    }

    @Test
    @DisplayName("After character died proper event is post and it's no longer in team")
    public void test7() {
        DEFAULT_ATTACK_VALUE = 3;
        reloadGame();
        giveMinionsCards(firstTeam, CardType.ATTACK);
        clearMinionsHand(secondTeam);
        AtomicBoolean wasPosted = listenEventPosted(ActionEventName.MINION_DIED);

        executeTurn();

        failIfWasNotPosted(wasPosted);
        assertEquals(MINION_PER_TEAM - 1, secondTeam.getMinions().size());
    }

    @Test
    @DisplayName("Block is reduced after attack")
    public void test8() {
        MINION_PER_TEAM = 1;
        DEFAULT_ATTACK_VALUE = 2;
        SECOND_TEAM_INITIAL_BLOCK = 3;
        reloadGame();
        disableBlockClear();
        giveMinionsCards(firstTeam, CardType.ATTACK);
        clearMinionsHand(secondTeam);

        executeTurn();

        assertEquals(1, secondTeam.getBlock());
    }

    @Test
    @DisplayName("Attack over block hit minion")
    public void test9() {
        MINION_PER_TEAM = 1;
        DEFAULT_ATTACK_VALUE = 5;
        reloadGame();
        giveMinionsCards(firstTeam, CardType.ATTACK);
        clearMinionsHand(secondTeam);

        executeTurn();

        Minion theOnlyOneMinion = secondTeam.getRandomMinion().get();
        assertEquals(MINION_INITIAL_HP + SECOND_TEAM_INITIAL_BLOCK - DEFAULT_ATTACK_VALUE,
                theOnlyOneMinion.getHealth());
    }

    //todo niezdane testy
    @Test
    @DisplayName("After all minion cards were played discard deck is shuffled and put as draw deck")
    public void test10() {
        DEFAULT_ATTACK_VALUE = 0;
        reloadGame();

        executeTurn();
        executeTurn();

        for (Minion minion : allMinions()) {
            assertEquals(MINION_DRAW_PER_TURN, minion.getHand().size());
            assertEquals(INITIAL_MINION_DECK_SIZE - MINION_DRAW_PER_TURN,
                    minion.getDraw().size());
            assertEquals(0, minion.getDiscarded().size());
        }
    }


}
