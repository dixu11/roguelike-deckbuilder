package dixu.deckard.server;

import dixu.deckard.server.event.BusManager;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static dixu.deckard.server.GameParams.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

//tests for core game functionalities
class GameTest {

    private Team firstTeam;
    private Team secondTeam;

    @BeforeEach
    public void before() {
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
    public void test3() {
        assertEquals(0, firstTeam.getBlock());
        assertEquals(SECOND_TEAM_INITIAL_BLOCK, secondTeam.getBlock());
    }

    @Test
    @DisplayName("On start all minions draw two cards, has 0 discarded and 2 in draw deck")
    public void test1() {
        for (Minion minion : allMinions()) {
            assertEquals(MINION_DRAW_PER_TURN, minion.getHand().size());
            assertEquals(INITIAL_MINION_DECK_SIZE - MINION_DRAW_PER_TURN,
                    minion.getDraw().size());
            assertEquals(0, minion.getDiscard().size());
        }
    }

    @Test
    @DisplayName("On start all minions have correct hp")
    public void test2() {
        for (Minion minion : allMinions()) {
            assertEquals(MINION_INITIAL_HP, minion.getHealth());
        }
    }

    private List<Minion> allMinions() {
        List<Minion> all = new ArrayList<>();
        all.addAll(firstTeam.getMinions());
        all.addAll(secondTeam.getMinions());
        return all;
    }
}
