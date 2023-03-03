package dixu.deckard.client;

import dixu.deckard.server.event.Event;
import dixu.deckard.server.event.GuiEventName;

public class GuiEvent implements Event<GuiEventName> {

    private GuiEventName name;

    public GuiEvent(GuiEventName name) {
        this.name = name;
    }

    @Override
    public GuiEventName getName() {
        return name;
    }
}
