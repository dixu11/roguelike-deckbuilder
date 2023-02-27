package dixu.deckard.server.event;

import dixu.deckard.client.EventCounterView;

public interface EventCounterEvent extends Event {
    void accept(EventCounterView visitor);
}
