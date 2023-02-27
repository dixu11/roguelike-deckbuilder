package dixu.deckard.client;

import dixu.deckard.server.event.Event;
import dixu.deckard.server.event.EventHandler;
import dixu.deckard.server.event.MinionDamagedEvent;
import dixu.deckard.server.event.TeamBlockChangedEvent;

import java.awt.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class EventCounterView implements CounterView, EventHandler {

    private static final double MARGIN_PERCENT = 0.2;
    private static final Color DEFAULT_COLOR = Color.DARK_GRAY;
    private static final Font FONT = new Font(Font.SERIF, Font.PLAIN, 20);

    //location on rectangle
    private Direction direction1;
    private Direction direction2;

    //value
    private int value;

    //looks
    private Color color;
    private String description = "";
    private Object parent;


    //update animation
    private LocalTime changed = LocalTime.now();
    private boolean blinking = true;

    public EventCounterView(Direction direction1, Direction direction2) {
        this(direction1, direction2, DEFAULT_COLOR);
    }


    public EventCounterView(Direction direction1, Direction direction2, Color color) {
        this.direction1 = direction1;
        this.direction2 = direction2;
        this.color = color;
    }


    public void render(Graphics g, Rectangle rect) {
        int marginX = (int) (rect.width * MARGIN_PERCENT);
        int marginY = (int) (rect.height * MARGIN_PERCENT);
        int x = rect.x;
        int y = rect.y;
        switch (direction1) {
            case TOP -> y += marginY;
            case BOTTOM -> y += rect.height - marginY;
        }
        switch (direction2) {
            case LEFT -> x += marginX;
            case RIGHT -> x += rect.width - marginX;
            case BOTTOM -> x += marginX * 2;
        }
//        g.setColor(Color.CYAN);
//        g.fillRect(rect.x,rect.y,rect.width,rect.height);
        Color color = this.color;
        if (changed.until(LocalTime.now(), ChronoUnit.SECONDS) < 1 && blinking) {
            color = Color.YELLOW;
        }
        g.setColor(color);
        Font standard = g.getFont();
        g.setFont(FONT);
        g.drawString(description + value, x, y);
        g.setFont(standard);
    }

    public void setBlinking(boolean blinking) {
        this.blinking = blinking;
    }

    @Override
    public void addValue(int value) {
        this.value += value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }


    //todo CAN SOMEBODY TELL ME HOW TO IMPLEMENT THIS WITHOUT NEED OF INSTANCEOF?
    @Override
    public void handle(Event event) {
        if (event instanceof TeamBlockChangedEvent teamBlockChangedEvent) {
            onTeamBlockChanged(teamBlockChangedEvent);
        }
        if (event instanceof MinionDamagedEvent minionDamagedEvent) {
            onMinionDamaged(minionDamagedEvent);
        }
    }

    private void onTeamBlockChanged(TeamBlockChangedEvent teamBlockChangedEvent) {
        if (teamBlockChangedEvent.getTeam() == parent) {
            value = teamBlockChangedEvent.getNewValue();
            changed = LocalTime.now();
        }
    }

    private void onMinionDamaged(MinionDamagedEvent minionDamagedEvent) {
        if (minionDamagedEvent.getMinion() == parent) {
            value = minionDamagedEvent.getNewValue();
            changed = LocalTime.now();
        }
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }
}
