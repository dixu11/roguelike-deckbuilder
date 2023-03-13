package dixu.deckard.server.event;

import dixu.deckard.client.GuiEvent;

public interface EventHandler {
    default void handle(ActionEvent event) {

    }

    default void handle(CoreEvent event) {

    }

    default void handle(StateEvent event) {

    }

    default void handle(GuiEvent event) {

    }
}
