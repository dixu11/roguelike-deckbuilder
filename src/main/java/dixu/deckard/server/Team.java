package dixu.deckard.server;

import dixu.deckard.client.CounterSource;

import java.util.List;

public class Team {
    private List<Character> characters;
    private int block;

    public Team(List<Character> characters) {
        this.characters = characters;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void playBlocks() {
        for (Character character : characters) {
            character.playBlocks(this);
        }
    }

    public void playAttacks() {

    }

    public void addBlock(int value) {
        block += value;
    }

    public int getBlock() {
        return block;
    }
}
