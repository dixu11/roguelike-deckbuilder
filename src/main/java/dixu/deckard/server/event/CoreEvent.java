package dixu.deckard.server.event;

import lombok.Builder;

@Builder
public class CoreEvent implements Event<CoreEventName> {

    private CoreEventName name;

    @Override
    public CoreEventName getName() {
        return null;
    }

    public static CoreEvent of(CoreEventName name) {
        return builder()
                .name(name)
                .build();
    }
}
