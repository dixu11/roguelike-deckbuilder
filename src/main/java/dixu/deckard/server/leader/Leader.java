package dixu.deckard.server.leader;


import dixu.deckard.server.card.Card;
import dixu.deckard.server.event.*;
import dixu.deckard.server.event.bus.Bus;
import dixu.deckard.server.minion.Minion;
import dixu.deckard.server.team.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dixu.deckard.server.game.GameParams.*;

/**
 * {@link Leader} is player entity or computer that is controlling one side of a combat.
 * {@link Leader} has  {@link Team} of {@link Minion}s combating for him but not directly controlled.
 * {@link Leader} can decide to spend his {@link Leader#energy} to cast {@link Special} actions to mess
 * with {@link Minion}s {@link Card}s.
 * <p>
 * His special abilities are listed in {@link Special} class.
* */

public class Leader implements EventHandler {
    private final Team team;
    private final List<Card> hand = new ArrayList<>();
    private final Map<ActionEventType,Special> specials = new HashMap<>();
    private int energy = 0;

    public Leader(Team team) {
        this.team = team;

        specials.put(ActionEventType.LEADER_SPECIAL_STEAL, new Special(STEAL_SPECIAL_COST));
        specials.put(ActionEventType.LEADER_SPECIAL_GIVE, new Special(UPGRADE_SPECIAL_COST));
        specials.put(ActionEventType.LEADER_SPECIAL_MOVE_HAND, new Special(MOVE_SPECIAL_COST));

        Bus.register(this, ActionEventType.LEADER_SPECIAL_GIVE);
        Bus.register(this, ActionEventType.LEADER_SPECIAL_MOVE_HAND);
        Bus.register(this, ActionEventType.LEADER_SPECIAL_STEAL);
    }

    //SKILLS
    public void regenerateEnergy() {
        energy = INITIAL_ENERGY;
        Bus.post(ActionEvent.builder()
                .type(ActionEventType.LEADER_ENERGY_CHANGED)
                .leader(this)
                .value(energy)
                .build()
        );
    }

    public boolean canAfford(ActionEventType actionEventType) {
        if (!actionEventType.isSpecial()) return false;

        return energy - specials.get(actionEventType).getCost() >= 0;
    }

    @Override
    public void handle(ActionEvent event) {
        if (!event.isSpecial()) return;

        spendEnergy(event);

        if (event.getType() == ActionEventType.LEADER_SPECIAL_GIVE && event.getLeader().equals(this)) {
           removeCard(event.getCard());
        } else if (event.getType() == ActionEventType.LEADER_SPECIAL_STEAL && event.getLeader().equals(this)) {
            addCard(event.getCard());
        }
        //when special hand move is played leader spends energy but has nothing else to do
    }

    private void spendEnergy(ActionEvent event) {
        int cost = specials.get(event.getType()).getCost();
        energy -= cost;
        Bus.post(ActionEvent.builder()
                .type(ActionEventType.LEADER_ENERGY_CHANGED)
                .leader(this)
                .value(energy)
                .build()
        );
    }

    //HAND
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
        Bus.post(ActionEvent.builder()
                .type(ActionEventType.LEADER_HAND_CHANGED)
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
