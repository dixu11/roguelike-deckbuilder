package dixu.deckard.server;

import java.util.List;
import java.util.Random;

public class Team {
    private List<Character> characters;
    private int block;
    private TeamSide side;

    public Team(List<Character> characters,int block, TeamSide side) {
        this.characters = characters;
        this.block = block;
        this.side = side;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void playCards() {
        for (Character character : characters) {
            character.playCards(this);
        }
    }

    public void addBlock(int value) {
        block += value;
    }

    public int getBlock() {
        return block;
    }

    public TeamSide getSide() {
        return side;
    }

    public void applyRandomDmg(int value) {
        if (block <= value) {
            value -= block;
            block = 0;
        } else {
            block -= value;
            return;
        }
        Random random = new Random();
        characters.get(random.nextInt(characters.size())).obtainDamage(value);
    }
}
