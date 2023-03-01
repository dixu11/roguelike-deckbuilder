package dixu.deckard;

import dixu.deckard.client.*;
import dixu.deckard.server.*;

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
