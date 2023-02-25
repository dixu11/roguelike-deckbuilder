package dixu.deckard.client;

import java.awt.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class CounterView {

    private static final double MARGIN_PERCENT = 0.2;
   private static final  Color DEFAULT_COLOR = Color.DARK_GRAY;
    private Direction direction1;
    private Direction direction2;
    private final CounterSource source;
    private Color color;
    private int lastValue;
    private LocalTime changed = LocalTime.now();
    private Font font= new Font(Font.SERIF, Font.PLAIN,  20);
    private boolean blinking = true;

    public CounterView(Direction direction1, Direction direction2, CounterSource source) {
        this(direction1, direction2, source, DEFAULT_COLOR);
    }


    public CounterView(Direction direction1, Direction direction2, CounterSource source, Color color) {
        this.direction1 = direction1;
        this.direction2 = direction2;
        this.source = source;
        this.color = color;
    }


    public void render(Graphics g,Rectangle rect) {
        if (lastValue != source.getValue()) {
            changed = LocalTime.now();
            lastValue = source.getValue();
        }

        int marginX = (int) (rect.width * MARGIN_PERCENT);
        int marginY = (int) (rect.height * MARGIN_PERCENT);
        int x = rect.x;
        int y = rect.y;
        switch (direction1) {
            case TOP -> y += marginY ;
            case BOTTOM -> y += rect.height - marginY;
        }
        switch (direction2) {
            case LEFT -> x += marginX;
            case RIGHT -> x += rect.width - marginX ;
        }
//        g.setColor(Color.CYAN);
//        g.fillRect(rect.x,rect.y,rect.width,rect.height);
        Color color = this.color;
        if (changed.until(LocalTime.now(), ChronoUnit.SECONDS) < 1 && blinking) {
          color=  Color.YELLOW;
        }
        g.setColor(color);
        Font standard = g.getFont();
        g.setFont(font);
        g.drawString(source.getValue() + "", x, y);
        g.setFont(standard);
    }

    public void setBlinking(boolean blinking) {
        this.blinking = blinking;
    }


}
