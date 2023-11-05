import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Tree class to store the nodes.
 * Has a reference to the root node.
 * @see Node
 */
public class Tree {
    public Node root;
    public PrintWriter writer;

    public Tree(Node root, PrintWriter writer) {
        this.root = root;
        this.writer = writer;
    }

    /**
     * Insert a new node in the tree.
     * Public method to call the private insert method.
     * @param name
     * @param value
     * @return the new node
     */
    public Node insert(String name, double value) {
        return insert(root, name, value);
    }

    /**
     * Insert a new node in the tree.
     * Used recursion to find the right place for the new node.
     * After the insertion, the tree is balanced.
     * @param node the current node in the recursion (not the new node)
     * @param name
     * @param value
     * @return the new node
     */
    private Node insert(Node node, String name, double value) {
        if (node == null) {
            return new Node(name, value);
        }

        writer.println(node.name + " welcomed " + name);

        if (value < node.value) {
            node.left = insert(node.left, name, value);
        } else if (value > node.value) {
            node.right = insert(node.right, name, value);
        }

        return balance(node);
    }

    /**
     * Find the node with the minimum value in the subtree.
     * @param node current node in the recursion
     * @return the node with the minimum value
     */
    private Node findMin(Node node) {
        if (node.left == null) {
            return node;
        }

        return findMin(node.left);
    }

    /**
     * Remove a node from the tree.
     * Public method to call the private remove method.
     * @param value
     * @return the removed node
     */
    public Node remove(double value) {
        return remove(root, value, false);
    }

    /**
     * Remove a node from the tree.
     * Used recursion to find the node to be removed.
     * If the node has no children, remove it.
     * If the node has 1 child, replace it with its child.
     * If the node has 2 children, replace it with the node with the minimum value in the right subtree.
     * After the removal, the tree is balanced.
     * @param node
     * @param value
     * @param removed
     * @return
     */
    private Node remove(Node node, double value, boolean removed) {
        if (node == null) {
            return null;
        }

        if (value < node.value) {
            node.left = remove(node.left, value, removed);
        } else if (value > node.value) {
            node.right = remove(node.right, value, removed);
        } else {
            if (node.left == null && node.right == null) {
                if (!removed) writer.println(node.name + " left the family, replaced by nobody");
                return null;
            } else if (node.left == null) {
                if (!removed) writer.println(node.name + " left the family, replaced by " + node.right.name);
                return node.right;
            } else if (node.right == null) {
                if (!removed) writer.println(node.name + " left the family, replaced by " + node.left.name);
                return node.left;
            }

            Node min = findMin(node.right);
            writer.println(node.name + " left the family, replaced by " + min.name);
            node.name = min.name;
            node.value = min.value;
            node.right = remove(node.right, min.value, true);
        }

        return balance(node);
    }

    /**
     * Rotate the tree to the left.
     * Used when the tree is unbalanced to the right.
     * @param node
     * @return the new root of the subtree
     */
    private Node leftRotate(Node node) {
        Node right = node.right;
        node.right = right.left;
        right.left = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        right.height = Math.max(height(right.left), height(right.right)) + 1;
        if(node == root) root = right;
        return right;
    }

    /**
     * Rotate the tree to the right.
     * Used when the tree is unbalanced to the left.
     * @param node
     * @return the new root of the subtree
     */
    private Node rightRotate(Node node) {
        Node left = node.left;
        node.left = left.right;
        left.right = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        left.height = Math.max(height(left.left), height(left.right)) + 1;
        if(node == root) root = left;
        return left;
    }

    /**
     * Rotate the tree to the left and then to the right.
     * Used when the tree is unbalanced to the left-right.
     * @param node
     * @return the new root of the subtree
     */
    private Node leftRightRotate(Node node) {
        node.left = leftRotate(node.left);
        return rightRotate(node);
    }

    /**
     * Rotate the tree to the right and then to the left.
     * Used when the tree is unbalanced to the right-left.
     * @param node
     * @return the new root of the subtree
     */
    private Node rightLeftRotate(Node node) {
        node.right = rightRotate(node.right);
        return leftRotate(node);
    }

    /**
     * Get the height of the node.
     * Height of a leaf is 0.
     * @param node
     * @return the height of the node
     */
    private int height(Node node) {
        if (node == null) return -1;
        return node.height;
    }

