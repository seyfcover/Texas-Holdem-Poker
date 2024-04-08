import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Poker {
    public static void main(String[] args) {
        Game game1 = new Game();
        Table table1 = new Table(10, 0);
        Scanner input = new Scanner(System.in);
        System.out.println("how many players to play?");
        int answer = input.nextInt();
        ArrayList<Player> players = new ArrayList<>();
        Collections.shuffle(game1.names);

        for (int i = 0; i < answer; i++) {
            if (i == 0) {
                System.out.println("Enter your name :");
                String name = input.next();
                players.add(new Player(1000, name));
                i++;
            }
            players.add(new Player(1000, game1.getNames().get(i)));

        }
        Bet bet1 = new Bet(table1, players);
        pokerGame(game1, players, table1, bet1);
    }

    public static void pokerGame(Game game, ArrayList<Player> players, Table table, Bet bet1) {
        Scanner input = new Scanner(System.in);
        boolean continueGame = true;

        while (continueGame) {
            // Yeni bir oyun turunu başlat
            game.shuffleCard();
            checkPlayers(players);
            if (players.size() > 1) {
                //System.out.println(game.getCard());
                startNewRound(players, game, table);
                inRound(game, players, table, bet1);
                bet1.endGame();
                bet1 = new Bet(table, players);
                playersInfo(players);
                // Yeni bir oyun turu başlatmak için kullanıcı girişini al
                System.out.println("Do you want to continue playing? (yes/no)");
                String answer = input.next();
                if (!answer.equalsIgnoreCase("yes")) {
                    continueGame = false;
                }
            }
            else{
                System.out.println(players.get(0).getName() +"after " + table.getCountGame()+"  hands beaten all of them!");
                System.exit(1);
            }
        }
    }

    public static void checkPlayers(ArrayList<Player> players) {
        players.removeIf(player -> player.getMoney() <= 0);
    }

    private static void startNewRound(ArrayList<Player> players, Game game, Table table) {
        // Oyuncuların elindeki kartları temizle ve yeni kartlar dağıt
        for (Player player : players) {
            player.clear();
        }
        for (Player player : players) {
            player.addHand(game.handOut());
        }
        for (Player player : players) {
            player.addHand(game.handOut());
        }
        table.clear();
        // Masaya 3 kart aç
        for (int i = 0; i < 3; i++) {

            table.openHand(game.handOut());
        }
    }

    public static void inRound(Game game, ArrayList<Player> players, Table table, Bet bet1) {
        System.out.println(players.get(0).getHand());
        bet1.firstBet();
        displayTable(players, table, bet1);
        if (bet1.onGame() > 1) {
            System.out.println("Sum of bets on table :" + bet1.getBet());
        }
        for (int i = 0; i < 3; i++) {
            bet1.inGameBet();
            if (i < 2) { // masaya sadece 2 kart
                table.openHand(game.handOut());
            }
            displayTable(players, table, bet1);
            if (bet1.onGame() > 1) {
                System.out.println("Sum of bets on table :" + bet1.getBet());
            }
        }
    }

    public static void displayTable(ArrayList<Player> players, Table table, Bet bet) {
        if (bet.onGame() > 1) { // oyunda aktif 2 oyuncu varsa
            for (Player player : players) {
                if (player.isIngame()) {
                    System.out.println(player.getName() + " - " + player.getHand());
                }
            }
            System.out.println(table);
        }
    }

    public static void playersInfo(ArrayList<Player> players) {
        for (Player pl1 : players) {
            System.out.println(pl1.getName() + "'s money:" + pl1.getMoney() +" & handvalue's "+ pl1.getHandAmount());
        }
    }
}
