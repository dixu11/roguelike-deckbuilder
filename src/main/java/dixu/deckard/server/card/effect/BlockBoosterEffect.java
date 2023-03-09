package dixu.deckard.server.card.effect;

import dixu.deckard.server.event.ActionEvent;
import dixu.deckard.server.event.ActionEventHandler;
import dixu.deckard.server.event.ActionEventType;

public class BlockBoosterEffect extends BlockEffectDecorator implements ActionEventHandler {
    public BlockBoosterEffect(BlockEffect decorated) {
        super(decorated);
        bus.register(this, ActionEventType.TEAM_BLOCK_CHANGED);
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
