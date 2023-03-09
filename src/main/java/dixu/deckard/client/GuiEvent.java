package dixu.deckard.client;

import dixu.deckard.server.event.Event;
import dixu.deckard.server.event.GuiEventName;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GuiEvent implements Event<GuiEventName> {

    private GuiEventName name;
    private CardView cardView;
    private MinionView minionView;
    private TeamView teamView;

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
