public class Node {
    public String name;
    public double value;
    public int height;
    public Node left;
    public Node right;
    public Node parent;

    public Node(String name, double value) {
        this.name = name;
        this.value = value;
    }
}
