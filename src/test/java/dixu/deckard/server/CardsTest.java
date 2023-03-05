package dixu.deckard.server;

import dixu.deckard.client.GuiParams;
import dixu.deckard.server.event.ActionEventName;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardsTest extends FunctionalTest {

    @Test
    @Description("Unstable attack looses power every play")
    public void test1() {
        GameParams.SECOND_TEAM_INITIAL_BLOCK = 0;
        GameParams.MINION_PER_TEAM = 1;
        int initialHp = 100;
        GameParams.MINION_INITIAL_HP = initialHp;
        reloadGame();
        int expectedDmgDealt = 3 + 2 + 1;

        clearAllCards();
        Minion minion = firstMinion(firstTeam);
        composeMinionHand(minion, CardType.UNSTABLE_ATTACK);

        executeTurn();
        executeTurn();
        executeTurn();
        executeTurn();
        executeTurn();

        assertEquals( initialHp-firstMinion(secondTeam).getHealth(), expectedDmgDealt);
    }


}
