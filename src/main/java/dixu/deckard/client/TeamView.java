package dixu.deckard.client;

import dixu.deckard.server.event.*;
import dixu.deckard.server.Team;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TeamView implements ActionEventHandler {
    private static final int X_BASE_OFFSET = GuiParams.getWidth(0.3);
    private static final int Y_BASE_OFFSET = GuiParams.getHeight(0.3);
    private static final int X_COMPUTER_OFFSET = GuiParams.getWidth(0.25);
    private static final int PADDING = GuiParams.getWidth(0.08);
    private final BusManager bus = BusManager.instance();
    private List<MinionView> characters;
    private final Team team;
    private final Direction direction;
    private final CounterView blockCounter;

    public TeamView(Team team, Direction direction) {
        this.team = team;
        this.direction = direction;
        this.characters = team.getMinions().stream()
                .map(MinionView::new)
                .toList();
        characters = new ArrayList<>(this.characters);
        EventCounterView blockCounterEvent = new EventCounterView(Direction.TOP, Direction.LEFT,  Color.GRAY);
        blockCounterEvent.setDescription("\uD83D\uDEE1️: ");
        blockCounterEvent.setSource(team);
        blockCounterEvent.setStrategy((oldVal, e)-> e.value());
        bus.register(blockCounterEvent, ActionEventName.TEAM_BLOCK_CHANGED);
        blockCounter = blockCounterEvent;

        bus.register(this, ActionEventName.MINION_DIED);
    }

    public void render(Graphics g) {
        int xChange = 0;

        g.translate(getX(), getY());
        blockCounter.render(g, new Rectangle(0, CardView.CARD_HEIGHT * 2, 100, 100));
        for (MinionView character : characters) {
            int xMove = PADDING + CardView.CARD_WIDTH;
            character.render(g);
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

    @Override
    public void handle(ActionEvent event) {
        switch (event.getName()) {
            case MINION_DIED -> onMinionDied(event);
        }
    }

    private void onMinionDied(ActionEvent event) {
        if (event.getOwnTeam() == team) {
            characters.removeIf(v -> v.getCharacter() == event.getMinion());
        }
    }
}
