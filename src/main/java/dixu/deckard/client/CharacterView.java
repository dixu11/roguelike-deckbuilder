package dixu.deckard.client;

import dixu.deckard.server.Card;
import dixu.deckard.server.Character;

import java.awt.*;
import java.util.List;

public class CharacterView {

    private Character character;
    private CardView cardView;
    private HandView handView;

    public CharacterView(Character character) {
        this.character = character;
        this.handView = new HandView(character.getHand());
        cardView = new CardView(character.getName());
        cardView.addCounter(new CounterView(Direction.BOTTOM,Direction.RIGHT, character::getHealth));
    }

    public void render(Graphics g) {
        cardView.render(g);
        g.translate(-CardView.CARD_WIDTH,-CardView.CARD_HEIGHT -20);
        handView.render(g);
        g.translate(CardView.CARD_WIDTH,CardView.CARD_HEIGHT +20);
    }



}
