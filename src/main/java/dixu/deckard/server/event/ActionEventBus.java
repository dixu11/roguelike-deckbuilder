package dixu.deckard.server.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionEventBus {

   ActionEventBus() {
    }

    private final Map<ActionEventType, List<ActionEventHandler>> allHandlers = new HashMap<>();

    public void register(ActionEventHandler handler, ActionEventType type) {
        List<ActionEventHandler> handlersByClass = allHandlers.computeIfAbsent(type, handlers -> new ArrayList<>());
        handlersByClass.add(handler);
    }

    public void post(ActionEvent event) {
        List<ActionEventHandler> eventHandlers = allHandlers.get(event.getType());
        if (eventHandlers != null) {
            for (ActionEventHandler handler : new ArrayList<>(eventHandlers)) {
                handler.handle(event);
            }
        }
    }

    public void unregister(ActionEventHandler handler, ActionEventType name) {
        allHandlers.get(name).remove(handler);
    }
}
