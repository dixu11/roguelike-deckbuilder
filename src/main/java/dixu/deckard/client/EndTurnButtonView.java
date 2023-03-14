package dixu.deckard.client;

import dixu.deckard.server.event.*;
import dixu.deckard.server.event.bus.Bus;

import java.awt.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class EndTurnButtonView implements EventHandler {
    private final Rectangle bounds;
    private final int TURN_ENDED_COOLDOWN_SECONDS = 1;
    private LocalTime turnStarted = LocalTime.now();
    private boolean visible = true;

    public EndTurnButtonView() {
        bounds = new Rectangle(GuiParams.getWidth(0.7), GuiParams.getHeight(0.9),
                GuiParams.getWidth(0.05), GuiParams.getHeight(0.03));

        Bus.register(this, CoreEventType.LEADER_PHASE_STARTED);
    }

    //render
    public void render(Graphics g) {
        if (!visible) {
            return;
        }
        g.setColor(GuiParams.MAIN_COLOR_BRIGHT);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g.setColor(GuiParams.MAIN_COLOR_DARK);
        g.drawString("End turn", (int) (bounds.x + bounds.width *0.1), (int) (bounds.y + bounds.height*0.73));
    }


    //interaction

    public boolean onClick(int x, int y) {
     if(!isClicked(x, y)){
         return false;
     }
        //block queued click - may be removed after implementing concurrency events
        if (turnStarted.until(LocalTime.now(), ChronoUnit.SECONDS) < TURN_ENDED_COOLDOWN_SECONDS) {
            return true;
        }
        visible = false;
        Bus.post(CoreEvent.of(CoreEventType.MINION_PHASE_STARTED));
        return true;
    }

    public boolean isClicked(int x, int y) {
        return bounds.intersects(x, y, 1, 1);
    }

    //animation button lock
    @Override
    public void handle(CoreEvent event) {
        switch (event.getType()) {
            case LEADER_PHASE_STARTED -> onTurnStart();
        }
    }

    private void onTurnStart() {
        turnStarted = LocalTime.now();
        visible = true;
    }
}
