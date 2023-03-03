package dixu.deckard.client;

import dixu.deckard.server.event.BusManager;
import dixu.deckard.server.event.GuiEventHandler;
import dixu.deckard.server.event.GuiEventName;

public class GuiController implements GuiEventHandler {
    private final BusManager bus = BusManager.instance();
    private LeaderHandView leaderHand;
    private TeamView firstTeam;
    private TeamView secondTeam;

    public GuiController(FightViewImpl fightView) {
        this.leaderHand = fightView.getLeaderHand();
        this.firstTeam = fightView.getFirstTeam();
        this.secondTeam = fightView.getSecondTeam();

        bus.register(this, GuiEventName.LEADER_CARD_SELECTED);
        bus.register(this, GuiEventName.MINION_CARD_SELECTED);
    }

    @Override
    public void handle(GuiEvent event) {
        switch (event.getName()) {
            case LEADER_CARD_SELECTED -> onLeaderCardSelected();
            case MINION_CARD_SELECTED -> onMinionCardSelected();
        }
    }

    void onLeaderCardSelected() {
        System.out.println("Leader card selected: " + leaderHand.getSelected().getCard());
    }

    void onMinionCardSelected() {

    }
}
