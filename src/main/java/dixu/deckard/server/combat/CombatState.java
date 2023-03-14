package dixu.deckard.server.combat;

import dixu.deckard.App;
import dixu.deckard.server.event.EventHandler;
import dixu.deckard.server.event.CoreEvent;
import dixu.deckard.server.event.CoreEventType;
import dixu.deckard.server.event.bus.Bus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CombatState implements EventHandler {
    private static final Logger logger = LogManager.getLogger(App.class);

    private Phase phase = Phase.SETUP;
    private static CombatState instance;

    private CombatState() {
        Bus.register(this, CoreEventType.COMBAT_OVER);
    }

    public static CombatState getInstance() {
        if (instance == null) {
            instance = new CombatState();
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
        logger.warn("Phase set: " + phase.name());
    }
}
