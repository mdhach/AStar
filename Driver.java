public class Driver {
    /**
     * Main driver to execute the AStar class
     */
    public static void main(String[] args) {
        AStar game = new AStar();
        game.generateBoard();
        game.generateBlock();
        game.printState();
    }
}