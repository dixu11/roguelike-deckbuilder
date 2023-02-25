package dixu.deckard.client;

import dixu.deckard.server.Character;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharacterView {

    private Character character;
    private CardView cardView;
    private HandView handView;
    private List<CounterView> counters = new ArrayList<>();

    public CharacterView(Character character) {
        this.character = character;
        this.handView = new HandView(character.getHand());
        cardView = new CardView(  0,new ArrayList<>(List.of(character.getCharacterCard())));
        cardView.addCounter(new CounterView(Direction.BOTTOM, Direction.RIGHT, character::getHealth));
        counters.add(new CounterView(Direction.BOTTOM,Direction.LEFT,()->character.getDraw().size(), Color.GRAY));
        CounterView discardCounter = new CounterView(Direction.BOTTOM, Direction.RIGHT, () -> character.getDiscard().size(), Color.GRAY);
        discardCounter.setBlinking(false);
        counters.add(discardCounter);
    }

    public void render(Graphics g) {
        cardView.render(g);
        g.translate(-CardView.CARD_WIDTH,-CardView.CARD_HEIGHT -20);
        handView.render(g);
        g.translate(CardView.CARD_WIDTH,CardView.CARD_HEIGHT +20);
        Rectangle r = new Rectangle(0,CardView.CARD_HEIGHT/2,CardView.CARD_WIDTH,CardView.CARD_HEIGHT);
        for (CounterView counter : counters) {
            counter.render(g, r);
        }
    }


    public Character getCharacter() {
        return character;
    }
}
