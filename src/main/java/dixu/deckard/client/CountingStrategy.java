package dixu.deckard.client;

import dixu.deckard.server.event.ActionEvent;

@FunctionalInterface
public interface CountingStrategy {
    int updateValue(int oldValue, ActionEvent e);
}
