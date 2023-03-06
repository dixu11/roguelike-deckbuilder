package dixu.deckard.server.event;

import dixu.deckard.client.GuiEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//todo how we can make this work??
/*public class EventBus<N,H,E> {
    EventBus() {
    }

    private final Map<N, List<H>> allHandlers = new HashMap<>();

    public void register(H handler, N name) {
        List<H> handlersByClass = allHandlers.computeIfAbsent(name, handlers -> new ArrayList<>());
        handlersByClass.add(handler);
    }

    public void post(E event) {
        List<H> eventHandlers = allHandlers.get(event.getName());
        if (eventHandlers != null) {
            for (H handler : eventHandlers) {
                handler.handle(event);
            }
        }
    }
}*/
