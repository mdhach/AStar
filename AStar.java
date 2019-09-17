import java.util.*;

/**
 * This class generates a 15x15 double array full of traversable and blocked nodes.
 * It uses the A* algorithm to find a path between the user specified starting
 * and goal node.
 * 
 * @author Mardi
 * @version 9/17/2019
 */
public class AStar {
    final int move = 10;
    final int moveDia = 14;
    private Node[][] board;
    private PriorityQueue<Node> openList;
    private Stack<Node> closedList;
    private Node start;
    private Node goal;
    private Comparator<Node> comparator;
    private boolean victory;
    private Scanner input;
    
    /**
     * Init constructor
     * 
     */
    public AStar() {
    	input = new Scanner(System.in);
    }
    
    /**
     * Overload constructor
     * 
     * @param sR start node row
     * @param sC start node col
     * @param gR goal node row
     * @param gC goal node col
     */
    public AStar(int sR, int sC, int gR, int gC) {
        comparator = new NodeComparator();
        start = new Node(sR, sC, 2);
        goal = new Node(gR, gC, 3);
        board = new Node[15][15];
        openList = new PriorityQueue<Node>(11, comparator);
        closedList = new Stack<Node>();
        victory = false;
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
        start.setH(calculateH(start, 0)); // set start h value
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

        // start node(0, 0) and goal node(14, 14); fail check
        /*
        board[13][14].setType(1); blocks the goal node at(14, 14)
        board[14][13].setType(1);
        board[13][13].setType(1);
        */
    }
    
