package dixu.deckard.server;

import java.util.List;

public interface PlayerView {
    void setController(GameController gameController);

    void addAll(List<Card> cards);

}
