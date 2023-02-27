package dixu.deckard.server.event;

@FunctionalInterface
public interface EventHandler<E extends Event> {
    void handle(E event);
}
