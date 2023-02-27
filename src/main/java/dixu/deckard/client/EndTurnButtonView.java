package dixu.deckard.client;

import dixu.deckard.server.event.*;

import java.awt.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class EndTurnButtonView implements Clickable, EventHandler<TurnStartedEvent> {

    private Rectangle rect;
    private LocalTime turnStarted = LocalTime.now();
    private boolean visible = true;

    public EndTurnButtonView() {
        rect = new Rectangle(Display.getWidth(0.63), Display.getHeight(0.9) + 15, Display.getWidth(0.05),
                Display.getHeight(0.03));
        EventBus.getInstance().register(this, TurnStartedEvent.class);
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
        EventBus.getInstance().post(new TurnEndedEvent());

    }

    @Override
    public boolean isClicked(int x, int y) {
        return rect.intersects(x, y, 1, 1);
    }

    @Override
    public void handle(TurnStartedEvent event) {
        turnStarted = LocalTime.now();
        visible = true;
    }
}
