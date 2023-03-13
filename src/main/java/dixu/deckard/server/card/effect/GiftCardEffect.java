package dixu.deckard.server.card.effect;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.card.CardContext;
import dixu.deckard.server.card.CardFactory;
import dixu.deckard.server.event.ActionEvent;
import dixu.deckard.server.event.ActionEventType;
import dixu.deckard.server.event.EventHandler;
import dixu.deckard.server.event.bus.Bus;


public class GiftCardEffect extends BasicEffect implements CardEffect, EventHandler {
    private Card card;


    public GiftCardEffect(Card card) {
        super(0,card);
        this.card = card;
        Bus.register(this, ActionEventType.LEADER_SPECIAL_STEAL);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getCard() == card) {
            CardFactory factory = new CardFactory();
            event.getLeader().addCard(factory.createRandomCard());
        }
    }

    @Override
    public void execute(CardContext context) {

    }

    @Override
    public String getDescription() {
        return "Gives random card when stolen";
    }

    @Override
    public int getAttack() {
        return 0;
    }

    @Override
    public int getBlock() {
        return 0;
    }
}
