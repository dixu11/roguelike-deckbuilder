package dixu.deckard.server;

import java.util.ArrayList;
import java.util.List;

public class TrainerFactory {

    private CharacterFactory characterFactory = new CharacterFactory();

    public Team createPlayer() {
        List<Character> characters = new ArrayList<>();
        characters.add(new Character());
        characters.add(new Character());
        return new Team(characters,0, TeamSide.LEFT);
    }

    public Team createComputer() {
        List<Character> characters = new ArrayList<>();
        characters.add(new Character());
        characters.add(new Character());
        return new Team(characters,3, TeamSide.RIGHT);
    }
}
