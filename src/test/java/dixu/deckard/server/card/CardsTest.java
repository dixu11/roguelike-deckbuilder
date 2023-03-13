package dixu.deckard.server.card;

import dixu.deckard.server.FunctionalTest;
import dixu.deckard.server.event.ActionEvent;
import dixu.deckard.server.event.ActionEventType;
import dixu.deckard.server.event.bus.Bus;
import dixu.deckard.server.game.GameParams;
import dixu.deckard.server.minion.Minion;
import dixu.deckard.server.team.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class CardsTest extends FunctionalTest {

    @Test
    @DisplayName("Unstable attack looses power every play")
    public void test1() {
        GameParams.SECOND_TEAM_INITIAL_BLOCK = 0;
        GameParams.MINION_PER_TEAM = 1;
        int initialHp = 100;
        changeCardBaseValueTo(CardType.BASIC_MINION, initialHp);
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

        assertEquals(expectedDmgDealt, initialHp - firstMinion(secondTeam).getHealth());
    }

    @Test
    @DisplayName("Piercing attack ignore block")
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
    @DisplayName("Area attack deal dmg to all enemy minions and can be blocked")
    public void test3() {
        secondTeam.setBlock(1);
        clearAllCards();
        Minion attacker = firstMinion(firstTeam);
        composeMinionHand(attacker, CardType.AREA_ATTACK, CardType.AREA_ATTACK);
        Card areaAttack = minionHandFirstCard(attacker);

        executeTurn();

        List<Minion> minions = secondTeam.getMinions();
        for (Minion enemy : minions) {
            assertEquals(minionInitialHp() - areaAttack.getApproximateDamage(), enemy.getHealth());
        }
    }

    @Test
    @DisplayName("When stolen gift attack gives random card to the leader")
    public void test4() {
        clearAllCards();
        Minion minion = firstMinion(secondTeam);
        composeMinionHand(minion, CardType.GIFT_ATTACK);
        Card giftAttack = minionHandFirstCard(minion);

        Bus.post(ActionEvent.builder()
                .type(ActionEventType.LEADER_SPECIAL_STEAL)
                .leader(firstLeader)
                .minion(minion)
                .card(giftAttack)
                .build()
        );

        assertEquals(2, firstLeader.getHand().size());
        assertSame(giftAttack, firstLeader.getHand().get(0));
    }

    @Test
    @DisplayName("Combo Attack gets +1 bonus after every attack played this turn")
    public void test5() {
        disableBlockClear();
        clearAllCards();
        secondTeam.setBlock(10);
        composeMinionHand(firstMinion(firstTeam), CardType.BASIC_ATTACK, CardType.BASIC_BLOCK);
        composeMinionHand(secondMinion(firstTeam), CardType.COMBO_ATTACK, CardType.BASIC_ATTACK);

        executeTurn();

        assertEquals(7 - CardType.COMBO_ATTACK.getValue(), secondTeam.getBlock());
    }

    @Test
    @DisplayName("Solo Attack have bonus if it's only attack played this turn")
    public void test6() {
        changeCardBaseValueTo(CardType.BASIC_BLOCK, 0);
        clearAllCards();
        disableBlockClear();
        firstTeam.setBlock(10);
        secondTeam.setBlock(10);
        composeMinionHand(firstMinion(firstTeam), CardType.SOLO_ATTACK);
        composeMinionHand(secondMinion(firstTeam), CardType.BASIC_ATTACK);
        composeMinionHand(firstMinion(secondTeam), CardType.BASIC_BLOCK);
        composeMinionHand(secondMinion(secondTeam), CardType.SOLO_ATTACK);

        executeTurn();

        assertEquals(10 - CardType.SOLO_ATTACK.getValue(), firstTeam.getBlock());
        assertEquals(10 - (CardType.SOLO_ATTACK.getValue() - 2 + CardType.BASIC_ATTACK.getValue()), secondTeam.getBlock());
    }

    @Test
    @DisplayName("Block Collector has block equals to number of block card in minions deck")
    public void test7() {
        disableBasicAttack();
        reloadGame();
        clearAllCards();
        disableBlockClear();
        firstTeam.setBlock(0);
        secondTeam.setBlock(0);
        composeMinionHand(firstMinion(firstTeam), CardType.DECK_SHIELD);
        composeMinionHand(secondMinion(firstTeam), CardType.DECK_SHIELD, CardType.BASIC_BLOCK);
        composeMinionHand(firstMinion(secondTeam), CardType.DECK_SHIELD);
        composeMinionDiscard(firstMinion(secondTeam), CardType.BASIC_ATTACK, CardType.UPGRADED_BLOCK);
        composeMinionHand(secondMinion(secondTeam), CardType.DECK_SHIELD);
        composeMinionDraw(secondMinion(secondTeam), CardType.BASIC_BLOCK, CardType.BASIC_ATTACK, CardType.BASIC_BLOCK);
        executeTurn();

        assertEquals(2, firstTeam.getBlock());
        assertEquals(3, secondTeam.getBlock());
    }

    @Test
    @DisplayName("Block Booster card doubles current block of a team")
    public void test8() {
        clearAllCards(); //todo make this settings one of test scenarios - no cards, no block, disabled effects
        disableBlockClear();
        firstTeam.setBlock(0);
        secondTeam.setBlock(0);
        composeMinionHand(firstMinion(firstTeam), CardType.BASIC_BLOCK, CardType.BASIC_BLOCK);
        composeMinionHand(secondMinion(firstTeam), CardType.BLOCK_BOOSTER, CardType.BASIC_BLOCK);
        composeMinionHand(firstMinion(secondTeam), CardType.BLOCK_BOOSTER);
        composeMinionHand(secondMinion(secondTeam), CardType.UPGRADED_BLOCK, CardType.BLOCK_BOOSTER);

        executeTurn();

        assertEquals(5, firstTeam.getBlock());
        assertEquals(4, secondTeam.getBlock());
    }

    @Test
    @DisplayName("Life Lust gives minion hp equal to damage dealt")
    public void test9() {
        GameParams.MINION_PER_TEAM = 1;
        reloadGame();
        clearAllCards();
        disableBlockClear();
        firstTeam.setBlock(1);
        secondTeam.setBlock(0);
        Minion firstTeamMinion = firstMinion(firstTeam);
        Minion secondTeamMinion = firstMinion(secondTeam);
        composeMinionHand(firstTeamMinion, CardType.BASIC_ATTACK);
        composeMinionHand(secondTeamMinion, CardType.LIFE_LUST, CardType.LIFE_LUST, CardType.LIFE_LUST);

        executeTurn();

        assertEquals(1, firstTeamMinion.getHealth());
        assertEquals(3, secondTeamMinion.getHealth());
    }

    @Test
    @DisplayName("Heal card regenerate random minion for 1 hp")
    public void test10() {
        clearAllCards();
        disableBlockClear();
        firstTeam.setBlock(0);
        secondTeam.setBlock(0);
        Minion firstTeamMinion = firstMinion(firstTeam);
        Minion secondTeamMinion = firstMinion(secondTeam);
        composeMinionHand(firstTeamMinion, CardType.UPGRADED_ATTACK);
        composeMinionHand(secondTeamMinion, CardType.HEAL);

        executeTurn();

        int secondTeamHealthSum = countTeamHealth(secondTeam);
        assertEquals(5, secondTeamHealthSum);
    }

    private int countTeamHealth(Team team) {
        int health = 0;
        List<Minion> minions = team.getMinions();
        for (Minion minion : minions) {
            health += minion.getHealth();
        }
        return health;
    }
}
