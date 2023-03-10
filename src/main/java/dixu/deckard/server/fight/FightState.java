package dixu.deckard.server.fight;

import dixu.deckard.server.event.BusManager;
import dixu.deckard.server.event.CoreEvent;
import dixu.deckard.server.event.CoreEventHandler;
import dixu.deckard.server.event.CoreEventType;

public class FightState implements CoreEventHandler {
    private BusManager bus = BusManager.instance();
    private Phase phase = Phase.SETUP;
    private static FightState instance;

    private FightState() {
        bus.register(this, CoreEventType.FIGHT_OVER);
    }

    public static FightState getInstance() {
        if (instance == null) {
            instance = new FightState();
            System.out.println("FightState created");
        }
        return instance;
    }

    @Override
    public void handle(CoreEvent event) {
        onFightOver();
    }

    private void onFightOver() {
        //todo unregister for next fights
        instance = null;
    }

    void setPhase(Phase phase) {
        this.phase = phase;
        System.out.println("Phase set: " + phase.name());
    }
}
