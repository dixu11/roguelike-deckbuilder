package dixu.deckard.server.event;

import dixu.deckard.client.GuiEvent;
import dixu.deckard.server.effect.LifeLustEffect;

public interface BusManager {
    void register(CoreEventHandler handler, CoreEventName name);

    void register(ActionEventHandler handler, ActionEventName name);
    void unregister(ActionEventHandler handler, ActionEventName name);

    void register(GuiEventHandler handler, GuiEventName name);

    void post(CoreEvent event);
    void post(ActionEvent event);

    void post(GuiEvent event);

    static BusManager instance() {
        return BusManagerImpl.getInstance();
    }

    static void reInitialize() {
        BusManagerImpl.getInstance().reset();
    }
}
