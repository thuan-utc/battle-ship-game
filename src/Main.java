public class Main {
    public static void main(String[] args) throws Exception {
        Player player1 = new Player("Bot1");
        Bot player2 = new Bot("Bot2");
        Game game = new Game(player1, player2);
        game.start();
    }
}