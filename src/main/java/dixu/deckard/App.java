package dixu.deckard;

import dixu.deckard.client.*;
import dixu.deckard.server.card.Card;
import dixu.deckard.server.leader.Special;
import dixu.deckard.server.minion.Minion;
import dixu.deckard.server.fight.Fight;
import dixu.deckard.server.leader.Leader;
import dixu.deckard.server.leader.LeaderFactory;
import dixu.deckard.server.leader.LeaderType;

/**
 * If you want to learn about the rules of the game and central elements of the system start from {@link Fight} class :)
 * <p>
 *     You can also check diagrams in resources package.
 * <p>
 * {@link dixu.deckard.server.event.Event} notifications are central architecture element of this project and working with
 * {@link dixu.deckard.server.event.Event}s is executed by {@link dixu.deckard.server.event.BusManager}.
 * <p>
 *     ANY suggestions how to improve this code, especially concerning architecture, are strongly recommended. I'm open for
 *     all sorts of feedback. If you like, you can contact me on GitHub, e-mail or facebook.
 *     <p>
 * I'm planning to make roguelike deckbuilder with special steal mechanics and little mix of auto-battle genre.
 * <p>
 * For now im implementing core game features like {@link Card}s, different {@link Minion}s or {@link Leader}'s
 * {@link Special} abilities. There is no progression system jet and game lasts for
 * only one {@link Fight}.
 * <p>
 * {@link App} create all objects from client and from server and starts an app.
 * <p>
 *     @author <a href="mailto:daniel.szlicht25@gmail.com">Daniel 'Dixu' Szlicht</a>
* */
public class App {
    public static void main(String[] args) {
        //UNCOMMENT THIS IF YOU DON'T SEE GAME WINDOW:
//        GuiParams.LOCATION_ON_SCREEN_X = 0;
//        GuiParams.LOCATION_ON_SCREEN_Y = 0;

        //initialize fight(server)
        LeaderFactory factory = new LeaderFactory();
        Leader firstLeader = factory.create(LeaderType.PLAYER);
        Leader secondLeader = factory.create(LeaderType.SIMPLE_BOT);
        Fight fight = new Fight(firstLeader,secondLeader);

        //initialize client
        Display display = new Display("Deckard Thief");
        FightViewImpl fightViewImpl = new FightViewImpl(firstLeader,secondLeader);
        GameEngine engine = new GameEngine(display, fightViewImpl);
        GuiController guiController = new GuiController(fightViewImpl);
        display.addListener(fightViewImpl);
        engine.start();

        //start game
        fight.start();
    }
}
