public class App {
    public static void main(String[] args) throws Exception {
        Tree tree = new Tree(new Node("A", 5.0));
        tree.insert("B", 2.0);
        tree.insert("F", 6.0);
        tree.insert("D", 4.0);
        tree.insert("E", 5.0);
        tree.insert("C", 3.0);
        tree.insert("G", 7.0);
        tree.print();
    }
}
