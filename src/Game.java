import java.util.Scanner;

public class Game {
    private Player player1;
    private Player player2;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public boolean isGameEnd() {
        return player1.isLooser() || player2.isLooser();
    }

    public void start() throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean player1Turn = true;
        int numberTurn = 1;
        while (!isGameEnd()) {
            if (player1Turn) {
                System.out.println("Turn: " + numberTurn);
                player1.printOpponentBoardWithProbabilityContainShip();
                AttackResult result = player2.receiveAttack(Bot.findNextAttack(player1.getOpponentBoard()));
                player1.updateOpponentBoard(result);
                Bot.updateBoardProbability(player1.getOpponentBoard(), result);
            } else {
//                player2.printOpponentBoardWithProbabilityContainShip();
                AttackResult result = player1.receiveAttack(Bot.findNextAttack(player2.getOpponentBoard()));
                player2.updateOpponentBoard(result);
            }
            player1Turn = !player1Turn;
        }
    }
}
