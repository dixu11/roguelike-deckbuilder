package dixu.deckard.server;

public class CardPlayedEvent implements Event {
    private final Player player;
    private final Card card;
    private final Character target;

    public CardPlayedEvent(Player player, Card card, Character target) {
        this.player = player;
        this.card = card;
        this.target = target;
    }

    public Player getPlayer() {
        return player;
    }

    public Card getCard() {
        return card;
    }
}
