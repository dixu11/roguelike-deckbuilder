package dixu.deckard;

import dixu.deckard.client.*;
import dixu.deckard.server.*;

public class App {
    public static void main(String[] args) {
        //UNCOMMENT THIS IF YOU DON'T SEE GAME WINDOW:
//        Display.LOCATION_ON_SCREEN_X = 0;
//        Display.LOCATION_ON_SCREEN_Y = 0;


        //create frame class and initialize window size
        Display display = new Display("Deckard Thief");

        //create team
        TeamFactory teamFactory = new TeamFactory();
        Team player = teamFactory.createFirst();
        Team computer = teamFactory.createCreateSecond();

        //create views
        TeamView playerTeam = new TeamView(player,Direction.LEFT);
        TeamView computerTeam = new TeamView(computer, Direction.RIGHT);
        FightViewImpl fightViewImpl = new FightViewImpl(playerTeam,computerTeam);

        //create engine and connections
        GameEngine engine = new GameEngine(display, fightViewImpl);
        Game game = new Game(player,computer);
        GameController gameController = new GameController(game);
        fightViewImpl.setController(gameController);
        display.addListener(fightViewImpl);
        game.start();
        engine.start();
    }
}
