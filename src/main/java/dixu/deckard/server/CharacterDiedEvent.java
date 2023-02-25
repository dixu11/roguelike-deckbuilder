package dixu.deckard.server;

public class CharacterDiedEvent implements Event{
    private TeamSide side;
    private Character character;

    public CharacterDiedEvent(TeamSide side, Character character) {
        this.side = side;
        this.character = character;
    }

    public TeamSide getSide() {
        return side;
    }

    public Character getCharacter() {
        return character;
    }
}
