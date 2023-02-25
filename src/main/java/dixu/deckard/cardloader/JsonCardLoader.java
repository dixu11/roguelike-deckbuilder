package dixu.deckard.cardloader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonCardLoader {
    private final static String PATH = "src/main/java/dixu/deckard/cardloader/exampleCards.json";
    private final Gson gson = new Gson();

    public Set<LoadedCard> load(String path) {
        File jsonFile = new File(path);

        String rawJson;

        try (BufferedReader br  = new BufferedReader(new FileReader(jsonFile))) {
            rawJson = br.lines()
                    .map(String::trim)
                    .collect(Collectors.joining(""));
            System.out.println(rawJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Set<LoadedCard> cards = null;
        if (!rawJson.isEmpty()) {
            TypeToken<Set<LoadedCard>> cardSetType = new TypeToken<>(){};
            cards = gson.fromJson(rawJson, cardSetType);
        }

        return Objects.requireNonNullElse(cards, new HashSet<>());
    }

    public static void main(String[] args) {
        JsonCardLoader testLoader = new JsonCardLoader();
        Set<LoadedCard> loadedCards = testLoader.load(PATH);

        loadedCards.forEach(System.out::println);
    }

}
