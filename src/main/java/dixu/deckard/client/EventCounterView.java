package dixu.deckard.client;

import dixu.deckard.server.event.ActionEvent;
import dixu.deckard.server.event.EventHandler;
import lombok.Builder;

import java.awt.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
@Builder
public class EventCounterView implements CounterView, EventHandler {

    private static final double MARGIN_PERCENT = 0.2;
    private static final Color DEFAULT_COLOR = Color.DARK_GRAY;
    private static final Font FONT = new Font(Font.SERIF, Font.PLAIN, 20);

    //location on rectangle
    @Builder.Default
    private Direction diagonalShift = Direction.LEFT;
    @Builder.Default
    private Direction straightDirection = Direction.TOP;

    //value
    private int value;
    private Object source;
    private CountingStrategy strategy;

    //looks
    private Color color;
    @Builder.Default
    private String description = "";


    //update animation
    @Builder.Default
    private LocalTime changed = LocalTime.now();
    @Builder.Default
    private boolean blinking = true;

    //dont have patience to refactor this further ;)
    public void render(Graphics g, Rectangle rect) {
        int marginX = (int) (rect.width * MARGIN_PERCENT);
        int marginY = (int) (rect.height * MARGIN_PERCENT);

        int x = rect.x;
        int y = rect.y;

        switch (straightDirection) {
            case TOP -> y += marginY;
            case BOTTOM -> y += rect.height - marginY;
        }
        switch (diagonalShift) {
            case LEFT -> x += marginX;
            case RIGHT -> x += rect.width - marginX;
            case BOTTOM -> x += marginX * 2;
        }

        Color color = this.color;
        if (changed.until(LocalTime.now(), ChronoUnit.SECONDS) < 1 && blinking) {
            color = Color.YELLOW;
        }

        g.setColor(color);
        Font standard = g.getFont();
        g.setFont(FONT);
        g.drawString(description + value, x, y);
        g.setFont(standard);

        //for debug
//        g.setColor(Color.CYAN);
//        g.fillRect(rect.x,rect.y,rect.width,rect.height);
    }

    public void render(Graphics g,int x,int y) {
        render(g,new Rectangle(x,y,1,1));
    }

    @Override
    public void handle(ActionEvent event) {
        if (strategy == null) {
            throw new IllegalStateException("STRATEGY MUST BE DEFINED ON EVERY COUNTER");
        }
        if (event.getSource() != source) return;

        int oldValue = value;
        value = strategy.updateValue(value, event);
        if (oldValue!= value) {
            changed = LocalTime.now();
        }
    }

    public void setBlinking(boolean blinking) {
        this.blinking = blinking;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    public void setStrategy(CountingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
