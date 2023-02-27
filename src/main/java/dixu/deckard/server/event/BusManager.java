package dixu.deckard.server.event;

public interface BusManager {
    void register(CoreEventHandler handler, CoreEventName name);
    void register(ActionEventHandler handler, ActionEventName name);

    void post(CoreEvent event);
    void post(ActionEvent event);

    static BusManager instance(){
        return BusManagerImpl.getInstance();
    }
}
