package dixu.deckard.client;

import dixu.deckard.server.GameController;

public interface Clickable {
    void onClick(GameController gameController);

    boolean isClicked(int x, int y);
}
