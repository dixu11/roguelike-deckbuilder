package dixu.deckard.client;

import dixu.deckard.server.GameController;
import dixu.deckard.server.event.*;
import dixu.deckard.server.event.Event;

import java.awt.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class EndTurnButtonView implements Clickable, EventHandler {

    private Rectangle rect;
    private LocalTime turnStarted = LocalTime.now();
    private boolean visible = true;

    public EndTurnButtonView() {
        rect = new Rectangle(Display.getWidth(0.63), Display.getHeight(0.9) + 15, Display.getWidth(0.05), Display.getHeight(0.03));
        EventBus.getInstance().register(this, StartTurnEvent.class);
    }

    public void render(Graphics g) {
        if (!visible) {
            return;
        }
        g.setColor(Color.GRAY);
        g.fillRect(rect.x, rect.y , rect.width, rect.height);
        g.setColor(Color.DARK_GRAY);
        g.drawString("End turn",rect.x+10,rect.y +15);
    }


    @Override
    public  void onClick() {
        if (turnStarted.until(LocalTime.now(), ChronoUnit.SECONDS) < 1) {
            return;
        }
        visible = false;
        EventBus.getInstance().post(new EndTurnEvent());

    }

    @Override
    public boolean isClicked(int x, int y) {
        return rect.intersects(x, y, 1, 1);
    }

    @Override
    public void handle(Event event) {
        if (event instanceof StartTurnEvent) {
            turnStarted = LocalTime.now();
            visible = true;
        }
    }
}
