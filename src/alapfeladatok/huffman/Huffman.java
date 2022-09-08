package alapfeladatok.huffman;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;

public class Huffman {

    public static EncodingResults encode(String text) {
        PriorityQueue<Node> nodesToProcess = createProcessingQueue(text);
        Node root = buildCodingTree(nodesToProcess);
        List<CharacterCode> codes = convertTreeToCodes(root, "");
        List<CharacterCode> canonicalCodes = convertToCanonicalCodes(codes);
        StringBuilder encodedString = encodeTextAsStringOfBits(text, canonicalCodes);
        return encodeTextToBytes(canonicalCodes, encodedString);
    }

    public static String decode(DecodingInputs input) throws IOException {
        List<CharacterCode> codes = reconstructCodeMapping(input.characters(), input.codeLength());
        System.out.println(codes);
        Map<String, Character> reverseMapping = codes.stream().collect(toMap(CharacterCode::code, CharacterCode::character));
        BitInputStream inputStream = new BitInputStream(new ByteArrayInputStream(input.data()));
        String prefix = "";
        StringBuilder sb = new StringBuilder(input.numberOfBits() / 8 + 1);
        for (int i = 0; i < input.numberOfBits(); i++) {
            prefix += inputStream.readBits(1) == 1 ? "1" : "0";
            if (reverseMapping.containsKey(prefix)) {
                Character character = reverseMapping.get(prefix);
                sb.append(character);
                prefix = "";
            }
        }
        return sb.toString();
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

    private static Node buildCodingTree(PriorityQueue<Node> nodesToProcess) {
        while (nodesToProcess.size() > 1) {
            Node left = nodesToProcess.poll();
            Node right = nodesToProcess.poll();
            Node parent = new Node(null, left.frequency + right.frequency, left, right);
            nodesToProcess.add(parent);
        }
        return nodesToProcess.poll();
    }

    private static List<CharacterCode> convertTreeToCodes(Node node, String prefix) {
        if (node.isLeaf()) {
            return List.of(new CharacterCode(node.value, prefix));
        }
        List<CharacterCode> results = new ArrayList<>();
        if (node.left != null) {
            results.addAll(convertTreeToCodes(node.left, prefix + '0'));
        }
        if (node.right != null) {
            results.addAll(convertTreeToCodes(node.right, prefix + '1'));
        }
        return results;
    }

    private static List<CharacterCode> convertToCanonicalCodes(List<CharacterCode> codes) {
        PriorityQueue<CharacterCode> codesToProcess = new PriorityQueue<>(codes);
        List<CharacterCode> canonicalCodes = new ArrayList<>(codes.size());

        CharacterCode first = codesToProcess.poll();
        String canonicalCode = "0".repeat(first.code().length());
        CharacterCode previous = new CharacterCode(first.character(), canonicalCode);
        canonicalCodes.add(previous);

        while (!codesToProcess.isEmpty()) {
            CharacterCode current = codesToProcess.poll();
            int nextCanonicalValue = Integer.parseInt(previous.code(), 2) + 1;
            String nextCanonicalCode = leftPadZeros(Integer.toString(nextCanonicalValue, 2), previous.code().length());
            if (current.code().length() != previous.code().length()) {
                nextCanonicalCode = rightPadZeros(nextCanonicalCode, current.code().length());
            }
            previous = new CharacterCode(current.character(), nextCanonicalCode);
            canonicalCodes.add(previous);
        }

        return canonicalCodes;
    }

    private static List<CharacterCode> reconstructCodeMapping(char[] characters, byte[] codeLength) {
        List<CharacterCode> codes = new ArrayList<>();
        String canonicalCode = "0";
        int charIndex = 0;
        int length = 1;
        for (byte numOfChars : codeLength) {
            for (int i = 0; i < numOfChars; i++) {
                char character = characters[charIndex++];
                codes.add(new CharacterCode(character, canonicalCode));
                int nextCanonicalValue = Integer.parseInt(canonicalCode, 2) + 1;
                canonicalCode = leftPadZeros(Integer.toString(nextCanonicalValue, 2), length);
            }
            canonicalCode = canonicalCode + "0";
            length++;
        }
        return codes;
    }

    private static StringBuilder encodeTextAsStringOfBits(String text, List<CharacterCode> codes) {
        Map<Character, String> mapping = codes.stream()
                .collect(Collectors.toMap(CharacterCode::character, CharacterCode::code));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            sb.append(mapping.get(currentChar));
        }
        return sb;
    }

    private static EncodingResults encodeTextToBytes(List<CharacterCode> codes, StringBuilder sb) {
        int numberOfBits = sb.length();
        int numberOfBytes = sb.length() / 8 + ((sb.length() % 8 != 0) ? 1 : 0);
        byte[] data = new byte[numberOfBytes];

        int index = 0;
        while (sb.length() > 8) {
            data[index] = (byte)Integer.parseInt(sb.substring(0, 8), 2);
            index++;
            sb.delete(0, 8);
        }
        if (sb.length() > 0) {
            String paddedString = sb + "0".repeat(8 - sb.length());
            data[index] = (byte)Integer.parseInt(paddedString, 2);
        }

        return new EncodingResults(codes, numberOfBits, data);
    }

    private static String leftPadZeros(String input, int length) {
        return String.format("%" + length + "s", input).replace(' ', '0');
    }

    private static String rightPadZeros(String input, int length) {
        return String.format("%" + (-length) + "s", input).replace(' ', '0');
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

}
