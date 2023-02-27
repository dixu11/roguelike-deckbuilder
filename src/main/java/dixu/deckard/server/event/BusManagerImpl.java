package dixu.deckard.server.event;

public class BusManagerImpl implements BusManager {

    private final static BusManagerImpl manager = new BusManagerImpl();
    private final EventBus<CoreEventName, CoreEvent> structureBus = new EventBus<>();
    private final EventBus<FightEventName, FightEvent> effectBus = new EventBus<>();



    private BusManagerImpl() {
    }

    public static BusManagerImpl getInstance() {
        return manager;
    }

    @Override
    public void register(FightEventHandler handler, FightEventName name) {
        effectBus.register(handler,name);
    }

    @Override
    public void register(CoreEventHandler handler, CoreEventName name) {
        structureBus.register(handler, name);
    }

    @Override
    public void post(CoreEvent event) {
        structureBus.post(event);
    }

    @Override
    public void post(FightEvent event) {
        effectBus.post(event);
    }
}
