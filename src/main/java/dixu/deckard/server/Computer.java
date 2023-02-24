package dixu.deckard.server;

import java.util.List;

public class Computer {
    private List<Character> characters;

    public Computer(List<Character> characters) {
        this.characters = characters;
    }

    public List<Character> getTeam() {
        return characters;
    }
}
