import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Game {

    private int row = -1;
    private final ArrayList<String> card;

    public ArrayList<String> names = new ArrayList<>(Arrays.asList(
            "John", "David", "Sarah", "Emily", "Michael", "Jessica", "Matthew", "Ashley"
    ));

    public Game() {
        card = new ArrayList<>();
        setupCards();
    }

    void setupCards() {
        String[] symbols = {"♠", "♥", "♦", "♣"};
        for (int i = 1; i < 14; i++) {
            for (int j = 0; j < 4; j++) {
                switch (i) {
                    case 1 -> card.add("A" + symbols[j]);
                    case 11 -> card.add("J" + symbols[j]);
                    case 12 -> card.add("Q" + symbols[j]);
                    case 13 -> card.add("K" + symbols[j]);
                    default -> card.add(String.valueOf(i) + symbols[j]);
                }
            }
        }
        shuffleCard();
    }

    public ArrayList<String> getCard() {
        return card;
    }

    public void shuffleCard() {
        Collections.shuffle(card);
        row = -1;
    }

    public String handOut() {
        return card.get(++row);
    }

    public ArrayList<String> getNames() {
        return names;
    }
}
