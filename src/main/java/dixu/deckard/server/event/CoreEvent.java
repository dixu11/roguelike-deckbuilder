package dixu.deckard.server.event;

import lombok.Builder;

@Builder
public class CoreEvent implements Event<CoreEventType> {

    private CoreEventType type;

    @Override
    public CoreEventType getType() {
        return type;
    }

    public static CoreEvent of(CoreEventType type) {
        return builder()
                .type(type)
                .build();
    }
}
