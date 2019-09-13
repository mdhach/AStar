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
    private PriorityQueue<Node> openList;
    private Node start;
    private Node goal;
    private Comparator<Node> comparator;
    
    /**
     * Init constructor for Node board and neighbor queue
     */
    public AStar(int sR, int sC, int gR, int gC) {
        comparator = new NodeComparator();
        start = new Node(sR, sC, 2);
        goal = new Node(gR, gC, 3);
        board = new Node[15][15];
        openList = new PriorityQueue<Node>(11, comparator);
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
                } else if (this.getNode(i, j).getType() == 2) {
                    System.out.print("[S]");
                } else if (this.getNode(i, j).getType() == 3) {
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
     * @param i row of Node board
     * @param j col of Node board
     * @return Node at specified index
     */
    public Node getNode(int i, int j) {
        return board[i][j];
    }
    
    /**
     * Searches the row and col of this node
     * 
     * @param in the node that is being searched
     */
    public void search(Node in) {
        openList.add(in);
        this.searchCol(in);
        this.searchRow(in);
    }
    
    /**
     * Search for traversable nodes on parent row
     * 
     * @param in the node that is being searched
     */
    public void searchRow(Node in) {
        if(board[in.getRow()-1][in.getCol()].getType() != 1) {
            board[in.getRow()-1][in.getCol()].setParent(in);
            openList.add(board[in.getRow()-1][in.getCol()]);
        }
        if(board[in.getRow()+1][in.getCol()].getType() != 1) {
            board[in.getRow()+1][in.getCol()].setParent(in);
            openList.add(board[in.getRow()+1][in.getCol()]);
        }
    }
    
    /**
     * Search for traversable nodes on parent column
     * 
     * @param in the node that is being searched
     */
    public void searchCol(Node in) {
        if(board[in.getRow()][in.getCol()-1].getType() != 1) {
            board[in.getRow()][in.getCol()-1].setParent(in);
            openList.add(board[in.getRow()][in.getCol()-1]);
        }
        if(board[in.getRow()][in.getCol()+1].getType() != 1) {
            board[in.getRow()][in.getCol()+1].setParent(in);
            openList.add(board[in.getRow()][in.getCol()+1]);
        }
    }
}