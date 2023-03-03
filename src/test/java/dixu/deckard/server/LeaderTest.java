package dixu.deckard.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LeaderTest extends FunctionalTest{

    @Test
    @DisplayName("Player leader starts with 0 cards, computer with 0")
    public void test1() {
        Assertions.assertEquals(0,firstLeader.getHand().size());
        Assertions.assertEquals(0,secondLeader.getHand().size());
    }

}
