import java.util.*;

/**
 * This class generates a 15x15 double array full of traversable and blocked nodes.
 * It uses the A* algorithm to find a path between the user specified starting
 * and goal node.
 * 
 * @author Mardi
 * @version 9/11/2019
 */
public class AStar {
    private Node[][] board;
    private PriorityQueue<Node> neighbors;
    private Node start;
    private Node goal;
    
    /**
     * Init constructor for Node board and neighbor queue
     */
    public AStar(int sR, int sC, int gR, int gC) {
        start = new Node(sR, sC, 1);
        goal = new Node(gR, gC, 1);
        board = new Node[15][15];
        neighbors = new PriorityQueue<Node>();
    }
    
    /**
     * Generates a double array of Nodes
     */
    public void generateBoard() {
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                board[i][j] = new Node(i, j, 0);
            }
        }
        board[start.getRow()][start.getCol()] = start;
        board[goal.getRow()][goal.getCol()] = goal;
    }
    
    /**
     * Sets random nodes to be nontraversable
     */
    public void generateBlock() {
        Random rand = new Random();
        int i = rand.nextInt(15);
        int j = rand.nextInt(15);
        int count = 0;
        while(count < 24) {
            if(board[i][j].getType() == 0) {
                board[i][j].setType(1);
                count++;
            }
            i = rand.nextInt(15);
            j = rand.nextInt(15);
        }
    }
    
    /**
     * Prints a string representation of the board
     */
    public void printState() {
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                if(this.getNode(i, j).getType() == 0) {
                    System.out.print("[ ]");
                } else if ((this.getNode(i, j).getRow() == start.getRow()) && (this.getNode(i, j).getCol() == start.getCol())) {
                    System.out.print("[S]");
                } else if ((this.getNode(i, j).getRow() == goal.getRow()) && (this.getNode(i, j).getCol() == goal.getCol())) {
                    System.out.print("[G]");
                } else {
                    System.out.print("[#]");
                }
            }
            System.out.println();
        }
    }

    /**
     * Returns node at index
     * 
     * @param int row of Node board
     * @param int col of Node board
     * @return Node at specified index
     */
    public Node getNode(int i, int j) {
        return board[i][j];
    }
    
    /**
     * User defined starting node
     */
    public void setStart(int i, int j) {
        if((board[i][j].getType()) != 1) {
            start = new Node(i, j, 1);
        } else {
            System.out.println("ERROR! This is not a valid starting Node!");
        }
    }
    
    /**
     * User defined goal node
     */
    public void setGoal(int i, int j) {
        if((board[i][j].getType()) != 1) {
            goal = new Node(i, j, 1);
        } else {
            System.out.println("ERROR! This is not a valid goal Node!");
        }
    }
}