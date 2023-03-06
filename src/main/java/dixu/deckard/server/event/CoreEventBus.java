package dixu.deckard.server.event;

import dixu.deckard.client.GuiEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoreEventBus {
    CoreEventBus() {
    }

    private final Map<CoreEventName, List<CoreEventHandler>> allHandlers = new HashMap<>();

    public void register(CoreEventHandler handler, CoreEventName name) {
        List<CoreEventHandler> handlersByClass = allHandlers.computeIfAbsent(name, handlers -> new ArrayList<>());
        handlersByClass.add(handler);
    }

    public void post(CoreEvent event) {
        List<CoreEventHandler> eventHandlers = allHandlers.get(event.getName());
        if (eventHandlers != null) {
            for (CoreEventHandler handler : eventHandlers) {
                handler.handle(event);
            }
        }
    }
}
