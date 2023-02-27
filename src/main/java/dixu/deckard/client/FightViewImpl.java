package dixu.deckard.client;

import dixu.deckard.server.GameController;
import dixu.deckard.server.FightView;
import dixu.deckard.server.event.Event;
import dixu.deckard.server.event.EventBus;
import dixu.deckard.server.event.EventHandler;
import dixu.deckard.server.event.GameOverEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static dixu.deckard.client.GuiParams.*;

public class FightViewImpl implements FightView, MouseListener, EventHandler {
    private final EventBus bus = EventBus.getInstance();
    private final TeamView firstTeam;
    private final TeamView secondTeam;
    private final EndTurnButtonView endTurn = new EndTurnButtonView();
    private GameController controller;//todo możliwe że będzie do usinięcia


    public FightViewImpl(TeamView firstTeam, TeamView secondTeam) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;

        bus.register(this, GameOverEvent.class);
    }

    //animations
    public void tick() {

    }

    //rendering
    public void render(Graphics g) {
        renderBackground(g);
        firstTeam.render(g);
        secondTeam.render(g);
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
    public void handle(Event event) {
        if (event instanceof GameOverEvent) {
            JOptionPane.showMessageDialog(null, "Game over! ");
            System.exit(0);
        }
    }

    public void setController(GameController controller) {
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
