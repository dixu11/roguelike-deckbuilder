package dixu.deckard.server;

import dixu.deckard.LeaderFactory;
import dixu.deckard.server.event.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static dixu.deckard.server.GameParams.*;
import static org.junit.jupiter.api.Assertions.*;

//tests for core game functionalities
class FightTest {

    private Team firstTeam;
    private Team secondTeam;
    private BusManager bus;

    @BeforeEach
    public void before() {
        setClassicParams();
        loadGame(); //remove if performance get worse because tests with custom params load engine twice
    }
    @AfterEach
    public void after() {
        //Buses are singleton and also needs to be re-initialized after each test
        BusManager.reInitialize();
    }

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
            assertEquals(0, minion.getDiscard().size());
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
            assertEquals(0, minion.getDiscard().size());
        }
    }

    private void setClassicParams() {
        MINION_PER_TEAM = 2;
        INITIAL_MINION_DECK_SIZE = 4;
        MINION_DRAW_PER_TURN = 2;
        MINION_INITIAL_HP = 3;
        SECOND_TEAM_INITIAL_BLOCK = 3;
        DEFAULT_BLOCK_VALUE = 1;
        DEFAULT_ATTACK_VALUE = 2;
    }

    private void loadGame() { //todo refactor copying same code here and in App
        Fight.disableDaley();
        bus = BusManager.instance();
        LeaderFactory leaderFactory = new LeaderFactory();
        Leader firstLeader = leaderFactory.create();
        Leader secondLeader = leaderFactory.create();
        firstTeam = firstLeader.getTeam();
        secondTeam = secondLeader.getTeam();
        Fight fight = new Fight(firstLeader,secondLeader);
        fight.start();
    }

    private void reloadGame() {
        after(); //reset bus
        loadGame();
    }

    private void executeTurn() {
        bus.post(CoreEvent.of(CoreEventName.TURN_ENDED));
    }

    private List<Minion> allMinions() {
        List<Minion> all = new ArrayList<>();
        all.addAll(firstTeam.getMinions());
        all.addAll(secondTeam.getMinions());
        return all;
    }
    //todo could not find way to avoid repetition - my try was EventName interface but i can't figure out how to

    //put it back to overloaded  bus.register() call - it makes compile error -> 'suspicious call'
    private <T> AtomicBoolean listenEventPosted(CoreEventName eventName) {
        //  T elem = eventName.getObject();
        AtomicBoolean wasPosted = new AtomicBoolean(false);
        bus.register(event -> wasPosted.set(true), eventName); // if i pass elem here - there's a problem
        return wasPosted;
    }

    private AtomicBoolean listenEventPosted(ActionEventName eventName) {
        AtomicBoolean wasPosted = new AtomicBoolean(false);
        bus.register(event -> wasPosted.set(true), eventName);
        return wasPosted;
    }

    private void failIfWasNotPosted(AtomicBoolean wasPosted) {
        if (!wasPosted.get()) {
            fail();
        }
    }

    private void clearMinionsHand(Team team) {
        giveMinionsCards(team);
    }

    private void disableBlockClear() {
        firstTeam.setClearBlockEnabled(false);
        secondTeam.setClearBlockEnabled(false);
    }

    /**
     * @param cards when no elements are passed minion has no cards in hand
     */
    private void giveMinionsCards(Team team, CardType... cards) {
        team.getMinions()
                .forEach(minion -> composeMinionHand(minion, cards));
    }

    private void giveAllMinionsBlockCard() {
        allMinions().forEach(minion -> composeMinionHand(minion, CardType.BLOCK));
    }

    private void composeMinionHand(Minion minion, CardType... cards) {
        CardFactory factory = new CardFactory();
        List<Card> newHand = new ArrayList<>();
        for (CardType type : cards) {
            newHand.addAll(factory.createCards(1, type));
        }
        minion.setHand(newHand);
    }
}
