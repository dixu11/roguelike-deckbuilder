package dixu.deckard.client;

import dixu.deckard.server.Character;

import java.awt.*;

public class CharacterView {

    private Character character;
    private CardView cardView;

    public CharacterView(Character character) {
        this.character = character;
        cardView = new CardView();
    }

    public void render(Graphics g) {
        g.setColor(Color.GRAY);
        cardView.render(g);
    }



}
