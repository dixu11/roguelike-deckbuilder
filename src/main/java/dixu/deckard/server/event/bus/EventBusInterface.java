package dixu.deckard.server.event.Bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface EventBusInterface<Handler, EventType, GameEvent> {

    default void register(Handler handler, EventType type) {
        List<Handler> handlersByType = getHandlers().computeIfAbsent(type, handlers -> new ArrayList<>());
        handlersByType.add(handler);
    }

    void post(GameEvent event);

    HashMap<EventType, List<Handler>> getHandlers();
}
