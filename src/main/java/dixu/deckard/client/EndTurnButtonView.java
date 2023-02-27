package dixu.deckard.client;

import dixu.deckard.server.event.*;

import java.awt.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class EndTurnButtonView implements Clickable, CoreEventHandler {
    private final BusManager bus = BusManager.instance();
    private final Rectangle bounds;
    private final int TURN_ENDED_COOLDOWN_SECONDS = 1;
    private LocalTime turnStarted = LocalTime.now();
    private boolean visible = true;

    public EndTurnButtonView() {
        bounds = new Rectangle(GuiParams.getWidth(0.7), GuiParams.getHeight(0.9),
                GuiParams.getWidth(0.05), GuiParams.getHeight(0.03));

        bus.register(this, CoreEventName.TURN_STARTED);
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
    @Override
    public void onClick() {
        //block queued click - may be removed after implementing concurrency events
        if (turnStarted.until(LocalTime.now(), ChronoUnit.SECONDS) < TURN_ENDED_COOLDOWN_SECONDS) {
            return;
        }
        visible = false;
        bus.post(CoreEvent.of(CoreEventName.TURN_ENDED));
    }

    @Override
    public boolean isClicked(int x, int y) {
        return bounds.intersects(x, y, 1, 1);
    }

    //animation button lock
    @Override
    public void handle(CoreEvent event) {
        switch (event.getName()) {
            case TURN_STARTED -> onTurnStart();
        }
    }

    private void onTurnStart() {
        turnStarted = LocalTime.now();
        visible = true;
    }
}
