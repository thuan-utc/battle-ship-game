public class Main {
    public static void main(String[] args) throws Exception {
        Player player1 = new Player("player1");
        player1.printMyBoard();
        player1.printOpponentBoard();
        Player player2 = new Player("player2");
        AttackResult attackResult = player2.receiveAttack("A1");
        player1.updateOpponentBoard(attackResult);
        player1.printOpponentBoard();
    }
}