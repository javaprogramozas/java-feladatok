package alapfeladatok;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class A21 {
    public static void main(String[] args) {
        String text = "alma a fa alatt";

        Map<Character, Integer> characterCounts = new TreeMap<>();
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            characterCounts.merge(currentChar, 1, Integer::sum);
        }
        System.out.println(characterCounts);

        PriorityQueue<Node> nodesToProcess = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : characterCounts.entrySet()) {
            double frequency = (double)entry.getValue() / text.length() * 100;
            Node node = new Node(entry.getKey(), frequency, null, null);
            nodesToProcess.add(node);
        }
        System.out.println(nodesToProcess);

        while (nodesToProcess.size() > 1) {
            Node left = nodesToProcess.poll();
            Node right = nodesToProcess.poll();
            Node parent = new Node(null, left.frequency + right.frequency, left, right);
            nodesToProcess.add(parent);
        }
        Node root = nodesToProcess.poll();
        System.out.println(root);
        Map<Character, String> codes = new TreeMap<>();
        convertTreeToCodes(root, "", codes);
        System.out.println(codes);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            sb.append(codes.get(currentChar));
        }
        System.out.println(sb);
    }

    private static void convertTreeToCodes(Node node, String prefix, Map<Character, String> mapping) {
        if (node.isLeaf()) {
            mapping.put(node.value, prefix);
            return;
        }
        if (node.left != null) {
            convertTreeToCodes(node.left, prefix + '0', mapping);
        }
        if (node.right != null) {
            convertTreeToCodes(node.right, prefix + '1', mapping);
        }
    }

    private record Node(Character value, double frequency, Node left, Node right) implements Comparable<Node> {

        @Override
        public int compareTo(Node other) {
            return Double.compare(frequency, other.frequency);
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }
    }
}
