package dixu.deckard.client;

import java.awt.*;

public class GuiParams {
    public static final Color MAIN_COLOR_BRIGHT = Color.GRAY;
    public static final Color MAIN_COLOR_DARK = Color.DARK_GRAY;
    public static final Color HIGHLIGHT_COLOR = Color.YELLOW;
    public static int LOCATION_ON_SCREEN_X = 2200;
    public static int LOCATION_ON_SCREEN_Y = 50;
    private static final int DEFAULT_WIDTH = 2100;
    private static final int DEFAULT_HEIGHT = 800;
    public static final int WIDTH = DEFAULT_WIDTH;
    public static final int HEIGHT = DEFAULT_HEIGHT;
    //card
    public static final int CARD_WIDTH = 80;
    public static final int CARD_HEIGHT = 120;
    public static final int CARD_PADDING = 20;

    public static int getWidth(double percent) {
        return (int) (WIDTH * percent);
    }

    public static int getHeight(double percent) {
        return (int) (HEIGHT * percent);
    }
}
