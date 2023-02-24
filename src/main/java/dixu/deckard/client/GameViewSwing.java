package dixu.deckard.client;

import dixu.deckard.server.GameView;

import java.awt.*;

public class GameViewSwing implements GameView {
    private PlayerViewSwing player;
    private EndTurnButton button;

    public GameViewSwing(PlayerViewSwing player, EndTurnButton button) {
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
}
