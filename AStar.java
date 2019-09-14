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
    final int move = 10;
    private Node[][] board;
    private PriorityQueue<Node> openList;
    private Node start;
    private Node goal;
    private Comparator<Node> comparator;
    
    /**
     * Init constructor
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
        
        // initialize values for starting node
        start.setG(0); // set start G value to 0
        start.setH(calculateH(start)); // set start h value
        start.setF(); // set start F value
        
        openList.add(start);
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
                } else if(this.getNode(i, j).getType() == 2) {
                    System.out.print("[S]");
                } else if(this.getNode(i, j).getType() == 3) {
                    System.out.print("[G]");
                } else if(this.getNode(i, j).getType() == 5) {
                    System.out.print("[.]");
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
     */
    public void search() {
        // pop off top of queue and search
        Node temp = openList.poll();
        if(temp.getType() != 2 && temp.getType() != 3) {
            temp.setType(5);
        }
        System.out.println("POPPED: " + temp.toString());
        if(!this.checkGoal(temp)) {
            this.searchCol(temp);
            this.searchRow(temp);
            this.search();
        }
    }
    
    /**
     * Search for traversable nodes on parent row
     * 
     * @param in the node that is being searched
     */
    public void searchRow(Node in) {
        Node temp; // point to node for less verbosity
        int g;
        int h;
        if(in.getRow() < board[0].length && in.getRow() >= 0) {
            temp = board[in.getRow()-1][in.getCol()];
            if(temp.getType() != 1 && temp.getType() != 5) {
                temp.setParent(in);
                
                // calculate g if traversable node was found
                g = this.calculateG(temp);
                temp.setG(g);
                
                // calculate h if traversable node was found
                h = this.calculateH(temp);
                temp.setH(h);
                
                // set f after g and h were calculated
                temp.setF();
                
                // add to queue
                openList.add(temp);
            }
        }
        if(in.getRow() < board[0].length && in.getRow() >= 0) {
            temp = board[in.getRow()+1][in.getCol()];
            if(temp.getType() != 1 && temp.getType() != 5) {
                temp.setParent(in);
                
                // calculate g if traversable node was found
                g = calculateG(temp);
                temp.setG(g);
                
                // calculate h if traversable node was found
                h = calculateH(temp);
                temp.setH(h);
                
                // set f after g and h were calculated
                temp.setF();
                
                // add to queue
                openList.add(temp);
            }
        }
    }
    
    /**
     * Search for traversable nodes on parent column
     * 
     * @param in the node that is being searched
     */
    public void searchCol(Node in) {
        Node temp; // point to node for less verbosity
        int g;
        int h;
        if(in.getCol() < board[1].length && in.getCol() >= 0) {
            temp = board[in.getRow()][in.getCol()-1];
            if(temp.getType() != 1 && temp.getType() != 5) {
                temp.setParent(in);
                
                // calculate g if traversable node was found
                g = this.calculateG(temp);
                temp.setG(g);
                
                // calculate h if traversable node was found
                h = this.calculateH(temp);
                temp.setH(h);
                
                // set f after g and h were calculated
                temp.setF();
                
                // add to queue
                openList.add(temp);
            }
        }
        if(in.getCol() < board[1].length && in.getCol() >= 0) {
            temp = board[in.getRow()][in.getCol()+1];
            if(temp.getType() != 1 && temp.getType() != 5) {
                temp.setParent(in);
                
                // calculate g if traversable node was found
                g = this.calculateG(temp);
                temp.setG(g);
                
                // calculate h if traversable node was found
                h = this.calculateH(temp);
                temp.setH(h);
                
                // set f after g and h were calculated
                temp.setF();
                
                // add to queue
                openList.add(temp);
            }
        }
    }
    
    /**
     * Calculates the heuristic between a specified node and the goal node
     * 
     * @param in the node that is being calculated
     * @return int distance (in blocks) between param node and goal node
     */
    public int calculateH(Node in) {
        int x = in.getCol() - goal.getCol();
        x = Math.abs(x); // get absolute value of col distance
    
        int y = in.getRow() - goal.getRow();
        y = Math.abs(y); // get absolute value of row distance
    
        return ((x + y) * move); // return the sum of the values * move
    }
    
    /**
     * Calculates the G value between a specified node and the start node
     * 
     * @param in the node that is being calculated
     * @return int distance (in blocks) between param node and start node
     */
    public int calculateG(Node in) {
        int x = in.getCol() - start.getCol();
        x = Math.abs(x);
        
        int y = in.getRow() - start.getRow();
        y = Math.abs(y);
        
        return ((x + y) * move);
    }
    
    /**
     * Used to check queue contents
     */
    public void queueToString() {
        Node[] arr1 = new Node[openList.size()];
        Node[] arr2 = openList.toArray(arr1);
        System.out.println("Queue size: " + arr2.length);
        System.out.println("Queue contents: ");
        for(int i = 0; i < arr2.length; i++) {
            System.out.println(arr2[i].toString());
        }
    }
    
    /**
     * Determines if the goal node has been found
     * 
     * @param in the node being checked
     * @return boolean returns false on default; true if goal was found
     */
    public boolean checkGoal(Node in) {
        if(in == goal) {
            return true;
        }
        return false;
    }
}