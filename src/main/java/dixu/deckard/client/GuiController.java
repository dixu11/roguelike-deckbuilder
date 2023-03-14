package dixu.deckard.client;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.event.*;
import dixu.deckard.server.event.bus.Bus;
import dixu.deckard.server.minion.Minion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiController implements EventHandler {
    private static final Logger logger = LogManager.getLogger(GuiController.class);
    private final LeaderHandView leaderHand;
    private final TeamView firstTeam;
    private final TeamView secondTeam;
    private final SelectView leaderCardSelect = new SelectView();
    private final SelectView secondTeamHandSelect = new SelectView();
    private final SelectView firstTeamHandSelect = new SelectView(); //todo implement
    private final SelectView firstTeamMinionSelect = new SelectView();

    public GuiController(CombatView combatView) {
        this.leaderHand = combatView.getLeaderHand();
        this.firstTeam = combatView.getFirstTeam();
        this.secondTeam = combatView.getSecondTeam();

        Bus.register(this, GuiEventType.MINION_CARD_SELECTED);
        Bus.register(this, GuiEventType.MINION_SELECTED);
        Bus.register(this, GuiEventType.LEADER_CARD_SELECTED);
        Bus.register(this, GuiEventType.BACKGROUND_CLICK);
        Bus.register(this, CoreEventType.MINION_PHASE_STARTED);
    }

    @Override
    public void handle(GuiEvent event) {
        switch (event.getType()) {
            case LEADER_CARD_SELECTED -> selectLeaderCard(event);
            case MINION_CARD_SELECTED -> onMinionCardSelected(event);
            case MINION_SELECTED -> onMinionSelected(event);
            case BACKGROUND_CLICK -> clearSelections();
        }
    }

    @Override
    public void handle(CoreEvent event) {
        switch (event.getType()) {
            case MINION_PHASE_STARTED -> clearSelections();
        }
    }

    private void onMinionCardSelected(GuiEvent event) {
        if (!leaderHand.getLeader().canAfford(ActionEventType.LEADER_SPECIAL_STEAL)) {
            clearSelections();
            return;
        }
        CardView newSelected = event.getCardView();
        if (event.getTeamView().equals(firstTeam)) {
            firstTeamHandSelect.newCardSelected(newSelected);
        } else {
            secondTeamHandSelect.newCardSelected(newSelected);
        }

        if (event.getTeamView() == firstTeam) {
            if (secondTeamHandSelect.isSelected()) {
                stealCardSpecial(event);
            } else if (leaderCardSelect.isSelected()) {
                giveSpecial(event);
            }
        } else {
            stealCardSpecial(event);
        }
    }

    private void onMinionSelected(GuiEvent event) {
        if (event.getTeamView() != firstTeam) {
            return;
        }
        CardView newSelected = event.getCardView();
        firstTeamMinionSelect.newCardSelected(newSelected);

        if (secondTeamHandSelect.isSelected()) {
            stealCardSpecial(event);
        } else if (leaderCardSelect.isSelected()) {
            giveSpecial(event);
        } else {
            moveMinionHandSpecial(event);
        }
    }

    void clearSelections() {
        firstTeamMinionSelect.clearSelection();
        firstTeamHandSelect.clearSelection();
        secondTeamHandSelect.clearSelection();
        leaderCardSelect.clearSelection();
    }

    private void selectLeaderCard(GuiEvent event) {
        if (!leaderHand.getLeader().canAfford(ActionEventType.LEADER_SPECIAL_GIVE)) {
            clearSelections();
            return;
        }

        leaderCardSelect.newCardSelected(event.getCardView());
        if (leaderCardSelect.isDoubleClick()) {
            leaderCardSelect.clearSelection();
        }
    }

    private void giveSpecial(GuiEvent event) {
        if (!leaderCardSelect.isSelected() || event.getCardView() == null) {
            return;
        }
        if (!leaderHand.getLeader().canAfford(ActionEventType.LEADER_SPECIAL_GIVE)) {
            clearSelections();
            return;
        }

        Card minionCard = event.getCardView().getCard();
        Card selectedLeaderCard = leaderCardSelect.getSelected().getCard();
        leaderCardSelect.clearSelection();

        Bus.post(ActionEvent.builder()
                .type(ActionEventType.LEADER_SPECIAL_GIVE)
                .leader(leaderHand.getLeader())
                .ownTeam(firstTeam.getTeam())
                .enemyTeam(secondTeam.getTeam())
                .targetMinion(event.getMinionView().getMinion())
                .card(selectedLeaderCard)
                .oldCard(minionCard)
                .build()
        );
    }

    private void stealCardSpecial(GuiEvent event) {
        if (secondTeamHandSelect.isDoubleClick()) {
            stealToLeader(event);
        } else if (firstTeamMinionSelect.isSelected()) {
            stealToDeck(event);
        } else if (firstTeamHandSelect.isSelected()) {
            stealSwap();
        } else {
            return;
        }
        clearSelections();
    }

    private void stealToLeader(GuiEvent event) {
        logger.debug("steal to leader selected");
        CardView newSelected = event.getCardView();
        Bus.post(ActionEvent.builder()
                .type(ActionEventType.LEADER_SPECIAL_STEAL)
                .subtype(ActionEventSubtype.STEAL_TO_LEADER)
                .leader(leaderHand.getLeader())
                .ownTeam(firstTeam.getTeam())
                .enemyTeam(secondTeam.getTeam())
                .targetMinion(event.getMinionView().getMinion())
                .card(newSelected.getCard())
                .build()
        );
    }

    private void stealToDeck(GuiEvent event) {
        logger.debug("steal to deck selected");
        Minion ownedMinion = firstTeamMinionSelect.getSelected().getCard().getOwner();
        Card stolenCard = secondTeamHandSelect.getSelected().getCard();

        Bus.post(ActionEvent.builder()
                .type(ActionEventType.LEADER_SPECIAL_STEAL)
                .subtype(ActionEventSubtype.STEAL_TO_DECK)
                .leader(leaderHand.getLeader())
                .ownTeam(firstTeam.getTeam())
                .enemyTeam(secondTeam.getTeam())
                .minion(stolenCard.getOwner())
                .targetMinion(ownedMinion)
                .card(stolenCard)
                .build()
        );
    }

    private void stealSwap() {
        logger.debug("steal to swap selected");
        Card ownCard = firstTeamHandSelect.getSelected().getCard();
        Card enemyCard = secondTeamHandSelect.getSelected().getCard();

        Bus.post(ActionEvent.builder()
                .type(ActionEventType.LEADER_SPECIAL_STEAL)
                .subtype(ActionEventSubtype.STEAL_TO_SWAP)
                .leader(leaderHand.getLeader())
                .ownTeam(firstTeam.getTeam())
                .enemyTeam(secondTeam.getTeam())
                .minion(ownCard.getOwner())
                .targetMinion(enemyCard.getOwner())
                .card(enemyCard)
                .oldCard(ownCard)
                .build()
        );
    }

    private void moveMinionHandSpecial(GuiEvent event) {
        if (!leaderHand.getLeader().canAfford(ActionEventType.LEADER_SPECIAL_MOVE_HAND)) {
            clearSelections();
            return;
        }

        if (!firstTeamMinionSelect.isDoubleClick()) return;

        Bus.post(ActionEvent.builder()
                .type(ActionEventType.LEADER_SPECIAL_MOVE_HAND)
                .leader(leaderHand.getLeader())
                .ownTeam(firstTeam.getTeam())
                .enemyTeam(secondTeam.getTeam())
                .targetMinion(event.getMinionView().getMinion())
                .card(event.getCardView().getCard())
                .build()
        );
        firstTeamMinionSelect.clearSelection();
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

        boolean isSelected() {
            return selectedCard != null;
        }

        public boolean isDoubleClick() {
            return doubleClick;
        }

        public CardView getSelected() {
            return selectedCard;
        }

    }
}
