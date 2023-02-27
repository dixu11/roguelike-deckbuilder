package dixu.deckard.client;

import dixu.deckard.server.GameController;
import dixu.deckard.server.FightView;
import dixu.deckard.server.event.EventBus;
import dixu.deckard.server.event.EventHandler;
import dixu.deckard.server.event.GameOverEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FightViewImpl implements FightView, MouseListener, EventHandler<GameOverEvent> {
    private final TeamView playerTeam;
    private final TeamView enemyTeam;
    private final EndTurnButtonView button = new EndTurnButtonView();
    private GameController controller;


    public FightViewImpl(TeamView playerTeam, TeamView enemyTeam) {
        this.playerTeam = playerTeam;
        this.enemyTeam = enemyTeam;

        EventBus.getInstance().register(this, GameOverEvent.class);
    }

    //animations
    public void tick() {

    }

    //rendering
    public void render(Graphics g) {
        renderBackground(g);
        playerTeam.render(g);
        enemyTeam.render(g);
        button.render(g);
    }

    private void renderBackground(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, Display.getWidth(), Display.getHeight());
    }


    //interaction
    public void setController(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (button.isClicked(e.getX(), e.getY())) {
            button.onClick();
        }
    }

    @Override
    public void handle(GameOverEvent event) {
        JOptionPane.showMessageDialog(null, "Koniec gry! ");
        System.exit(0);

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
