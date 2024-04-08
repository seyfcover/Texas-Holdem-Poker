import java.util.*;

public class Player extends HandSeries {

    private int money;
    private final ArrayList<String> hand;
    private int bet;
    private boolean isIngame = true;
    private double handAmount;
    private final String name;

    public Player(int money, String name) {
        this.money = money;
        this.name = name;
        hand = new ArrayList<>(2);
    }

    public int getMoney() {
        return money;
    }

    public ArrayList<String> getHand() {
        return hand;
    }

    public void addHand(String card) {
        try {
            hand.add(card);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("hand is full");
        }
    }

    public void updateMoney(int money) {
        this.money += money;
    }

    public boolean isAllin() {
        return getBet() == money;
    }

    public boolean isIngame() {
        return isIngame;
    }

    public void setIngame(boolean ingame) {
        isIngame = ingame;
    }

    public String getName() {
        return name;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public double getHandAmount() {
        return handAmount;
    }

    public void setHandAmount(double handAmount) {
        this.handAmount = handAmount;
    }

    public void clear() {
        hand.clear();
        setBet(0);
    }

    public String[][] determinedCard() {
        String[][] card = new String[2][2]; // İki oyuncu için 2x2'lik bir dizi oluştur
        String first = getHand().get(0);
        String second = getHand().get(1);

        // İlk oyuncunun kartlarını diziye yerleştir
        if (first.length() == 2) {
            card[0][0] = String.valueOf(first.charAt(0));
            card[0][1] = String.valueOf(first.charAt(1));
        } else if (first.length() == 3) {
            card[0][0] = first.charAt(0) + String.valueOf(first.charAt(1));
            card[0][1] = String.valueOf(first.charAt(2));
        }

        // İkinci oyuncunun kartlarını diziye yerleştir
        if (second.length() == 2) {
            card[1][0] = String.valueOf(second.charAt(0));
            card[1][1] = String.valueOf(second.charAt(1));
        } else if (second.length() == 3) {
            card[1][0] = (second.charAt(0) + String.valueOf(second.charAt(1)));
            card[1][1] = String.valueOf(second.charAt(2));
        }
        return card;
    }

    @Override
    public double calculateHand(Table table) {
        String[][] playerCards = determinedCard(); // Oyuncunun kartlarını al
        String[][] tableCards = table.determinedCard(); // Masa kartlarını al

        // Tüm kartları birleştir
        String[][] allCards = new String[7][2];
        System.arraycopy(playerCards, 0, allCards, 0, playerCards.length);
        System.arraycopy(tableCards, 0, allCards, playerCards.length, tableCards.length);
        // Kartları değerlerine göre sırala
        Arrays.sort(allCards, Comparator.comparingInt((String[] card) -> cardValue(card[0])));

        // Royal Flush kontrolü
        String U = isRoyalFlush(playerCards, tableCards);
        if (U != null) {
            return handValConverter(U);
        }
        String R = isStraight(playerCards, tableCards);
        // Straight Flush kontrolü
        if (R != null) {
            return handValConverter(R);
        }
        String H = isFourOfAKind(playerCards, tableCards);
        // Four of a Kind kontrolü
        if (H != null) {
            return handValConverter(H);
        }
        String F = isFullHouse(allCards);
        // Full House kontrolü
        if (F != null) {
            return handValConverter(F);
        }
        String C = isFlush(playerCards, tableCards);
        // Flush kontrolü
        if (C != null) {

            return handValConverter(C);
        }
        String S = isStraight(playerCards, tableCards);
        // Straight kontrolü
        if (S != null) {

            return handValConverter(S);
        }
        String T = isThreeOfAKind(playerCards, tableCards);
        // Three of a Kind kontrolü
        if (T != null) {
            return handValConverter(T);
        }
        String B = isTwoPairs(playerCards, tableCards);
        // Two Pairs kontrolü
        if (B != null) {
            return handValConverter(B);
        }
        String P = isOnePair(playerCards, tableCards);
        // One Pair kontrolü
        // Yüksek Kart kontrolü (En sona koyulmalı)
        return handValConverter(Objects.requireNonNullElse(P, "MM"));

    }

    // Royal Flush kontrolü
    private String isRoyalFlush(String[][] cards, String[][] table) {
        if (isStraightFlush(cards, table) != null && (cards[0][0].equals("10") || cards[1][0].equals("10"))) {
            return "U";
        }
        return null;
    }

    // Straight Flush kontrolü
    private String isStraightFlush(String[][] cards, String[][] table) {
        if (isFlush(cards, table) != null && isStraight(cards, table) != null) {
            return "R" + findGreatestCard();
        }
        return null;
    }

    // Four of a Kind kontrolü
    private String isFourOfAKind(String[][] cards, String[][] table) {
        int count;
        int sec = 0;

        for (int i = 0; i < 2; i++) {
            if (Objects.equals(cards[0][0], cards[1][0])) {
                count = 2;
            } else {
                count = 0;
            }
            for (int j = 0; j < 5; j++) {
                if (cards[i][0].equals(table[j][0])) {
                    count++;
                }
                // Eğer count 3'e ulaştıysa, üçlü bir el bulundu demektir
                if (count == 4) {
                    if (i == 1) {
                        return "H" + cards[i][0] + cards[sec][0];
                    } else {
                        return "H" + cards[i][0] + cards[sec + 1][0];
                    }
                }
            }
        }
        return null;
    }

    // Full House kontrolü
    private String isFullHouse(String[][] cards) {
        if ((cards[0][0].equals(cards[1][0]) && cards[2][0].equals(cards[3][0]) && cards[3][0].equals(cards[4][0])) || (cards[0][0].equals(cards[1][0]) && cards[1][0].equals(cards[2][0]) && cards[3][0].equals(cards[4][0]))) {
            return "F" + findGreatestCard();
        }
        return null;
    }

    // Flush kontrolü:renk
    private String isFlush(String[][] cards, String[][] table) {
        ArrayList<String> straight = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            straight.add(cards[i][1]);
        }
        for (int j = 0; j < 5; j++) {
            straight.add(table[j][1]);
        }

        String duplicate = findDuplicate(straight);

        if (duplicate != null) {
            int have = 0;
            if (Objects.equals(cards[0][1], duplicate)) {
                have++;
            }
            if (Objects.equals(cards[1][1], duplicate)) {
                have++;
            }
            if (have > 0) {
                return "C" + findGreatestCard();
            }
        }
        return null;
    }


