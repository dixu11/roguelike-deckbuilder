package dixu.deckard.server.event;

import dixu.deckard.client.GuiEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiEventBus {
    GuiEventBus() {
    }

    private final Map<GuiEventName, List<GuiEventHandler>> allHandlers = new HashMap<>();

    public void register(GuiEventHandler handler, GuiEventName name) {
        List<GuiEventHandler> handlersByClass = allHandlers.computeIfAbsent(name, handlers -> new ArrayList<>());
        handlersByClass.add(handler);
    }

    public void post(GuiEvent event) {
        List<GuiEventHandler> eventHandlers = allHandlers.get(event.getName());
        if (eventHandlers != null) {
            for (GuiEventHandler handler : new ArrayList<>(eventHandlers)) {
                handler.handle(event);
            }
        }
    }
}
