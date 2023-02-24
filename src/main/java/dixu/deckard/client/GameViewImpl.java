package dixu.deckard.client;

import dixu.deckard.server.GameController;
import dixu.deckard.server.GameView;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameViewImpl implements GameView, MouseListener {
    private PlayerView player;
    private EndTurnButtonView button;
    private GameController controller;


    public GameViewImpl(PlayerView player, EndTurnButtonView button) {
        this.player = player;
        this.button = button;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, Display.getWidth(), Display.getHeight());
        player.render(g);
        button.render(g);
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
