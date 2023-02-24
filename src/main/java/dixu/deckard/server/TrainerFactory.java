package dixu.deckard.server;

import java.util.ArrayList;
import java.util.List;

public class TrainerFactory {

    private CharacterFactory characterFactory = new CharacterFactory();

    public Player createPlayer() {
        List<Character> characters = new ArrayList<>();
        characters.add(new Character());
        characters.add(new Character());
        return new Player(characters);
    }

    public Computer createComputer() {
        List<Character> characters = new ArrayList<>();
        characters.add(new Character());
        characters.add(new Character());
        return new Computer(characters);
    }
}