    /**
     * Prints a string representation of the board
     */
    public void printState() {
        String p = "\u2022";
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                if(this.getNode(i, j).getType() == 1) {
                    System.out.print("[%]");
                } else if(this.getNode(i, j).getType() == 2) {
                    System.out.print("[S]");
                } else if(this.getNode(i, j).getType() == 3) {
                    System.out.print("[G]");
                } else if(this.getNode(i, j).getType() == 4) {
                    System.out.print("[" + p + "]");
                } else {
                	System.out.print("[ ]");
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Searches the row and col of this node
     * 
     */
    public void search() {
        // pop off top of queue (minheap) and search
        Node temp;
        if(openList.peek() != null) {
            temp = openList.poll();
            if(temp.getParent() != null && temp.getParent().getF() >= temp.getF()) {
            	closedList.push(temp);
            }
        } else {
            return;
        }
        if(temp.getType() != 2 && temp.getType() != 3) {
            temp.setType(5);
        }
        //System.out.println("POPPED: " + temp.toString()); // debug msg
        // determine if current node is the goal
        if(!this.checkGoal(temp)) {
            this.searchCol(temp);
            this.searchRow(temp);
            this.searchDia(temp);
            this.search();
        } else {
            victory = true;
            this.backtrack(temp);
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
        if(this.checkBounds(in.getRow()-1, in.getCol())) {
            temp = board[in.getRow()-1][in.getCol()];
            if(temp.getType() != 1 && temp.getType() != 5) {
                temp.setParent(in);
                
                // calculate g if traversable node was found
                g = this.calculateG(temp, 0);
                temp.setG(g);
                
                // calculate h if traversable node was found
                h = this.calculateH(temp, 0);
                temp.setH(h);
                
                // set f after g and h were calculated
                temp.setF();
                
                // add to queue
                openList.add(temp);
            }
        }
        if(this.checkBounds(in.getRow()+1, in.getCol())) {
            temp = board[in.getRow()+1][in.getCol()];
            if(temp.getType() != 1 && temp.getType() != 5) {
                temp.setParent(in);
                g = calculateG(temp, 0);
                temp.setG(g);
                h = calculateH(temp, 0);
                temp.setH(h);
                temp.setF();
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
        if(this.checkBounds(in.getRow(), in.getCol()-1)) {
            temp = board[in.getRow()][in.getCol()-1];
            if(temp.getType() != 1 && temp.getType() != 5) {
                temp.setParent(in);
                g = this.calculateG(temp, 0);
                temp.setG(g);
                h = this.calculateH(temp, 0);
                temp.setH(h);
                temp.setF();
                
                // add to queue
                openList.add(temp);
            }
        }
        if(this.checkBounds(in.getRow(), in.getCol()+1)) {
            temp = board[in.getRow()][in.getCol()+1];
            if(temp.getType() != 1 && temp.getType() != 5) {
                temp.setParent(in);
                g = this.calculateG(temp, 0);
                temp.setG(g);
                h = this.calculateH(temp, 0);
                temp.setH(h);
                temp.setF();
                openList.add(temp);
            }
        }
    }
    
    /**
     * Used to search diagonal nodes
     * 
     * @param in
     */
    public void searchDia(Node in) {
        Node temp;
        int g;
        int h;
        // top left
        if(this.checkBounds(in.getRow()-1, in.getCol()-1)) {
            temp = board[in.getRow()-1][in.getCol()-1];
            if(temp.getType() != 1 && temp.getType() != 5) {
                temp.setParent(in);
                g = this.calculateG(temp, 1);
                temp.setG(g);
                h = this.calculateH(temp, 1);
                temp.setH(h);
                temp.setF();
                openList.add(temp);
            }
        }
        // top right
        if(this.checkBounds(in.getRow()-1, in.getCol()+1)) {
            temp = board[in.getRow()-1][in.getCol()+1];
            if(temp.getType() != 1 && temp.getType() != 5) {
                temp.setParent(in);
                g = this.calculateG(temp, 1);
                temp.setG(g);
                h = this.calculateH(temp, 1);
                temp.setH(h);
                temp.setF();
                openList.add(temp);
            }
        }
        // bottom left
        if(this.checkBounds(in.getRow()+1, in.getCol()-1)) {
            temp = board[in.getRow()+1][in.getCol()-1];
            if(temp.getType() != 1 && temp.getType() != 5) {
                temp.setParent(in);
                g = this.calculateG(temp, 1);
                temp.setG(g);
                h = this.calculateH(temp, 1);
                temp.setH(h);
                temp.setF();
                openList.add(temp);
            }
        }
        // bottom right
        if(this.checkBounds(in.getRow()+1, in.getCol()+1)) {
            temp = board[in.getRow()+1][in.getCol()+1];
            if(temp.getType() != 1 && temp.getType() != 5) {
                temp.setParent(in);
                g = this.calculateG(temp, 1);
                temp.setG(g);
                h = this.calculateH(temp, 1);
                temp.setH(h);
                temp.setF();
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
    public int calculateH(Node in, int type) {
        int x = in.getCol() - goal.getCol();
        x = Math.abs(x); // get absolute value of col distance
    
        int y = in.getRow() - goal.getRow();
        y = Math.abs(y); // get absolute value of row distance
        
        if(type == 0) { // type 0 is 10, type 1 is 14
            return ((x + y) * move); // return the sum of the values * move
        } else {
            return ((x + y) * moveDia);
        }
        
    }
    
    /**
     * Calculates the G value between a specified node and the start node
     * 
     * @param in the node that is being calculated
     * @return int distance (in blocks) between param node and start node
     */
    public int calculateG(Node in, int type) {
        int x = in.getCol() - start.getCol();
        x = Math.abs(x);
        
        int y = in.getRow() - start.getRow();
        y = Math.abs(y);
        
        if(type == 0) {
            return ((x + y) * move);
        } else {
            return ((x + y) * moveDia);
        }
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
    
    /**
     * Used to check if node is in bounds
     * 
     * @param i the row of the node
     * @param j the col of the node
     * @return boolean true if in bounds; false by default
     */
    public boolean checkBounds(int i, int j) {
        if(i < board[1].length && i >= 0 && j < board[0].length && j >= 0) {
            return true;
        }
        return false;
    }
    
    /**
     * Finds the parents of the final node to trace a path
     * 
     * @param in the node being traced
     */
    public void backtrack(Node in) {
        Node temp;
        while(true) {
            if(!closedList.empty()){
                temp = closedList.pop().getParent();
                if(temp.getType() != 2) {
                    temp.setType(4);
                }
                this.backtrack(temp);
            } else {
                return;
            }
        }
    }
    
    /**
     * Returns victory
     * 
     * @return boolean victory
     */
    public boolean getVictory() {
        return victory;
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
     * Method used to determine if user continues or quits
     * 
     * @return char users decision
     */
    public char getChar() {
    	input = new Scanner(System.in);
        char charIn = 0;
        boolean escape = false;
        
        while(!escape) {
            try {
            	System.out.println("\nContinue? Y/N: ");
            	charIn = input.next().charAt(0);
            	charIn = Character.toUpperCase(charIn);
            	if(charIn != 'Y' && charIn != 'N') {
            		throw new Exception("Neither Y or N");
            	} else {
            		escape = true;
            	}
            } catch (Exception c) {
        		System.out.println("ERROR! Enter either Y or N (not case sensitive)");
            }
        }
        return charIn;
    }
    
    /**
     * Initializes the starting text
     * 
     * @return int[] values that are required for a new AStar object
     */
    public int[] init() {
    	int[] arr = new int[4];
    	
    	System.out.println("Enter the row for your starting node: ");
        arr[0] = input.nextInt();
        System.out.println("Enter the col for your starting node: ");
        arr[1] = input.nextInt();
        System.out.println("Enter the row for your goal node: ");
        arr[2] = input.nextInt();
        System.out.println("Enter the col for your goal node: ");
        arr[3] = input.nextInt();
        
        return arr;
    }
}