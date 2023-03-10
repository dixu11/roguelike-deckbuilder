package dixu.deckard.server.event;

import dixu.deckard.client.GuiEvent;
import dixu.deckard.server.fight.FightState;

public interface BusManager {
    void register(CoreEventHandler handler, CoreEventType type);
    void register(ActionEventHandler handler, ActionEventType type);
    void unregister(ActionEventHandler handler, ActionEventType type);
    void register(GuiEventHandler handler, GuiEventType type);
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
