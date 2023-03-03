package dixu.deckard.client;

import dixu.deckard.server.Card;
import dixu.deckard.server.event.*;

public class GuiController implements GuiEventHandler {
    private final BusManager bus = BusManager.instance();
    private final LeaderHandView leaderHand;
    private final TeamView firstTeam;
    private final TeamView secondTeam;
    private CardView selectedLeaderCardView = null;
    private CardView secondTeamCardSelectedView = null;
    private CardView selectedMinionCardView = null;

    public GuiController(FightViewImpl fightView) {
        this.leaderHand = fightView.getLeaderHand();
        this.firstTeam = fightView.getFirstTeam();
        this.secondTeam = fightView.getSecondTeam();

        bus.register(this, GuiEventName.MINION_CARD_SELECTED);
        bus.register(this, GuiEventName.MINION_SELECTED);
        bus.register(this, GuiEventName.LEADER_CARD_SELECTED);
    }

    @Override
    public void handle(GuiEvent event) {
        switch (event.getName()) {
            case LEADER_CARD_SELECTED -> selectLeaderCard(event);
            case MINION_CARD_SELECTED -> onMinionCardSelected(event);
            case MINION_SELECTED -> moveMinionHandSpecial(event);
        }
    }

    private void selectLeaderCard(GuiEvent event) {
        if (selectedLeaderCardView == event.getCardView() && selectedLeaderCardView.isSelected()) { //duplicates too
            selectedLeaderCardView.setSelected(false);
            selectedLeaderCardView = null;
            return;
        }
        if (selectedLeaderCardView != null) {
            selectedLeaderCardView.setSelected(false);
        }
        selectedLeaderCardView = event.getCardView();
        selectedLeaderCardView.setSelected(true);
    }

    private void onMinionCardSelected(GuiEvent event) {
        if (event.getTeamView() == firstTeam) {
            upgradeDeckSpecial(event);
        } else {
            stealCardSpecial(event);
        }
    }

    private void upgradeDeckSpecial(GuiEvent event) {
        if (selectedLeaderCardView == null || event.getCardView() == null) {
            return;
        }
        Card minionCard = event.getCardView().getCard();
        Card selectedLeaderCard = selectedLeaderCardView.getCard();
        leaderHand.remove(selectedLeaderCard);

        selectedLeaderCardView.setSelected(false);

        bus.post(ActionEvent.builder()
                .name(ActionEventName.LEADER_SPECIAL_UPGRADE)
                .leader(leaderHand.getLeader())
                .ownTeam(firstTeam.getTeam())
                .enemyTeam(secondTeam.getTeam())
                .minion(event.getMinionView().getMinion())
                .card(selectedLeaderCard)
                .oldCard(minionCard)
                .build()
        );
    }

    private void stealCardSpecial(GuiEvent event) {
        CardView newSelected =event.getCardView();
        if (event.getTeamView() != secondTeam) {
            return;
        }
        if (secondTeamCardSelectedView == null) {
            secondTeamCardSelectedView = newSelected;
            secondTeamCardSelectedView.setSelected(true);
            return;
        }
        if (!newSelected.isSelected()) {
            secondTeamCardSelectedView.setSelected(false);
            secondTeamCardSelectedView = newSelected;
            secondTeamCardSelectedView.setSelected(true);
            return;
        }

        newSelected.setSelected(false);
        secondTeamCardSelectedView = null;

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

    private void moveMinionHandSpecial(GuiEvent event) {
        CardView newSelected =event.getCardView();  //todo figure out how to avoid duplicates
        if (event.getTeamView() != firstTeam) {
            return;
        }

        if (selectedMinionCardView == null) {
            selectedMinionCardView = newSelected;
            selectedMinionCardView.setSelected(true);
            return;
        }
        if (!newSelected.isSelected()) {
            selectedMinionCardView.setSelected(false);
            selectedMinionCardView = newSelected;
            selectedMinionCardView.setSelected(true);
            return;
        }

        newSelected.setSelected(false);
        selectedMinionCardView = null;

        bus.post(ActionEvent.builder()
                .name(ActionEventName.LEADER_SPECIAL_MOVE_HAND)
                .leader(leaderHand.getLeader())
                .ownTeam(firstTeam.getTeam())
                .enemyTeam(secondTeam.getTeam())
                .minion(event.getMinionView().getMinion())
                .card(event.getCardView().getCard())
                .build()
        );
        System.out.println("swap");
    }
}
