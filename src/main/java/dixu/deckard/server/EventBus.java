package dixu.deckard.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {

    private static final EventBus instance = new EventBus();

    private EventBus() {
    }

    public static EventBus getInstance() {
        return instance;
    }

    private final Map<Class<?>, List<EventHandler>> allHandlers = new HashMap<>();

    public <T> void register(EventHandler handler, Class<T> eventType) {
        List<EventHandler> handlersByClass = allHandlers.computeIfAbsent(eventType, handlers -> new ArrayList<>());
        handlersByClass.add(handler);
    }

    public  void post(Event event){
        List<EventHandler> eventHandlers = allHandlers.get(event.getClass());
        if (eventHandlers != null) {
            for (EventHandler handler : eventHandlers) {
                handler.handle(event);
            }
        }
    }
}
