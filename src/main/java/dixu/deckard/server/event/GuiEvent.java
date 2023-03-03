package dixu.deckard.server.event;

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
