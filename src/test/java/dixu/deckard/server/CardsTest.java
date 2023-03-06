package dixu.deckard.server;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardsTest extends FunctionalTest {

    @Test
    @Description("Unstable attack looses power every play")
    public void test1() {
        GameParams.SECOND_TEAM_INITIAL_BLOCK = 0;
        GameParams.MINION_PER_TEAM = 1;
        int initialHp = 100;
        changeCardBaseValueTo(CardType.BASIC_MINION,initialHp);
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

        assertEquals( expectedDmgDealt, initialHp - firstMinion(secondTeam).getHealth());
    }

    @Test
    @Description("Piercing attack ignore block")
    public void test2() {
        GameParams.MINION_PER_TEAM = 1;
        reloadGame();
        clearAllCards();
        Minion minion = firstMinion(firstTeam);
        composeMinionHand(minion, CardType.PIERCING_ATTACK);
        Card piercingAttack = minionHandFirstCard(minion);

        executeTurn();

        assertEquals(minionInitialHp() - firstMinion(secondTeam).getHealth(), piercingAttack.getApproximateDamage());
    }

    @Test
    @Description("Area attack deal dmg to all enemy minions and can be blocked")
    public void test3() {
        secondTeam.setBlock(1);
        clearAllCards();
        Minion attacker = firstMinion(firstTeam);
        composeMinionHand(attacker,CardType.AREA_ATTACK,CardType.AREA_ATTACK );
        Card areaAttack = minionHandFirstCard(attacker);

        executeTurn();

        List<Minion> minions = secondTeam.getMinions();
        for (Minion enemy : minions) {
            assertEquals(minionInitialHp() - areaAttack.getApproximateDamage(), enemy.getHealth());
        }
    }
}
