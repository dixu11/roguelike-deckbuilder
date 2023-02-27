package dixu.deckard.server.event;

public interface BusManager {
    void register(CoreEventHandler handler, CoreEventName name);
    void register(FightEventHandler handler, FightEventName name);

    void post(CoreEvent event);
    void post(FightEvent event);

    static BusManager instance(){
        return BusManagerImpl.getInstance();
    }
}
