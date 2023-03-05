package dixu.deckard.client;

import dixu.deckard.server.Card;
import dixu.deckard.server.event.*;
import dixu.deckard.server.Team;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dixu.deckard.client.GuiParams.CARD_HEIGHT;
import static dixu.deckard.client.GuiParams.CARD_WIDTH;

public class TeamView implements ActionEventHandler {
    //layout
    private static final int X_FIRST_TEAM_POSITION = GuiParams.getWidth(0.27);
    private static final int Y_FIRST_TEAM_POSITION = GuiParams.getHeight(0.33);
    private static final int TEAMS_DISTANCE = GuiParams.getWidth(0.3);
    private static final int PADDING = GuiParams.getWidth(0.08);

    private final BusManager bus = BusManager.instance();
    private List<MinionView> minions;
    private final Team team;
    private final Direction direction;
    private  CounterView blockCounter;

    public TeamView(Team team, Direction direction) {
        this.team = team;
        this.direction = direction;

        setupMinions(team);
        setupBlockCounters(team);

        bus.register(this, ActionEventName.MINION_DIED);
    }

    private void setupMinions(Team team) {
        this.minions = team.getMinions().stream()
                .map(minion ->new MinionView(minion,this))
                .toList();
        minions = new ArrayList<>(this.minions);
    }

    private void setupBlockCounters(Team team) {
        EventCounterView blockCounterEvent = EventCounterView.builder()
                .color(GuiParams.MAIN_COLOR_BRIGHT)
                .description("\uD83D\uDEE1️: ")
                .source(team)
                .strategy((oldVal, e) -> e.getValue())
                .build();

        bus.register(blockCounterEvent, ActionEventName.TEAM_BLOCK_CHANGED);
        blockCounter = blockCounterEvent;
    }

    public void render(Graphics2D g) {
        int xChange = 0;
        g.translate(getX(), getY());
        blockCounter.render(g, new Rectangle((int) (PADDING*0.75), CARD_HEIGHT * 2, CARD_WIDTH, CARD_HEIGHT));

        for (MinionView minion : new ArrayList<>(minions)) {
            int xMove = PADDING + CARD_WIDTH;
            minion.render(g);
            g.translate(xMove, 0);
            xChange += xMove;
        }

        g.translate(-getX() - xChange, -getY());
    }

    private int getX() {
        return X_FIRST_TEAM_POSITION + (direction == Direction.RIGHT ? TEAMS_DISTANCE : 0);
    }

    private int getY() {
        return Y_FIRST_TEAM_POSITION;
    }

    public void reactToClickOnScreen(int x, int y){
        for (MinionView minion : minions) {
            Optional<CardView> optionalSelected = minion.reactToClick(x, y);
            if (optionalSelected.isPresent()) {
                CardView newSelected = optionalSelected.get();

                bus.post(GuiEvent.builder()
                                .name(GuiEventName.MINION_CARD_SELECTED)
                                .cardView(newSelected)
                                .minionView(minion)
                                .teamView(this)
                                .build()
                );

            }
        }
    }

    @Override
    public void handle(ActionEvent event) {
        switch (event.getName()) {
            case MINION_DIED -> onMinionDied(event);
        }
    }

    private void onMinionDied(ActionEvent event) {
        if (event.getOwnTeam() == team) {
            minions.removeIf(v -> v.getMinion() == event.getMinion());
        }
    }

    public Team getTeam() {
        return team;
    }
}
