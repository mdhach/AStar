import java.util.*;

/**
 * Used to override the comparator for PriorityQueue
 */
public class NodeComparator implements Comparator<Node> {
    @Override
    
    /**
     * Compares the F values of two nodes
     * 
     * @param Node the first node to be compared
     * @param Node the second node to be compared
     * @return -1, 0, or 1 based on result
     */
    public int compare(Node in1, Node in2) {
        if(in1.getF() < in2.getF()) {
            return 1;
        } else if(in1.getF() > in2.getF()) {
            return -1;
        } else {
            return 0;
        }
    }
}