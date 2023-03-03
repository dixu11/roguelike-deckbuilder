package dixu.deckard.client;

import dixu.deckard.server.Card;
import dixu.deckard.server.event.*;

public class GuiController implements GuiEventHandler {
    private final BusManager bus = BusManager.instance();
    private LeaderHandView leaderHand;
    private TeamView firstTeam;
    private TeamView secondTeam;

    public GuiController(FightViewImpl fightView) {
        this.leaderHand = fightView.getLeaderHand();
        this.firstTeam = fightView.getFirstTeam();
        this.secondTeam = fightView.getSecondTeam();

        bus.register(this, GuiEventName.MINION_CARD_SELECTED);
    }

    @Override
    public void handle(GuiEvent event) {
        switch (event.getName()) {
            case MINION_CARD_SELECTED -> onMinionCardSelected(event);
        }
    }

    private void onMinionCardSelected(GuiEvent event) {
        if (event.getTeamView() == firstTeam) {
            upgradeDeckSpecial(event);
        } else {
            stealCardSpecial(event);
        }
    }

    private void upgradeDeckSpecial(GuiEvent event) {
        Card leaderCard = selectedLeaderCard();
        Card minionCard = selectedfirstTeamCard();
        if (leaderCard == null || minionCard == null) {
            return;
        }

        leaderHand.remove(leaderCard);
        leaderHand.clearSelect();
        firstTeam.clearSelect();

        bus.post(ActionEvent.builder()
                        .name(ActionEventName.LEADER_SPECIAL_UPGRADE)
                        .leader(leaderHand.getLeader())
                        .ownTeam(firstTeam.getTeam())
                        .enemyTeam(secondTeam.getTeam())
                        .minion(event.getMinionView().getMinion())
                        .card(leaderCard)
                        .oldCard(minionCard)
                        .build()
        );
    }

    private void stealCardSpecial(GuiEvent event) {
        if (event.getTeamView() != secondTeam) {
            return;
        }
        CardView selected = event.getCardView();
        if (!selected.isSelected()) {
            selected.toggleSelected();
            return;
        }
        selected.toggleSelected();
        bus.post(ActionEvent.builder()
                        .name(ActionEventName.LEADER_SPECIAL_STEAL)
                        .leader(leaderHand.getLeader())
                        .ownTeam(firstTeam.getTeam())
                        .enemyTeam(secondTeam.getTeam())
                        .minion(event.getMinionView().getMinion())
                        .card(event.getCardView().getCard())
                        .build()
        );
        leaderHand.add(event.getCardView().getCard());
    }

    private void changeMinionHandSpecial() {

    }

    private Card selectedLeaderCard() {
        if (leaderHand.getSelected() == null) {
            return null;
        }
        return leaderHand.getSelected().getCard();
    }

    private Card selectedfirstTeamCard() {
        return firstTeam.getSelected();
    }

}
