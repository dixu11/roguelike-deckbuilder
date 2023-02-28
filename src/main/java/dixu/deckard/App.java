package dixu.deckard;

import dixu.deckard.client.*;
import dixu.deckard.server.*;

public class App {
    public static void main(String[] args) {
        //UNCOMMENT THIS IF YOU DON'T SEE GAME WINDOW:
        GuiParams.LOCATION_ON_SCREEN_X = 0;
        GuiParams.LOCATION_ON_SCREEN_Y = 0;


        //create frame class and initialize window size
        Display display = new Display("Deckard Thief");

        //create team
        TeamFactory teamFactory = new TeamFactory();
        Team firstTeam = teamFactory.createFirst();
        Team secondTeam = teamFactory.createSecond();

        //create views
        TeamView firstTeamView = new TeamView(firstTeam,Direction.LEFT);
        TeamView secondTeamView = new TeamView(secondTeam, Direction.RIGHT);
        FightViewImpl fightViewImpl = new FightViewImpl(firstTeamView,secondTeamView);

        //create engine and connections
        GameEngine engine = new GameEngine(display, fightViewImpl);
        Game game = new Game(firstTeam,secondTeam);
        GameController gameController = new GameController(game);
        fightViewImpl.setController(gameController);
        display.addListener(fightViewImpl);
        game.start();
        engine.start();
    }
}
