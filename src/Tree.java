import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

public class Tree {
    public Node root;
    public PrintWriter writer;

    public Tree(Node root, PrintWriter writer) {
        this.root = root;
        this.writer = writer;
    }

    public void print(){
        System.out.println("-------------------------");
        print(root);
        System.out.println("-------------------------");
    }

    private void print(Node node) {
        if (node == null) return;
        print(node.left);
        System.out.println(node.name + " " + String.format(Locale.US, "%.3f", node.value));
        print(node.right);
    }

    public Node insert(String name, double value) {
        return insert(root, name, value);
    }

    private Node insert(Node node, String name, double value) {
        if (node == null) {
            return new Node(name, value);
        }

        writer.println(node.name + " welcomed " + name);

        if (value < node.value) {
            node.left = insert(node.left, name, value);
            node.left.parent = node;
        } else if (value > node.value) {
            node.right = insert(node.right, name, value);
            node.right.parent = node;
        }

        return balance(node);
    }

    private Node findMin(Node node) {
        if (node.left == null) {
            return node;
        }

        return findMin(node.left);
    }

    public Node remove(double value) {
        return remove(root, value, false);
    }

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

    private Node leftRotate(Node node) {
        Node right = node.right;
        node.right = right.left;
        right.left = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        right.height = Math.max(height(right.left), height(right.right)) + 1;
        if(node == root) root = right;
        right.parent = node.parent;
        node.parent = right;
        return right;
    }

    private Node rightRotate(Node node) {
        Node left = node.left;
        node.left = left.right;
        left.right = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        left.height = Math.max(height(left.left), height(left.right)) + 1;
        if(node == root) root = left;
        left.parent = node.parent;
        node.parent = left;
        return left;
    }

    private Node leftRightRotate(Node node) {
        node.left = leftRotate(node.left);
        return rightRotate(node);
    }

    private Node rightLeftRotate(Node node) {
        node.right = rightRotate(node.right);
        return leftRotate(node);
    }

    private int height(Node node) {
        if (node == null) return -1;
        return node.height;
    }

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

    public Node lowestCommonAncestor(double value1, double value2) {
        return lowestCommonAncestor(root, value1, value2);
    }

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

    public void nodesWithSameRank(double value) {
        nodesWithSameRank(root, value);
    }

    private void nodesWithSameRank(Node node, double value) {
        LinkedList<Node> queue = new LinkedList<Node>();
        ArrayList<Node> nodes = new ArrayList<Node>();

        queue.offer(node);
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

    public int maxIndependentNodes() {
        return maxIndependentNodes(root);
    }

    private int maxIndependentNodes(Node node) {
        if (node == null) return 0;

        int maxWithRoot = 1;
        if (node.left != null) {
            maxWithRoot += maxIndependentNodes(node.left.left) + maxIndependentNodes(node.left.right);
        }
        if (node.right != null) {
            maxWithRoot += maxIndependentNodes(node.right.left) + maxIndependentNodes(node.right.right);
        }

        int maxWithoutRoot = maxIndependentNodes(node.left) + maxIndependentNodes(node.right);
        
        return Math.max(maxWithRoot, maxWithoutRoot);
    }
}
    
