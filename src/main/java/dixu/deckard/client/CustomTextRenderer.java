package dixu.deckard.client;

import java.awt.*;

public class CustomTextRenderer {
    //todo too hardcoded
    private static final float FONT_SIZE = 11;
    private static final int CHARACTERS_PER_LINE = 14;
    private final Rectangle bounds;

    public CustomTextRenderer(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void render(Graphics2D g, String text, int layoutX, int layoutY) {
        Font font = g.getFont().deriveFont(FONT_SIZE);
        g.setFont(font);
        String textLeft = text;
        String line = "";
        int yMove = 0;
        while (!textLeft.isEmpty()) {
            String word = getNextWord(textLeft);

            textLeft = textLeft.replace(word, "");
            if (textLeft.contains(" ")) {
                textLeft = textLeft.substring(1);
            }

            if (line.length() + word.length() > CHARACTERS_PER_LINE) {
                g.drawString(line, bounds.x + layoutX, bounds.y + yMove + layoutY);
                line = word + " ";
                yMove += 15;
            } else {
                line += word + " ";
            }
        }
        g.drawString(line, bounds.x+ layoutX, bounds.y + layoutY + yMove);
    }

    private String getNextWord(String text) {
        int spaceIndex = text.indexOf(" ");
        if (spaceIndex == -1) {
            spaceIndex = text.length();
        }
        String nextWord = text.substring(0, spaceIndex);
        return nextWord;
    }
}
