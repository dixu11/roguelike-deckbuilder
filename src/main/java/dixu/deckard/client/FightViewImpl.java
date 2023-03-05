package dixu.deckard.client;

import dixu.deckard.server.FightView;
import dixu.deckard.server.Leader;
import dixu.deckard.server.event.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static dixu.deckard.client.GuiParams.*;

public class FightViewImpl implements FightView, MouseListener, CoreEventHandler {
    private final BusManager bus = BusManager.instance();
    private final TeamView firstTeam;
    private final TeamView secondTeam;
    private final LeaderHandView firstLeaderHand;
    private final EndTurnButtonView endTurn = new EndTurnButtonView();
    private CounterView energyCounter;

    public FightViewImpl(Leader firstLeader, Leader secondLeader) {
        this.firstTeam = new TeamView(firstLeader.getTeam(),Direction.LEFT);
        this.secondTeam = new TeamView(secondLeader.getTeam(),Direction.RIGHT);
        firstLeaderHand = new LeaderHandView(firstLeader);

        bus.register(this, CoreEventName.GAME_OVER);

        setupCounters(firstLeader);
    }

    private void setupCounters(Leader firstLeader) {
        EventCounterView energyCounter = EventCounterView.builder()
                .color(MAIN_COLOR_BRIGHT)
                .description("⚡: ")
                .source(firstLeader)
                .strategy(((oldValue, e) -> e.getValue()))
                .build();

        bus.register(energyCounter, ActionEventName.LEADER_ENERGY_CHANGED);
        this.energyCounter = energyCounter;
    }

    //animations
    public void tick() {

    }

    //rendering
    public void render(Graphics2D g) {
        renderBackground(g);
        firstTeam.render(g);
        secondTeam.render(g);
        firstLeaderHand.render(g);
        endTurn.render(g);
        energyCounter.render(g,getWidth(0.34),getHeight(0.7));
    }

    private void renderBackground(Graphics g) {
        g.setColor(MAIN_COLOR_DARK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    //interaction
    @Override
    public void mouseReleased(MouseEvent e) {
        if (endTurn.isClicked(e.getX(), e.getY())) {
            endTurn.onClick();
        }
        firstLeaderHand.reactToClickOnScreen(e.getX(), e.getY());
        firstTeam.reactToClickOnScreen(e.getX(), e.getY());
        secondTeam.reactToClickOnScreen(e.getX(), e.getY());
    }

    @Override
    public void handle(CoreEvent event) {
        if (event.getName() == CoreEventName.GAME_OVER) {
            onGameOver();
        }
    }

    private static void onGameOver() {
        JOptionPane.showMessageDialog(null, "Game over! ");
        System.exit(0);
    }

    public LeaderHandView getLeaderHand() {
        return firstLeaderHand;
    }

    public TeamView getFirstTeam() {
        return firstTeam;
    }

    public TeamView getSecondTeam() {
        return secondTeam;
    }

    //garbage

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}
