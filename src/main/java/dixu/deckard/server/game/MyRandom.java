package dixu.deckard.server.game;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class MyRandom {
    private static final Random random = new Random();

    public static  <T> Optional<T> getRandomElement(List<T> collection) {
        if (collection.isEmpty()) {
            return Optional.empty();
        }
        T element = collection.get(random.nextInt(collection.size()));
        return Optional.of(element);
    }

}
