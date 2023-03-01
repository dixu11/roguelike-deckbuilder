package dixu.deckard.client;

import dixu.deckard.server.FightController;
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
    private FightController controller;//TODO możliwe że będzie do usinięcia


    public FightViewImpl(Leader firstLeader, Leader secondLeader) {
        this.firstTeam = new TeamView(firstLeader.getTeam(),Direction.LEFT);
        this.secondTeam = new TeamView(secondLeader.getTeam(),Direction.RIGHT);
        firstLeaderHand = new LeaderHandView(firstLeader);

        bus.register(this, CoreEventName.GAME_OVER);
    }

    //animations
    public void tick() {

    }

    //rendering
    public void render(Graphics g) {
        renderBackground(g);
        firstTeam.render(g);
        secondTeam.render(g);
        firstLeaderHand.render(g);
        endTurn.render(g);
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

    public void setController(FightController controller) {
        this.controller = controller;
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
