package dixu.deckard;

import dixu.deckard.client.*;
import dixu.deckard.server.*;

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
 * For now im trying to stabilize central {@link Fight} mechanics before adding content, like more {@link Card}s,
 * different {@link Minion}s or more {@link Special} abilities. There is no progression system jet and game lasts for
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


        //create frame class and initialize window size
        Display display = new Display("Deckard Thief");

        //create team
        LeaderFactory factory = new LeaderFactory();
        Leader firstLeader = factory.create();
        Leader secondLeader = factory.create();
        Team firstTeam =  firstLeader.getTeam();
        Team secondTeam = secondLeader.getTeam();

        //create views
        TeamView firstTeamView = new TeamView(firstTeam,Direction.LEFT);
        TeamView secondTeamView = new TeamView(secondTeam, Direction.RIGHT);
        FightViewImpl fightViewImpl = new FightViewImpl(firstTeamView,secondTeamView);

        //create engine and connections
        GameEngine engine = new GameEngine(display, fightViewImpl);
        Fight fight = new Fight(firstLeader,secondLeader);
        FightController fightController = new FightController(fight);
        fightViewImpl.setController(fightController);
        display.addListener(fightViewImpl);
        fight.start();
        engine.start();
    }
}
