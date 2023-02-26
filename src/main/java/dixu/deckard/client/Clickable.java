package dixu.deckard.client;

import dixu.deckard.server.GameController;

public interface Clickable {
    void onClick();

    boolean isClicked(int x, int y);
}
