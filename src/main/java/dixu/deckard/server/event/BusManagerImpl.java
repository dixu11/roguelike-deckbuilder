package dixu.deckard.server.event;

import dixu.deckard.client.GuiEvent;

public class BusManagerImpl implements BusManager {

    private final static BusManagerImpl manager = new BusManagerImpl();
    private CoreEventBus structureBus = new CoreEventBus();
    private ActionEventBus effectBus = new ActionEventBus();
    private GuiEventBus guiBus = new GuiEventBus();


    private BusManagerImpl() {

    }

    public static BusManagerImpl getInstance() {
        return manager;
    }

    @Override
    public void register(ActionEventHandler handler, ActionEventType type) {
        effectBus.register(handler, type);
    }

    @Override
    public void unregister(ActionEventHandler handler, ActionEventType type) {
        effectBus.unregister(handler, type);
    }

    @Override
    public void register(CoreEventHandler handler, CoreEventType type) {
        structureBus.register(handler, type);
    }

    @Override
    public void register(GuiEventHandler handler, GuiEventType type) {
        guiBus.register(handler,type);
    }

    @Override
    public void post(CoreEvent event) {
        structureBus.post(event);
    }

    @Override
    public void post(ActionEvent event) {
        effectBus.post(event);
    }

    @Override
    public void post(GuiEvent event) {
        guiBus.post(event);
    }

    public void reset() {
        structureBus = new CoreEventBus();
        effectBus = new ActionEventBus();
        guiBus = new GuiEventBus();
    }
}
