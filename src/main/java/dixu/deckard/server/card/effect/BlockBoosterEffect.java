package dixu.deckard.server.card.effect;

import dixu.deckard.server.event.ActionEvent;
import dixu.deckard.server.event.ActionEventType;
import dixu.deckard.server.event.EventHandler;
import dixu.deckard.server.event.bus.Bus;

public class BlockBoosterEffect extends BlockEffectDecorator implements EventHandler {
    public BlockBoosterEffect(BlockEffect decorated) {
        super(decorated);
        Bus.register(this, ActionEventType.TEAM_BLOCK_CHANGED);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " = double current block";
    }

    @Override
    public void handle(ActionEvent event) {
        if (getCard().getOwner() == null) {
            setValue(getInitialValue());
            return;
        }
        if (getCard().getOwner().getTeam().equals(event.getOwnTeam())) {
            setValue(event.getOwnTeam().getBlock());
        }
    }
}
