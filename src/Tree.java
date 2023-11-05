public class Tree {
    public Node root;

    public Tree(Node root) {
        this.root = root;
    }

    public void print(){
        print(root);
    }

    private void print(Node node) {
        if (node == null) return;
        print(node.left);
        System.out.println(node.name + " " + node.value);
        print(node.right);
    }

    public Node insert(String name, double value) {
        return insert(root, name, value);
    }

    private Node insert(Node node, String name, double value) {
        if (node == null) {
            return new Node(name, value);
        }

        if (value < node.value) {
            node.left = insert(node.left, name, value);
        } else if (value > node.value) {
            node.right = insert(node.right, name, value);
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
        return remove(root, value);
    }

    private Node remove(Node node, double value) {
        if (node == null) {
            return null;
        }

        if (value < node.value) {
            node.left = remove(node.left, value);
        } else if (value > node.value) {
            node.right = remove(node.right, value);
        } else {
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            Node min = findMin(node.right);
            node.name = min.name;
            node.value = min.value;
            node.right = remove(node.right, min.value);
        }

        return balance(node);
    }

    private Node leftRotate(Node node) {
        Node right = node.right;
        node.right = right.left;
        right.left = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        right.height = Math.max(height(right.left), height(right.right)) + 1;
        return right;
    }

    private Node rightRotate(Node node) {
        Node left = node.left;
        node.left = left.right;
        left.right = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        left.height = Math.max(height(left.left), height(left.right)) + 1;
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
}
