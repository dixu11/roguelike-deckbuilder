package dixu.deckard;

import dixu.deckard.client.Display;
import dixu.deckard.client.GameEngine;
import dixu.deckard.client.MainView;

public class App {
    public static void main(String[] args) {
        Display display = new Display("Deckard Thief");
        MainView mainView = new MainView();
        GameEngine engine = new GameEngine(display,mainView);
        engine.start();
    }
}
