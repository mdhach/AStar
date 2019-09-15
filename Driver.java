import java.util.Scanner;

/**
 * Driver class to test the AStar class
 */
public class Driver {
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int startRow, startCol, goalRow, goalCol;
        
        System.out.println("Enter the row for your starting node: ");
        startRow = input.nextInt();
        System.out.println("Enter the col for your starting node: ");
        startCol = input.nextInt();
        System.out.println("Enter the row for your goal node: ");
        goalRow = input.nextInt();
        System.out.println("Enter the col for your goal node: ");
        goalCol = input.nextInt();
        
        AStar game = new AStar(startRow, startCol, goalRow, goalCol);
        game.generateBoard();
        game.generateBlock();
        game.printState();
        game.search();
        //game.queueToString();
        System.out.println("\nSolution: ");
        game.printState();
    }
}