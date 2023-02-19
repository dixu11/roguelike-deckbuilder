package dixu.deckard.server;

public class Card {
    private final String name;
    private final int dmg;
    private static int nextNr = 1;

    public Card() {
        name = "Attack "+ nextNr++;
        dmg = 1;
    }
}
