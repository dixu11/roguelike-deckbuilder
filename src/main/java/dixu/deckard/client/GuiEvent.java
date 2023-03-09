package dixu.deckard.client;

import dixu.deckard.server.event.Event;
import dixu.deckard.server.event.GuiEventType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GuiEvent implements Event<GuiEventType> {

    private GuiEventType name;
    private CardView cardView;
    private MinionView minionView;
    private TeamView teamView;

    @Override
    public GuiEventType getType() {
        return name;
    }

    public static GuiEvent of(GuiEventType name) {
        return builder()
                .name(name)
                .build();
    }
}
