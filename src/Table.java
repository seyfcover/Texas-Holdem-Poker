import java.util.ArrayList;

public class Table {
    private final ArrayList<String> hand;
    private int minBet;
    private int countGame;



    public Table(int minBet, int countGame) {
        this.minBet = minBet;
        this.countGame = countGame;
        hand = new ArrayList<>(5);
    }

    public ArrayList<String> getHand() {
        return hand;
    }

    public void openHand(String card) {
        try {
            hand.add(card);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("table is full!");
        }
    }

    public int getMinBet() {
        return minBet;
    }

    public void setMinBet(int minBet) {
        this.minBet = minBet;
    }


    public int getCountGame() {
        return countGame;
    }

    public void setCountGame(int countGame) {
        this.countGame += countGame;
    }

    public void clear()
    {
        hand.clear();
    }

    public String[][] determinedCard() {
        String[][] cards = new String[5][2]; // Her oyuncu için 5x2'lik bir dizi oluştur
        String[] hand = getHand().toArray(new String[0]); // Oyuncunun elindeki kartları diziye dönüştür

        for (int i = 0; i < hand.length; i++) {
            String card = hand[i];
            if (card.length() == 2) {
                cards[i][0] = String.valueOf(card.charAt(0));
                cards[i][1] = String.valueOf(card.charAt(1));
            } else if (card.length() == 3) {
                cards[i][0] = String.valueOf(card.charAt(0)+String.valueOf(card.charAt(1)));
                cards[i][1] = String.valueOf(card.charAt(2));
            }
        }

        return cards;
    }


    @Override
    public String toString() {
        // Null olmayan kartları içeren yeni bir liste oluşturun
        ArrayList<String> nonNullCards = new ArrayList<>();
        for (String card : hand) {
            if (card != null) {
                nonNullCards.add(card);
            }
        }
        // Yeni listeyi string olarak formatlayın
        return "Table hand " + nonNullCards.toString();
    }
}
