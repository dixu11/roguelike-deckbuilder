package dixu.deckard.server;

import java.util.*;
import java.util.stream.IntStream;

public class Character {
    private String name;
    private int hp = 3;
    private static int nextChar = 1;
    private Card characterCard = new Card(CardType.CHARACTER);
    private LinkedList<Card> draw = new LinkedList<>();
    private List<Card> hand = new LinkedList<>();
    private List<Card> discard = new LinkedList<>();

    public Character() {
        name = "Character " + nextChar++;
        IntStream.range(0, 2)
                .forEach(n -> draw.add(new Card(CardType.BLOCK)));
        IntStream.range(0, 2)
                .forEach(n -> draw.add(new Card(CardType.ATTACK)));
        Collections.shuffle(draw);

        drawTwo();
    }

    public void drawTwo() {
        if (draw.size() < 2) {
            Collections.shuffle(discard);
            draw.addAll(discard);
            discard.clear();
        }
        hand.add(draw.remove());
        hand.add(draw.remove());
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return hp;
    }

    public List<Card> getHand() {
        return hand;
    }

    public Queue<Card> getDraw() {
        return draw;
    }

    public List<Card> getDiscard() {
        return discard;
    }

    public void playCards(Team team) {
        for (Card card : new ArrayList<>(hand)) {
            EventBus.getInstance().post(new CardPlayedEvent(team, card, this));
            card.play(team, this);
        }
    }

    public void remove(Card card) {
        hand.remove(card);
        discard.add(card);
    }

    public Card getCharacterCard() {
        return characterCard;
    }

    public void obtainDamage(Team team,int value) {
        hp -= value;
        if (hp <= 0) {
            EventBus.getInstance().post(new CharacterDiedEvent(team.getSide(),this));
        }
    }

    public boolean isDead() {
        return hp <= 0;
    }
}
