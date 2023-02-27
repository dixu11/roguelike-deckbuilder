package dixu.deckard.server.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus<T,E extends Event<T>> {

    EventBus() {
    }

    private final Map<T, List<EventHandler<E>>> allHandlers = new HashMap<>();

    public void register(EventHandler<E> handler, T name) {
        List<EventHandler<E>> handlersByClass = allHandlers.computeIfAbsent(name, handlers -> new ArrayList<>());
        handlersByClass.add(handler);
    }

    public  void post(E event){
        List<EventHandler<E>> eventHandlers = allHandlers.get(event.getName());
        if (eventHandlers != null) {
            for (EventHandler<E> handler : eventHandlers) {
                handler.handle(event);
            }
        }
    }
}
