package dixu.deckard.server.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoreEventBus {
    CoreEventBus() {
    }

    private final Map<CoreEventType, List<CoreEventHandler>> allHandlers = new HashMap<>();

    public void register(CoreEventHandler handler, CoreEventType type) {
        List<CoreEventHandler> handlersByClass = allHandlers.computeIfAbsent(type, handlers -> new ArrayList<>());
        handlersByClass.add(0,handler);
        //todo fast fix for problem registering handlers after Fight object. It makes them react to TurnStart before TurnEnd... Making it concurrency fix problem but introduces a loooot more bugs
    }

    public void post(CoreEvent event) {
        List<CoreEventHandler> eventHandlers = allHandlers.get(event.getType());
        if (eventHandlers != null) {
            for (CoreEventHandler handler :new ArrayList<>(eventHandlers)) {
                handler.handle(event);
            }
        }
    }
}
