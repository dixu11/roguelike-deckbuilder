package dixu.deckard.server;

@FunctionalInterface
public interface EventHandler {
    void handle(Event event);
    //canHandle?
}
