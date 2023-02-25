package dixu.deckard.client;

import dixu.deckard.server.Team;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TeamView {
    private static final int X_BASE_OFFSET = Display.getWidth(0.3);
    private static final int Y_BASE_OFFSET = Display.getHeight(0.3);
    private static final int X_COMPUTER_OFFSET = Display.getWidth(0.25);
    private static final int PADDING = Display.getWidth(0.08);
    private List<CharacterView> characters;
    private Team team;
    private Direction direction;
    private CounterView blockCounter;

    public TeamView(Team team, Direction direction) {
        this.team =team;
        this.direction = direction;
        this.characters = team.getCharacters().stream()
                .map(CharacterView::new)
                .toList();
        characters = new ArrayList<>(this.characters);
        blockCounter = new CounterView(Direction.TOP,Direction.LEFT, team::getBlock,Color.GRAY);
    }

    public void render(Graphics g) {
        characters.removeIf(view -> view.getCharacter().isDead());
        int xChange = 0;

        g.translate(getX(),getY());
        blockCounter.render(g,new Rectangle(0,CardView.CARD_HEIGHT*2,100,100));
        for (int i = 0; i < characters.size(); i++) {
            int xMove = PADDING + CardView.CARD_WIDTH;
            characters.get(i).render(g);
            g.translate(xMove, 0);
            xChange+=xMove;
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
