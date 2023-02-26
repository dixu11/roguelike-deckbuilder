package dixu.deckard.server;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MyRandom {
    private static final Random random = new Random();

    public static  <T> T getRandomElement(List<T> collection) {
        return collection.get(random.nextInt(collection.size()));
    }

}
