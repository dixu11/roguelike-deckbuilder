package dixu.deckard.server.event;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StateEvent implements Event<StateEventType> {

    private StateEventType type;
    @Override
    public StateEventType getType() {
        return type;
    }
}
