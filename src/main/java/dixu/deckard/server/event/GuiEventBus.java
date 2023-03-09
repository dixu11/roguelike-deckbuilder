package dixu.deckard.server.event;

import dixu.deckard.client.GuiEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiEventBus {
    GuiEventBus() {
    }

    private final Map<GuiEventType, List<GuiEventHandler>> allHandlers = new HashMap<>();

    public void register(GuiEventHandler handler, GuiEventType type) {
        List<GuiEventHandler> handlersByClass = allHandlers.computeIfAbsent(type, handlers -> new ArrayList<>());
        handlersByClass.add(handler);
    }

    public void post(GuiEvent event) {
        List<GuiEventHandler> eventHandlers = allHandlers.get(event.getType());
        if (eventHandlers != null) {
            for (GuiEventHandler handler : new ArrayList<>(eventHandlers)) {
                handler.handle(event);
            }
        }
    }
}
