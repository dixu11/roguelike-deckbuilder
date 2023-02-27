package dixu.deckard.client;

import dixu.deckard.server.event.*;

import java.awt.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class EndTurnButtonView implements Clickable, CoreEventHandler {

    private final BusManager bus = BusManager.instance();
    private final Rectangle rect;
    private LocalTime turnStarted = LocalTime.now();
    private boolean visible = true;

    public EndTurnButtonView() {
        rect = new Rectangle(GuiParams.getWidth(0.63), GuiParams.getHeight(0.9) + 15, GuiParams.getWidth(0.05), GuiParams.getHeight(0.03));
        bus.register(this, CoreEventName.TURN_STARTED);
    }

    public void render(Graphics g) {
        if (!visible) {
            return;
        }
        g.setColor(Color.GRAY);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
        g.setColor(Color.DARK_GRAY);
        g.drawString("End turn", rect.x + 10, rect.y + 15);
    }


    @Override
    public void onClick() {
        if (turnStarted.until(LocalTime.now(), ChronoUnit.SECONDS) < 1) {
            return;
        }
        visible = false;
        bus.post(CoreEvent.of(CoreEventName.TURN_ENDED));
    }

    @Override
    public boolean isClicked(int x, int y) {
        return rect.intersects(x, y, 1, 1);
    }

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
