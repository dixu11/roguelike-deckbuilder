package dixu.deckard.client;

import dixu.deckard.server.event.*;
import dixu.deckard.server.Team;
import dixu.deckard.server.event.Event;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TeamView implements EventHandler {
    private static final int X_BASE_OFFSET = GuiParams.getWidth(0.3);
    private static final int Y_BASE_OFFSET = GuiParams.getHeight(0.3);
    private static final int X_COMPUTER_OFFSET = GuiParams.getWidth(0.25);
    private static final int PADDING = GuiParams.getWidth(0.08);
    private List<MinionView> characters;
    private Team team;
    private Direction direction;
    private CounterView blockCounter;

    public TeamView(Team team, Direction direction) {
        this.team = team;
        this.direction = direction;
        this.characters = team.getMinions().stream()
                .map(MinionView::new)
                .toList();
        characters = new ArrayList<>(this.characters);
        EventCounterView blockCounterEvent = new EventCounterView(Direction.TOP, Direction.LEFT,  Color.GRAY);
        blockCounterEvent.setDescription("\uD83D\uDEE1️: ");
        blockCounterEvent.setParent(team);
        EventBus.getInstance().register(blockCounterEvent, TeamBlockChangedEvent.class);
        blockCounter = blockCounterEvent;

        EventBus.getInstance().register(this, MinionDiedEvent.class);
    }

    public void render(Graphics g) {
        int xChange = 0;

        g.translate(getX(), getY());
        blockCounter.render(g, new Rectangle(0, CardView.CARD_HEIGHT * 2, 100, 100));
        for (int i = 0; i < characters.size(); i++) {
            int xMove = PADDING + CardView.CARD_WIDTH;
            characters.get(i).render(g);
            g.translate(xMove, 0);
            xChange += xMove;
        }
        g.translate(-getX() - xChange, -getY());
    }

    private int getX() {
        return X_BASE_OFFSET + (direction == Direction.RIGHT ? X_COMPUTER_OFFSET : 0);
    }

    private int getY() {
        return Y_BASE_OFFSET;
    }

    //todo CAN SOMEBODY TELL ME HOW TO IMPLEMENT THIS WITHOUT NEED OF INSTANCEOF?
    @Override
    public void handle(Event event) {
        if (event instanceof MinionDiedEvent minionDiedEvent) {
            onMinionDied(minionDiedEvent);
        }
    }

    private void onMinionDied(MinionDiedEvent event) {
        if (event.getTeam() == team) {
            characters.removeIf(v -> v.getCharacter() == event.getMinion());
        }
    }
}
