import java.util.*;

public class Bet {
    private final ArrayList<Player> players;
    private final Table table;


    private int bet = 0;
    private final Scanner input = new Scanner(System.in);

    public Bet(Table table, ArrayList<Player> players) {

        this.table = table;
        this.players = players;

    }

    public void firstBet() {
        if (onGame() > 1) {
            for (Player player : players) {
                if (player.isIngame() && player.getBet() < table.getMinBet()) {
                    System.out.println("enter bet for " + player.getName() + ": ");
                    int bet = input.nextInt();
                    if (bet == 0) {
                        player.setIngame(false);
                    } else if (bet >= table.getMinBet() && bet <= player.getMoney()) {
                        player.setBet(bet);
                        table.setMinBet(bet);
                    } else if (bet == 1) {
                        System.out.println("Check");
                        player.setBet(table.getMinBet());

                    } else {
                        System.out.println("The min bet is:" + table.getMinBet() + "/ not enough money!");
                        player.setIngame(false);
                    }
                }

            }
            if (isCompleted(table.getMinBet())) {
                firstBet();
            } else {
                for (Player player : players) {
                    if (player.isIngame()) {
                        this.bet += player.getBet();
                        player.updateMoney(-player.getBet());
                        player.setBet(0);
                    } else {
                        player.setBet(0);
                    }
                }
            }

        }
    }

    public void inGameBet() {
        int currentBet = 0;
        if (onGame() > 1) {
            for (Player player : players) {
                if (player.isIngame() && !player.isAllin()) {
                    System.out.println("enter bet for " + player.getName() + ": ");
                    int bet = input.nextInt();
                    if (bet == 1) {
                        System.out.println("Check");
                        if (player.getMoney() < currentBet) {
                            this.bet -= currentBet - player.getMoney();
                        }
                        player.setBet(currentBet);
                    } else if (bet >= currentBet) {
                        if (!(bet > player.getMoney())) {
                            currentBet = bet;
                            player.setBet(bet);
                        } else {
                            System.out.println("not enough money");
                            player.setIngame(false);
                        }
                    } else {
                        System.out.println("The min bet is:" + currentBet);
                        player.setIngame(false);
                    }
                }
            }
            while (isCompleted(currentBet)) {
                inGameBet();
            }
            for (Player player : players) {
                this.bet += player.getBet();
                player.updateMoney(-player.getBet());
                player.setBet(0);
            }
        }
    }

    public void endGame() {
        if (onGame() > 1) {
            double handvalue = 0;

            // En yüksek el değerini bul
            for (Player player : players) {
                if (player.isIngame()) {
                    player.setHandAmount(player.calculateHand(table));
                    if (player.getHandAmount() > handvalue) {
                        handvalue = player.getHandAmount();
                    }
                }
            }

            // Kazananları bul
            List<Player> winners = new ArrayList<>();
            for (Player player : players) {
                if (player.isIngame() && player.getHandAmount() == handvalue) {
                    winners.add(player);
                    System.out.println("winner is :" + player.getName());
                }
            }

            // Kazananlara ödül dağıt
            int prize = this.bet / winners.size();
            for (Player winner : winners) {
                winner.updateMoney(prize);

            }

            // Oyuncuların durumunu temizle
            for (Player player : players) {
                player.setIngame(true);
                if (player.isIngame()) {
                    player.setBet(0);
                }
            }


            // Masayı ve bahsi sıfırla
        } else {
            Optional<Player> winner = players.stream()
                    .filter(Player::isIngame)
                    .min(Comparator.comparing(Player::getName));
            winner.ifPresent(value -> value.updateMoney(bet));

            // Oyuncuların durumunu temizle
            for (Player player : players) {
                player.setIngame(true);
                player.setBet(0);
            }

            // Masayı ve bahsi sıfırla
        }
        table.setMinBet(10);
        table.setCountGame(1);
        bet = 0;
    }


    private boolean isCompleted(int min) {
        int equals = 0;
        for (Player player : players) {
            if (player.isIngame() && (player.getBet() == min)) {
                equals++;
            }
        }
        return equals != onGame();
    }


    public int onGame() {
        int onGameCount = 0;
        for (Player player : players) {
            if (player.isIngame())
                onGameCount++;
        }
        return onGameCount;
    }

    public int getBet() {
        return bet;
    }
}
