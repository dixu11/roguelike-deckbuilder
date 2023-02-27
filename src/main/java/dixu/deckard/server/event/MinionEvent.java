package dixu.deckard.server.event;

import dixu.deckard.client.MinionView;

public interface MinionEvent extends Event {
    void accept(MinionView visitor);
}
