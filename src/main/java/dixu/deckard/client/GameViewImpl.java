package dixu.deckard.client;

import dixu.deckard.server.GameController;
import dixu.deckard.server.GameView;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameViewImpl implements GameView, MouseListener {
    private TeamView playerTeam;
    private TeamView enemyTeam;
    private EndTurnButtonView button;
    private GameController controller;


    public GameViewImpl(EndTurnButtonView button, TeamView playerTeam, TeamView enemyTeam) {
        this.playerTeam = playerTeam;
        this.enemyTeam = enemyTeam;
        this.button = button;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, Display.getWidth(), Display.getHeight());
        button.render(g);
        playerTeam.render(g);
        enemyTeam.render(g);
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (button.isClicked(e.getX(), e.getY())) {
            button.onClick(controller);
        }
    }

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
