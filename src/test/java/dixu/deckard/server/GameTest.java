package dixu.deckard.server;

import dixu.deckard.server.event.BusManager;
import dixu.deckard.server.event.CoreEvent;
import dixu.deckard.server.event.CoreEventName;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static dixu.deckard.server.GameParams.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

//tests for core game functionalities
class GameTest {

    private Team firstTeam;
    private Team secondTeam;
    private BusManager bus;

    @BeforeEach
    public void before() {
        Game.disableDaley();
        bus = BusManager.instance();
        TeamFactory factory = new TeamFactory();
        firstTeam = factory.createFirst();
        secondTeam = factory.createCreateSecond();
        Game game = new Game(firstTeam, secondTeam);
        game.start();
    }

    @AfterEach
    public void after() {
        //Buses are singleton and also needs to be re-initialized after each test
        BusManager.reset();
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
        disableClearBlock();
        giveAllMinionsBlockCard();

        bus.post(CoreEvent.of(CoreEventName.TURN_ENDED));
        int blockFromCards = DEFAULT_BLOCK_VALUE* MINION_PER_TEAM;

        assertEquals(blockFromCards, firstTeam.getBlock());
        assertEquals(blockFromCards + SECOND_TEAM_INITIAL_BLOCK, secondTeam.getBlock());
    }

    @Test
    @DisplayName("Block is cleared for every team, first team - after plays, second - before plays")
    public void test5() {
        giveAllMinionsBlockCard();

        bus.post(CoreEvent.of(CoreEventName.TURN_ENDED));
        int blockFromCards = DEFAULT_BLOCK_VALUE* MINION_PER_TEAM;

        assertEquals(0, firstTeam.getBlock());
        assertEquals(blockFromCards, secondTeam.getBlock());
    }

    private void disableClearBlock() {
        firstTeam.setClearBlockEnabled(false);
        secondTeam.setClearBlockEnabled(false);
    }

    private List<Minion> allMinions() {
        List<Minion> all = new ArrayList<>();
        all.addAll(firstTeam.getMinions());
        all.addAll(secondTeam.getMinions());
        return all;
    }

    private void giveAllMinionsBlockCard() {
        allMinions().forEach(minion -> composeMinionHand(minion,CardType.BLOCK));
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