    // Straight kontrolü
    private String isStraight(String[][] cards, String[][] table) {
        ArrayList<Integer> straight = new ArrayList<>();

        // Tüm kartları tek bir listeye ekle, tekrar edenleri ekleme
        HashSet<Integer> uniqueValues = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            int value = cardValue(cards[i][0]);
            if (!uniqueValues.contains(value)) {
                uniqueValues.add(value);
                straight.add(value);
            }
        }
        for (int j = 0; j < 5; j++) {
            int value = cardValue(table[j][0]);
            if (!uniqueValues.contains(value)) {
                uniqueValues.add(value);
                straight.add(value);
            }
        }

        // Listeyi sırala
        Collections.sort(straight);

        // Ardışık beş kart kontrolü
        for (int i = 0; i < straight.size() - 4; i++) {
            if (straight.get(i) + 4 == straight.get(i + 4)) {
                // En yüksek kartı belirle
                int highestCardValue = straight.get(i + 4);
                return "S" + highestCardValue;
            }
        }
        return null;
    }


    // Three of a Kind kontrolü
    private String isThreeOfAKind(String[][] cards, String[][] table) {
        int count;
        int sec = 0;

        for (int i = 0; i < 2; i++) {
            if (Objects.equals(cards[0][0], cards[1][0])) {
                count = 2;
            } else {
                count = 0;
            }
            for (int j = 0; j < 5; j++) {
                if (cards[i][0].equals(table[j][0])) {
                    count++;
                }
                // Eğer count 3'e ulaştıysa, üçlü bir el bulundu demektir
                if (count == 3) {
                    if (i == 1) {
                        return "T" + cards[i][0] + cards[sec][0];
                    } else {
                        return "T" + cards[i][0] + cards[sec + 1][0];
                    }
                }
            }
        }
        return null;
    }


    // Two Pairs kontrolü
    private String isTwoPairs(String[][] cards, String[][] table) {
        int pairCount = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                if (cards[i][0].equals(table[j][0])) {
                    pairCount++;
                    if (pairCount == 2) return "B" + findGreatestCard() + cards[1][0];
                    break; // İkinci çifti bulduktan sonra döngüyü durdur
                }
            }
        }
        return null;
    }


    // One Pair kontrolü
    private String isOnePair(String[][] cards, String[][] table) {
        int sec = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                if (cards[j][0].equals(table[i][0])) {
                    if (j == 0) sec = 1;
                    return "P" + cards[j][0] + cards[sec][0];
                }
            }
        }
        return null;
    }

    private String findGreatestCard() {
        String[][] playerCards = determinedCard();
        int value1 = cardValue(playerCards[0][0]);
        int value2 = cardValue(playerCards[1][0]);

        // En yüksek değeri olan kartı döndür
        return (value1 > value2) ? playerCards[0][0] : playerCards[1][0];
    }

    private String findLowestCard() {
        String[][] playerCards = determinedCard();
        int value1 = cardValue(playerCards[0][0]);
        int value2 = cardValue(playerCards[1][0]);

        // En düşük değeri olan kartı döndür
        return (value1 < value2) ? playerCards[0][0] : playerCards[1][0];
    }


    private double handValConverter(String handcode) {
        return switch (handcode.charAt(0)) {
            case 'U' -> 13;
            case 'R' -> 12 + cardValue(String.valueOf(handcode.charAt(1))) * 0.001;
            case 'H' -> 11;
            case 'F' -> 10 + cardValue(String.valueOf(handcode.charAt(1))) * 0.001;
            case 'C' -> 9 + cardValue(String.valueOf(handcode.charAt(1))) * 0.001;
            case 'S' -> 8 + cardValue(String.valueOf(handcode.charAt(1))) * 0.001;
            case 'T' ->
                    7 + cardValue(String.valueOf(handcode.charAt(1))) * 0.01 + cardValue(String.valueOf(handcode.charAt(2))) * 0.001;
            case 'B' ->
                    6 + cardValue(String.valueOf(handcode.charAt(1))) * 0.01 + cardValue(String.valueOf(handcode.charAt(2))) * 0.001;
            case 'P' ->
                    5 + cardValue(String.valueOf(handcode.charAt(1))) * 0.01 + cardValue(String.valueOf(handcode.charAt(2))) * 0.001;
            default -> 1 + cardValue(findGreatestCard()) * 0.01 + cardValue(findLowestCard()) * 0.001;
        };
    }

    // Kartın değerini döndüren yardımcı bir metot
    public int cardValue(String card) {
        return switch (card.charAt(0)) {
            case '1' -> 10;
            case 'J' -> 11;
            case 'Q' -> 12;
            case 'K' -> 13;
            case 'A' -> 14;
            default -> Character.getNumericValue(card.charAt(0));
        };
    }

    private String findDuplicate(ArrayList<String> list) {
        Map<String, Integer> map = new HashMap<>();

        for (String element : list) {
            if (map.containsKey(element)) {
                int count = map.get(element) + 1;
                if (count == 5) {
                    return element;
                } else {
                    map.put(element, count);
                }
            } else {
                map.put(element, 1);
            }
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Players{name='" + name + "', hand=[");
        for (String card : hand) {
            result.append(card).append(", ");
        }
        if (!hand.isEmpty()) {
            result.setLength(result.length() - 2); // Son virgülü ve boşluk karakterini sil
        }
        result.append("]}");
        return result.toString();
    }
}
