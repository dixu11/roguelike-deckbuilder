package dixu.deckard.client;

import java.awt.*;

public interface CounterView {
    void render(Graphics g, Rectangle rectangle);
    void render(Graphics g,int x,int y);

    void setDescription(String description);

    void setValue(int health);

    void setBlinking(boolean blinking);
}
