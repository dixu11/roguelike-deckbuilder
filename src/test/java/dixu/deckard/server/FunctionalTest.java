package dixu.deckard.server;

import dixu.deckard.server.card.Card;
import dixu.deckard.server.card.CardFactory;
import dixu.deckard.server.card.CardType;
import dixu.deckard.server.event.ActionEventType;
import dixu.deckard.server.event.BusManager;
import dixu.deckard.server.event.CoreEvent;
import dixu.deckard.server.event.CoreEventType;
import dixu.deckard.server.fight.Fight;
import dixu.deckard.server.leader.Leader;
import dixu.deckard.server.leader.LeaderFactory;
import dixu.deckard.server.leader.LeaderType;
import dixu.deckard.server.minion.Minion;
import dixu.deckard.server.team.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static dixu.deckard.server.card.CardType.*;
import static dixu.deckard.server.game.GameParams.*;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class FunctionalTest {
    protected Team firstTeam;
    protected Team secondTeam;
    protected Leader firstLeader;
    protected Leader secondLeader;
    protected BusManager bus;

    @BeforeEach
    public void before() {
        setClassicParams();
        loadGame(); //remove if performance get worse because tests with custom params load engine twice
    }

    @AfterEach
    public void after() {
        //Buses are singleton and also needs to be re-initialized after each test
        BusManager.reInitialize();
    }

    private void setClassicParams() {
        MINION_PER_TEAM = 2;
        MINION_DRAW_PER_TURN = 2;
        SECOND_TEAM_INITIAL_BLOCK = 3;
        changeCardBaseValueTo(BASIC_MINION, 3);
        changeCardBaseValueTo(BASIC_ATTACK, 1);
        changeCardBaseValueTo(UPGRADED_ATTACK, 2);
        changeCardBaseValueTo(BASIC_BLOCK, 1);
        changeCardBaseValueTo(UPGRADED_BLOCK, 2);
    }

    protected void changeCardBaseValueTo(CardType type, int value) {
        type.setBaseValue(value);
    }

    protected int minionInitialHp() {
        return BASIC_MINION.getValue();
    }

    private void loadGame() { //todo refactor copying same code here and in App
        Fight.disableDelay();
        bus = BusManager.instance();
        LeaderFactory leaderFactory = new LeaderFactory();
        firstLeader = leaderFactory.create(LeaderType.PLAYER);
        secondLeader = leaderFactory.create(LeaderType.SIMPLE_BOT);
        firstTeam = firstLeader.getTeam();
        secondTeam = secondLeader.getTeam();
        Fight fight = new Fight(firstLeader, secondLeader);
        fight.start();
    }

    protected void reloadGame() {
        after(); //reset bus
        loadGame();
    }

    protected void executeTurn() {
        bus.post(CoreEvent.of(CoreEventType.TURN_ENDED));
    }

    protected List<Minion> allMinions() {
        List<Minion> all = new ArrayList<>();
        all.addAll(firstTeam.getMinions());
        all.addAll(secondTeam.getMinions());
        return all;
    }
    //todo could not find way to avoid repetition - my try was EventName interface but i can't figure out how to

    //put it back to overloaded  bus.register() call - it makes compile error -> 'suspicious call'
    protected <T> AtomicBoolean listenEventPosted(CoreEventType eventName) {
        //  T elem = eventName.getObject();
        AtomicBoolean wasPosted = new AtomicBoolean(false);
        bus.register(event -> wasPosted.set(true), eventName); // if i pass elem here - there's a problem
        return wasPosted;
    }

    protected AtomicBoolean listenEventPosted(ActionEventType eventName) {
        AtomicBoolean wasPosted = new AtomicBoolean(false);
        bus.register(event -> wasPosted.set(true), eventName);
        return wasPosted;
    }

    protected void failIfWasNotPosted(AtomicBoolean wasPosted) {
        if (!wasPosted.get()) {
            fail();
        }
    }

    protected void clearAllCards() {
        for (Minion minion : allMinions()) {
            clearMinionHand(minion);
            clearMinionDraw(minion);
        }
    }

    protected void clearMinionsHand(Team team) {
        giveMinionsCards(team);
    }

    protected void clearMinionHand(Minion minion) {
        composeMinionHand(minion);
    }

    protected void clearMinionDraw(Minion minion) {
        minion.clearDraw();
    }

    protected void disableBlockClear() {
        firstTeam.setClearBlockEnabled(false);
        secondTeam.setClearBlockEnabled(false);
    }

    /**
     * @param cards when no elements are passed minion has no cards in hand
     */
    protected void giveMinionsCards(Team team, CardType... cards) {
        team.getMinions()
                .forEach(minion -> composeMinionHand(minion, cards));
    }

    protected void giveAllMinionsBlockCard() {
        allMinions().forEach(minion -> composeMinionHand(minion, CardType.BASIC_BLOCK));
    }

    protected void composeMinionHand(Minion minion, CardType... types) {
        minion.setHand(createCards(types));
    }

    protected void composeMinionDiscard(Minion minion, CardType... types) {
        minion.setDiscard(createCards(types));
    }

    protected void composeMinionDraw(Minion minion, CardType... types) {
        minion.setDraw(createCards(types));
    }

    private List<Card> createCards(CardType... types) {
        CardFactory factory = new CardFactory();
        List<Card> cards = new ArrayList<>();
        for (CardType type : types) {
            cards.addAll(factory.createCards(1, type));
        }
        return cards;
    }

    public Minion firstMinion(Team team) {
        return team.getMinions().get(0);
    }

    public Minion secondMinion(Team team) {
        return team.getMinions().get(1);
    }

    protected Card minionHandFirstCard(Minion minion) {
        return minion.getHand().get(0);
    }

    protected void disableBasicAttack() {
        changeCardBaseValueTo(BASIC_ATTACK, 0);
    }

    protected void disableBasicBlock() {
        changeCardBaseValueTo(CardType.BASIC_BLOCK, 0);
    }
}
