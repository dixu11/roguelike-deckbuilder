package dixu.deckard.server.combat;

import dixu.deckard.server.event.EventHandler;
import dixu.deckard.server.event.CoreEvent;
import dixu.deckard.server.event.CoreEventType;
import dixu.deckard.server.event.bus.Bus;


public class CombatState implements EventHandler {
    private Phase phase = Phase.SETUP;
    private static CombatState instance;

    private CombatState() {
        Bus.register(this, CoreEventType.FIGHT_OVER);
    }

    public static CombatState getInstance() {
        if (instance == null) {
            instance = new CombatState();
            System.out.println("CombatState created");
        }
        return instance;
    }

    @Override
    public void handle(CoreEvent event) {
        onCombatOver();
    }

    private void onCombatOver() {
        //todo unregister for next combats
        instance = null;
    }

    void setPhase(Phase phase) {
        this.phase = phase;
        System.out.println("Phase set: " + phase.name());
    }
}
