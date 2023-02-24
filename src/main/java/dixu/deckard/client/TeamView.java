package dixu.deckard.client;

import dixu.deckard.server.Character;

import java.awt.*;
import java.util.List;

public class TeamView {
    private static final int X_BASE_OFFSET = Display.getWidth(0.2);
    private static final int Y_BASE_OFFSET = Display.getHeight(0.3);
    private static final int X_COMPUTER_OFFSET = Display.getWidth(0.25);
    private static final int PADDING = Display.getWidth(0.08);
    private List<CharacterView> characters;
    private Direction direction;

    public TeamView(List<Character> characters, Direction direction) {
        this.characters = characters.stream()
                .map(CharacterView::new)
                .toList();
        this.direction = direction;
    }

    public void render(Graphics g) {
        int xChange = 0;
        g.translate(getX(),getY());
        for (int i = 0; i < characters.size(); i++) {
            int xMove = PADDING + CardView.CARD_WIDTH;
            g.translate(xMove, 0);
            xChange+=xMove;
            characters.get(i).render(g);
        }
        g.translate(-getX()- xChange,-getY());
    }

    private int getX() {
        return X_BASE_OFFSET + (direction == Direction.RIGHT ? X_COMPUTER_OFFSET : 0);
    }

    private int getY() {
        return Y_BASE_OFFSET;
    }
}
