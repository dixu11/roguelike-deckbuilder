package dixu.deckard.server.event;

@FunctionalInterface
public interface EventHandler<T extends Event> {
    void handle(T event);

    //canHandle?
}
