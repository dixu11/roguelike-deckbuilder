package dixu.deckard.server.leader;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.event.ActionEventType;
import dixu.deckard.server.minion.Minion;

/**
* {@link Special} is ability that {@link Leader} can cast in their turn, spending energy {@link Special#cost}.
 * <p>
 * These {@link Special}s are currently available:
 * <p>
 *     - {@link ActionEventType#LEADER_SPECIAL_STEAL} let {@link Leader} steal cards from enemy
 *     {@link Minion}s and put them to his hand.
 *      <p>
 *       - {@link ActionEventType#LEADER_SPECIAL_GIVE} let {@link Leader} give stolen card
 *       to his {@link Minion} replacing chosen {@link Card} with new one - effectively upgrading his deck for the rest
 *       of the game.
 *       <p>
 *         - {@link ActionEventType#LEADER_SPECIAL_MOVE_HAND} let {@link Leader} made {@link Minion}
 *         draw new {@link Card} and discard one from their hand, changing {@link Minion}'s strategy for the turn if needed.
* */
public class Special {
    private int cost;

    public Special(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }
}
