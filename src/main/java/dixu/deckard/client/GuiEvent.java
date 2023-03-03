package dixu.deckard.client;

import dixu.deckard.server.event.CoreEvent;
import dixu.deckard.server.event.CoreEventName;
import dixu.deckard.server.event.Event;
import dixu.deckard.server.event.GuiEventName;
import lombok.Builder;

@Builder
public class GuiEvent implements Event<GuiEventName> {

    private GuiEventName name;

    public GuiEvent(GuiEventName name) {
        this.name = name;
    }

    @Override
    public GuiEventName getName() {
        return name;
    }

    public static GuiEvent of(GuiEventName name) {
        return builder()
                .name(name)
                .build();
    }
}
