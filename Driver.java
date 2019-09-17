/**
 * Driver class to test the AStar class
 */
public class Driver {
    
    public static void main(String[] args) {
    	AStar game = new AStar();
        int[] arr = new int[4];
        char charIn = ' ';
        
        while(charIn != 'N') {
	        arr = game.init();
	        game = new AStar(arr[0], arr[1], arr[2], arr[3]);
	        game.generateBoard();
	        game.generateBlock();
	        System.out.println("Initial State: ");
	        game.printState();
	        game.search();
	        //game.queueToString(); debug msg
	        if(game.getVictory()) {
	            System.out.println("\nSolution: ");
	            game.printState();
	        } else {
	            System.out.println("\nSorry! No path could be determined.");
	            game.printState();
	        }
	        charIn = game.getChar();
        }
        System.out.println("Goodbye!");
    }
}