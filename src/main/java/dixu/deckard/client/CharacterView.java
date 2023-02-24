package dixu.deckard.client;

import dixu.deckard.server.Character;

import java.awt.*;

public class CharacterView {

    private Character character;
    private CardView cardView;

    public CharacterView(Character character) {
        this.character = character;
        cardView = new CardView(character.getName());
        cardView.addCounter(new CounterView(Direction.BOTTOM,Direction.RIGHT, character::getHealth));
    }

    public void render(Graphics g) {
        g.setColor(Color.GRAY);
        cardView.render(g);
    }



}
