import java.io.File;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;

/**
 * Main class to run the code.
 */
public class Main {
    /**
     * Main method to run the code.
     * @param args Command line arguments (input file, output file)
     */
    public static void main(String[] args) throws Exception {
        File inputFile = new File(args[0]);
        Scanner scanner = new Scanner(inputFile);
        PrintWriter writer = new PrintWriter(args[1], "UTF-8");
        String[] boss = scanner.nextLine().split(" ");
        Tree tree = new Tree(new Node(boss[0], Double.parseDouble(boss[1])), writer);

        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" ");
            String operation = line[0];

            switch (operation) {
                case "MEMBER_IN":
                    tree.insert(line[1], Double.parseDouble(line[2]));
                    break;
                case "MEMBER_OUT":
                    tree.remove(Double.parseDouble(line[2]));
                    break;
                case "INTEL_TARGET":
                    Node ancestor = tree.lowestCommonAncestor(Double.parseDouble(line[2]), Double.parseDouble(line[4]));
                    writer.println("Target Analysis Result: " + ancestor.name + " " + String.format(Locale.US, "%.3f", ancestor.value));
                    break;
                case "INTEL_DIVIDE":
                    int maxCount = tree.maxIndependentNodes();
                    writer.println("Division Analysis Result: " + maxCount);
                    break;
                case "INTEL_RANK":
                    tree.nodesWithSameRank(Double.parseDouble(line[2]));
                    break;
            }
        }

        scanner.close();
        writer.close();
    }
}
