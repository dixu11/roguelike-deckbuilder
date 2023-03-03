package dixu.deckard.server.event;

import dixu.deckard.client.GuiEvent;

public class BusManagerImpl implements BusManager {

    private final static BusManagerImpl manager = new BusManagerImpl();
    private  EventBus<CoreEventName, CoreEvent> structureBus = new EventBus<>();
    private  EventBus<ActionEventName, ActionEvent> effectBus = new EventBus<>();
    private  EventBus<GuiEventName, GuiEvent> guiBus = new EventBus<>();


    private BusManagerImpl() {

    }

    public static BusManagerImpl getInstance() {
        return manager;
    }

    @Override
    public void register(ActionEventHandler handler, ActionEventName name) {
        effectBus.register(handler, name);
    }

    @Override
    public void register(CoreEventHandler handler, CoreEventName name) {
        structureBus.register(handler, name);
    }

    @Override
    public void register(GuiEventHandler handler, GuiEventName name) {
        guiBus.register(handler,name);
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
        structureBus = new EventBus<>();
        effectBus = new EventBus<>();
    }
}
