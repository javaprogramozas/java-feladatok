package alapfeladatok;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import static java.util.stream.Collectors.toCollection;

public class A21 {
    public static void main(String[] args) {
        String text = "alma a fa alatt";

        PriorityQueue<Node> nodesToProcess = createProcessingQueue(text);

        Node root = buildCodingTree(nodesToProcess);
        Map<Character, String> codes = new TreeMap<>();
        convertTreeToCodes(root, "", codes);
        System.out.println(codes);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            sb.append(codes.get(currentChar));
        }
        System.out.println(sb);

        char[] characters = new char[codes.size()];
        int[] encodedCharacters = new int[codes.size()];
        int numberOfBits = sb.length();
        int numberOfBytes = sb.length() / 8 + ((sb.length() % 8 != 0) ? 1 : 0);
        byte[] data = new byte[numberOfBytes];

        int index = 0;
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            characters[index] = entry.getKey();
            encodedCharacters[index] = Integer.parseUnsignedInt(entry.getValue(), 2);
            index++;
        }

        index = 0;
        while (sb.length() > 8) {
            data[index] = (byte)Integer.parseInt(sb.substring(0, 8), 2);
            index++;
            sb.delete(0, 8);
        }
        if (sb.length() > 0) {
            String paddedString = sb + "0".repeat(8 - sb.length());
            data[index] = (byte)Integer.parseInt(paddedString, 2);
        }

        CompressedData compressedData = new CompressedData(characters, encodedCharacters, numberOfBits, data);


        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("test.dat"))) {
            objectOutputStream.writeObject(compressedData);
        } catch (IOException e) {
            System.err.println("Error during writing file: " + e.getMessage());
        }
    }

    private static Node buildCodingTree(PriorityQueue<Node> nodesToProcess) {
        while (nodesToProcess.size() > 1) {
            Node left = nodesToProcess.poll();
            Node right = nodesToProcess.poll();
            Node parent = new Node(null, left.frequency + right.frequency, left, right);
            nodesToProcess.add(parent);
        }
        Node root = nodesToProcess.poll();
        return root;
    }

    private static PriorityQueue<Node> createProcessingQueue(String text) {
        Map<Character, Integer> characterCounts = new TreeMap<>();
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            characterCounts.merge(currentChar, 1, Integer::sum);
        }
        return characterCounts.entrySet().stream()
                .map(entry -> new Node(entry.getKey(), entry.getValue()))
                .collect(toCollection(PriorityQueue::new));
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

    private record Node(Character value, int frequency, Node left, Node right) implements Comparable<Node> {

        public Node(Character value, int frequency) {
            this(value, frequency, null, null);
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(frequency, other.frequency);
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }
    }

    private record CompressedData(char[] characters, int[] encodedCharacters, int numberOfBits, byte[] data) implements Serializable {}
}
