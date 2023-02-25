package dixu.deckard.client;

import java.awt.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class SourceCounterView implements CounterView{

    private static final double MARGIN_PERCENT = 0.2;
   private static final  Color DEFAULT_COLOR = Color.DARK_GRAY;
    private static final Font FONT = new Font(Font.SERIF, Font.PLAIN,  20);

    //location on rectangle
    private Direction direction1;
    private Direction direction2;

    //value
    private final CounterSource source;

    //looks
    private Color color;
    private String description = "";


    //update animation
    private int lastValue;
    private LocalTime changed = LocalTime.now();
    private boolean blinking = true;

    public SourceCounterView(Direction direction1, Direction direction2, CounterSource source) {
        this(direction1, direction2, source, DEFAULT_COLOR);
    }


    public SourceCounterView(Direction direction1, Direction direction2, CounterSource source, Color color) {
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
            case BOTTOM -> x+= marginX*2;
        }
//        g.setColor(Color.CYAN);
//        g.fillRect(rect.x,rect.y,rect.width,rect.height);
        Color color = this.color;
        if (changed.until(LocalTime.now(), ChronoUnit.SECONDS) < 1 && blinking) {
          color=  Color.YELLOW;
        }
        g.setColor(color);
        Font standard = g.getFont();
        g.setFont(FONT);
        g.drawString(description+ source.getValue(), x, y);
        g.setFont(standard);
    }

    public void setBlinking(boolean blinking) {
        this.blinking = blinking;
    }

    @Override
    public void addValue(int value) {
        //had to implement - to remove
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setValue(int newValue) {
        //had to implement - to remove
    }
}
