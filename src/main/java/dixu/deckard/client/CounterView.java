package dixu.deckard.client;

import java.awt.*;

public class CounterView {

    private static final double MARGIN_PERCENT = 0.05;
   private static final  Color DEFAULT_COLOR = Color.DARK_GRAY;
    private Direction direction1;
    private Direction direction2;
    private final CounterSource source;

    public CounterView(Direction direction1, Direction direction2, CounterSource source) {
        this.direction1 = direction1;
        this.direction2 = direction2;
        this.source = source;
    }

    public void render(Graphics g,Rectangle rect) {
        int marginX = (int) (rect.width * MARGIN_PERCENT);
        int marginY = (int) (rect.height * MARGIN_PERCENT);
        int x = rect.x;
        int y = rect.y;
        switch (direction1) {
            case TOP -> y += marginY;
            case BOTTOM -> y += rect.height - marginY*2;
        }
        switch (direction2) {
            case LEFT -> x += marginX;
            case RIGHT -> x += rect.width - marginX * 3;
        }
        g.setColor(DEFAULT_COLOR);
        g.drawString(source.getValue()+"",x,y);
    }
}
