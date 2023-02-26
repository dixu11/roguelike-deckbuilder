package dixu.deckard.server.event;

@FunctionalInterface
public interface EventHandler {
    void handle(Event event);
    //canHandle?
}
