/**
 * Node class for the AVL tree
 * Added name and height properties to the node on top of the classic ones (value, left, right).
 * @see Tree
 */
public class Node {
    public String name;
    public double value;
    /**
     * Height of the node (used for balancing).
     * Leaves have height 0
     * @see Tree#balance(Node)
     */
    public int height;
    public Node left;
    public Node right;

    public Node(String name, double value) {
        this.name = name;
        this.value = value;
    }
}
