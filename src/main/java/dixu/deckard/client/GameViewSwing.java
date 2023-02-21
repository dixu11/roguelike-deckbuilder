package dixu.deckard.client;

import dixu.deckard.server.GameView;

import java.awt.*;

public class GameViewSwing implements GameView {

    private PlayerViewSwing computer;
    private PlayerViewSwing player;
    private EndTurnButton button;

    public GameViewSwing(PlayerViewSwing computer, PlayerViewSwing player, EndTurnButton button) {
        this.computer = computer;
        this.player = player;
        this.button = button;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, Display.getWidth(), Display.getHeight());
        computer.render(g);
        player.render(g);
        button.render(g);
    }
}
