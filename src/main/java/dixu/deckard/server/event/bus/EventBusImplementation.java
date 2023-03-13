package dixu.deckard.server.event.bus;

import dixu.deckard.client.GuiEvent;
import dixu.deckard.server.event.*;

import java.util.*;

public class EventBusImplementation {


    private final Map<EventType, List<EventHandler>> allHandlers = new HashMap<>();

    public void register(EventHandler handler, EventType type) {
        List<EventHandler> handlersByType = allHandlers.computeIfAbsent(type, handlers -> new ArrayList<>());
        handlersByType.add(0, handler);        //todo INDEX = 0: fast fix for problem registering handlers after Combat object.
                                                    // It makes them react to TurnStart before TurnEnd... Making it concurrency fix problem but introduces a loooot more bugs
    }

    private List<EventHandler> getHandlers(Event event) {
        return getHandlers(event.getType());
    }

    private List<EventHandler> getHandlers(EventType type) {
        List<EventHandler> eventHandlers = allHandlers.get(type);
        if (eventHandlers == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(eventHandlers);
    }

    public void unregister(EventHandler handler, EventType type) {
        getHandlers(type).remove(handler);
    }

    public void post(CoreEvent event) {
        getHandlers(event).forEach(handler -> handler.handle(event));
    }

    public void post(StateEvent event) {
        getHandlers(event).forEach(handler -> handler.handle(event));
    }

    public void post(ActionEvent event) {
        getHandlers(event).forEach(handler -> handler.handle(event));
    }

    public void post(GuiEvent event) {
        getHandlers(event).forEach(handler -> handler.handle(event));
    }

}
