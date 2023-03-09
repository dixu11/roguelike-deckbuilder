package dixu.deckard.client;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.event.*;

public class GuiController implements GuiEventHandler {
    private final BusManager bus = BusManager.instance();
    private final LeaderHandView leaderHand;
    private final TeamView firstTeam;
    private final TeamView secondTeam;
    private final SelectView leaderCardSelect = new SelectView();
    private final SelectView secondTeamSelect = new SelectView();
    private final SelectView firstTeamMinionSelect = new SelectView();

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
        if (!leaderHand.getLeader().canAfford(ActionEventName.LEADER_SPECIAL_UPGRADE)) {
            clearSelections();
            return;
        }

        leaderCardSelect.newCardSelected(event.getCardView());
        if (leaderCardSelect.isDoubleClick()) {
            leaderCardSelect.clearSelection();
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
        if (leaderCardSelect.getSelected() == null || event.getCardView() == null) {
            return;
        }
        if (!leaderHand.getLeader().canAfford(ActionEventName.LEADER_SPECIAL_UPGRADE)) {
            clearSelections();
            return;
        }

        Card minionCard = event.getCardView().getCard();
        Card selectedLeaderCard = leaderCardSelect.getSelected().getCard();
        leaderCardSelect.clearSelection();

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
        if (!leaderHand.getLeader().canAfford(ActionEventName.LEADER_SPECIAL_STEAL)) {
            clearSelections();
            return;
        }

        CardView newSelected = event.getCardView();
        secondTeamSelect.newCardSelected(newSelected);
        if (!secondTeamSelect.isDoubleClick()) return;

        bus.post(ActionEvent.builder()
                .name(ActionEventName.LEADER_SPECIAL_STEAL)
                .leader(leaderHand.getLeader())
                .ownTeam(firstTeam.getTeam())
                .enemyTeam(secondTeam.getTeam())
                .minion(event.getMinionView().getMinion())
                .card(newSelected.getCard())
                .build()
        );
    }

    private void moveMinionHandSpecial(GuiEvent event) {
        if (!leaderHand.getLeader().canAfford(ActionEventName.LEADER_SPECIAL_MOVE_HAND)) {
            clearSelections();
            return;
        }

        CardView newSelected = event.getCardView();
        if (event.getTeamView() != firstTeam) {
            return;
        }

        firstTeamMinionSelect.newCardSelected(newSelected);
        if (!firstTeamMinionSelect.isDoubleClick()) return;

        bus.post(ActionEvent.builder()
                .name(ActionEventName.LEADER_SPECIAL_MOVE_HAND)
                .leader(leaderHand.getLeader())
                .ownTeam(firstTeam.getTeam())
                .enemyTeam(secondTeam.getTeam())
                .minion(event.getMinionView().getMinion())
                .card(event.getCardView().getCard())
                .build()
        );
        firstTeamMinionSelect.clearSelection();
    }

    private void clearSelections() {
      firstTeamMinionSelect.clearSelection();
        secondTeamSelect.clearSelection();
        leaderCardSelect.clearSelection();
    }

    static class SelectView {
        private CardView selectedCard = null;
        private boolean doubleClick = false;

        public void newCardSelected(CardView newSelected) {
            doubleClick = false;
            if (selectedCard == null) {
                selectedCard = newSelected;
                selectedCard.setSelected(true);
                return;
            }

            if (!newSelected.isSelected()) {
                selectedCard.setSelected(false);
                selectedCard = newSelected;
                selectedCard.setSelected(true);
                return;
            }
            doubleClick = true;
        }

        private void clearSelection() {
            if (selectedCard != null) {
                selectedCard.setSelected(false);
                selectedCard = null;
            }
        }

        public boolean isDoubleClick() {
            return doubleClick;
        }

        public CardView getSelected() {
            return selectedCard;
        }

    }
}
