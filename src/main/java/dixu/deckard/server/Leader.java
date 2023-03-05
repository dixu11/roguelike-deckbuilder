package dixu.deckard.server;


import dixu.deckard.server.event.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dixu.deckard.server.GameParams.*;

/**
 * {@link Leader} is player entity or computer that is controlling one side of a fight.
 * {@link Leader} has  {@link Team} of {@link Minion}s fighting for him but not directly controlled.
 * {@link Leader} can decide to spend his {@link Leader#energy} to cast {@link Special} actions to mess
 * with {@link Minion}s {@link Card}s.
 * <p>
 * His special abilities are listed in {@link Special} class.
* */

public class Leader implements ActionEventHandler {

    private final BusManager bus = BusManager.instance();
    private final int startingEnergy = INITIAL_ENERGY;
    private final Team team;
    private final List<Card> hand = new ArrayList<>();
    private final Map<ActionEventName,Special> specials = new HashMap<>();
    private int energy = 0;

    public Leader(Team team) {
        this.team = team;

        specials.put(ActionEventName.LEADER_SPECIAL_STEAL, new Special(STEAL_SPECIAL_COST));
        specials.put(ActionEventName.LEADER_SPECIAL_UPGRADE, new Special(UPGRADE_SPECIAL_COST));
        specials.put(ActionEventName.LEADER_SPECIAL_MOVE_HAND, new Special(MOVE_SPECIAL_COST));

        bus.register(this, ActionEventName.LEADER_SPECIAL_UPGRADE);
        bus.register(this, ActionEventName.LEADER_SPECIAL_MOVE_HAND);
        bus.register(this, ActionEventName.LEADER_SPECIAL_STEAL);
    }

    public void regenerateEnergy() {
        energy = startingEnergy;
        bus.post(ActionEvent.builder()
                .name(ActionEventName.LEADER_ENERGY_CHANGED)
                .leader(this)
                .value(energy)
                .build()
        );
    }

    public boolean canAfford(ActionEventName actionEventName) {
        if (!actionEventName.isSpecial()) return false;

        return energy - specials.get(actionEventName).getCost() >= 0;
    }

    @Override
    public void handle(ActionEvent event) {
        if (!event.isSpecial()) return;

        spendEnergy(event);

        if (event.getName() == ActionEventName.LEADER_SPECIAL_UPGRADE && event.getLeader().equals(this)) {
           removeCard(event.getCard());
        } else if (event.getName() == ActionEventName.LEADER_SPECIAL_STEAL && event.getLeader().equals(this)) {
            addCard(event.getCard());
        }
    }

    private void spendEnergy(ActionEvent event) {
        int cost = specials.get(event.getName()).getCost();
        energy -= cost;
        bus.post(ActionEvent.builder()
                .name(ActionEventName.LEADER_ENERGY_CHANGED)
                .leader(this)
                .value(energy)
                .build()
        );
    }

    private void removeCard(Card card) {
        hand.remove(card);
        postHandChangedEvent();
    }

   public void addCard(Card card) {
        hand.add(card);
        if (hand.size() > LEADER_MAX_HAND_SIZE) {
            hand.remove(0);
        }
        postHandChangedEvent();
    }

    private void postHandChangedEvent() {
        bus.post(ActionEvent.builder()
                .name(ActionEventName.LEADER_HAND_CHANGED)
                .leader(this)
                .build()
        );
    }

    public Team getTeam() {
        return team;
    }

    public int getEnergy() {
        return energy;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