    /**
     * Balance the tree.
     * Used after insertion and removal.
     * If the height difference between the left and right subtree is greater than 1, the tree is unbalanced.
     * If the tree is unbalanced to the left, rotate it to the right.
     * If the tree is unbalanced to the right, rotate it to the left.
     * @param node
     * @return the new root of the subtree
     */
    private Node balance(Node node){
        if (node == null) return null;

        if (height(node.left) - height(node.right) > 1) {
            if (height(node.left.left) >= height(node.left.right)) {
                node = rightRotate(node);
            } else {
                node = leftRightRotate(node);
            }
        } else if (height(node.right) - height(node.left) > 1) {
            if (height(node.right.right) >= height(node.right.left)) {
                node = leftRotate(node);
            } else {
                node = rightLeftRotate(node);
            }
        }

        node.height = Math.max(height(node.left), height(node.right)) + 1;
        return node;
    }

    /**
     * Find the lowest common ancestor of two nodes.
     * Public method to call the private lowestCommonAncestor method.
     * @param value1
     * @param value2
     * @return the lowest common ancestor
     */
    public Node lowestCommonAncestor(double value1, double value2) {
        return lowestCommonAncestor(root, value1, value2);
    }

    /**
     * Find the lowest common ancestor of two nodes.
     * Used Depth First Search with recursion.
     * If the current node is null or one of the two nodes, return the current node.
     * If the current node is the lowest common ancestor, return the current node.
     * If the current node is not the lowest common ancestor, return the node that is not null.
     * @param node current node in the recursion
     * @param value1
     * @param value2
     * @return the lowest common ancestor
     */
    private Node lowestCommonAncestor(Node node, double value1, double value2) {
        if (node == null || node.value == value1 || node.value == value2) {
            return node;
        }

        Node left = lowestCommonAncestor(node.left, value1, value2);
        Node right = lowestCommonAncestor(node.right, value1, value2);

        if (left != null && right != null) {
            return node;
        }

        return (left != null) ? left : right;
    }

    /**
     * Find the nodes with the same rank as the given value.
     * Used Breadth First Search with a queue.
     * Add the nodes to a list for each level.
     * If the value is found, print the nodes from the last level.
     * @param value
     */
    public void nodesWithSameRank(double value) {
        LinkedList<Node> queue = new LinkedList<Node>();
        ArrayList<Node> nodes = new ArrayList<Node>();

        queue.offer(root);
        boolean found = false;
        while(!queue.isEmpty()) {
            nodes.clear();
            int size = queue.size();
            for(int i = 0; i < size; i++) {
                Node current = queue.poll();
                nodes.add(current);
                if(current.value == value) found = true;
                if(current.left != null) queue.offer(current.left);
                if(current.right != null) queue.offer(current.right);
            }
            if(found) break;
        }

        writer.print("Rank Analysis Result:");
        for(Node n : nodes) {
            writer.print(" " + n.name + " " + String.format(Locale.US, "%.3f", n.value));
        }
        writer.println();
    }

    /**
     * Find the maximum number of independent nodes.
     * Public method to call the private maxIndependentNodes method.
     * @return
     */
    public int maxIndependentNodes() {
        HashMap<Double, Integer> map = new HashMap<Double, Integer>();
        return maxIndependentNodes(root, map);
    }

    /**
     * Find the maximum number of independent nodes.
     * Used recursion with memoization.
     * If the current node is null, return 0.
     * If the current node is in the map, return the value from the map.
     * If the current node is the root of the subtree, add 1 to the maximum number of independent nodes and add the maximum number of independent nodes from the grandchildren.
     * If the current node is not the root of the subtree, add the maximum number of independent nodes from the left and right subtrees.
     * @param node current node in the recursion
     * @param map map with the node values as keys and the maximum number of independent nodes as values
     * @return the maximum number of independent nodes
     */
    private int maxIndependentNodes(Node node, HashMap<Double, Integer> map) {
        if (node == null) return 0;
        if (map.containsKey(node.value)) return map.get(node.value);

        int maxWithRoot = 1;
        if (node.left != null) {
            maxWithRoot += maxIndependentNodes(node.left.left, map) + maxIndependentNodes(node.left.right, map);
        }
        if (node.right != null) {
            maxWithRoot += maxIndependentNodes(node.right.left, map) + maxIndependentNodes(node.right.right, map);
        }

        int maxWithoutRoot = maxIndependentNodes(node.left, map) + maxIndependentNodes(node.right, map);

        map.put(node.value, Math.max(maxWithRoot, maxWithoutRoot));
        return Math.max(maxWithRoot, maxWithoutRoot);
    }
}
    
