package alapfeladatok;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.function.UnaryOperator;

public class A19 {

    // java A19 <command> [-in <filename>] [-out <filename>]
    public static void main(String[] args) throws IOException {
        ExecutionParameters params = null;

        try {
            params = processProgramArguments(args);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Usage: java A19 <command> [-in <filename>] [-out <filename>]");
            System.exit(-1);
        }

        InputStream inputStream = params.inputFile() == null ? System.in : new FileInputStream(params.inputFile());
        OutputStream outputStream = params.outputFile() == null ? System.out : new FileOutputStream(params.outputFile());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             PrintWriter writer = new PrintWriter(outputStream)) {

            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            UnaryOperator<String> operator = switch (params.command()) {
                case "cypher" -> A19::cypher;
                case "decypher" -> A19::decypher;
                default -> throw new IllegalStateException("Unknown command: " + params.command());
            };
            writer.println(operator.apply(buffer.toString()));
        }
    }

    private static ExecutionParameters processProgramArguments(String[] args) {
        if (args.length != 1 && args.length != 3 && args.length != 5) {
            throw new IllegalArgumentException("Invalid number of parameters");
        }

        String command = args[0];
        if (!"cypher".equalsIgnoreCase(command) && !"decypher".equalsIgnoreCase(command)) {
            throw new IllegalArgumentException("Unknown command: " + args[0] + "\nAcceptable commands: cypher, decypher");
        }

        String input = null;
        String output = null;
        for (int i = 1; i < args.length; i += 2) {
            String nextValue = args[i + 1];
            switch (args[i]) {
                case "-in" -> input = nextValue;
                case "-out" -> output = nextValue;
                default -> throw new IllegalArgumentException("Unknown parameter: " + args[i]);
            }
        }

        return new ExecutionParameters(command, input, output);
    }

    private static String cypher(String text) {
        StringBuilder buffer = cleanText(text);
        char[][] block = createBlock(buffer.length(), false);

        TriPredicate writeChar = (bi, i, j) -> bi < buffer.length();
        writeBlock(buffer, block, writeChar);

        return readBlockByColumns(block);
    }

    private static String decypher(String text) {
        StringBuilder buffer = cleanText(text);
        int length = buffer.length();
        char[][] block = createBlock(length, true);
        int missingChars = (block.length * block[0].length) - length;

        TriPredicate writeChar = (bi, i, j) -> i < block.length - missingChars || j != block[i].length - 1;
        writeBlock(buffer, block, writeChar);

        return readBlockByColumns(block);
    }

    private static StringBuilder cleanText(String text) {
        StringBuilder buffer = new StringBuilder(text.length());

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                buffer.append(Character.toLowerCase(c));
            }
        }
        return buffer;
    }

    private static char[][] createBlock(int length, boolean invert) {
        double sqrt = Math.sqrt(length);
        int column = (int) Math.ceil(sqrt);
        int row = (int) Math.floor(sqrt);

        if (row * column < length) {
            row++;
        }
        return invert ? new char[column][row] : new char[row][column];
    }

    private static void writeBlock(StringBuilder buffer, char[][] block, TriPredicate writeChar) {
        int bufferIndex = 0;
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[i].length; j++) {
                if (writeChar.test(bufferIndex, i, j)) {
                    block[i][j] = buffer.charAt(bufferIndex++);
                } else {
                    block[i][j] = ' ';
                }
            }
        }
    }

    private static String readBlockByColumns(char[][] block) {
        StringBuilder buffer = new StringBuilder(block.length * block[0].length);
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[j][i] != ' ') {
                    buffer.append(block[j][i]);
                }
            }
        }
        return buffer.toString();
    }

    private record ExecutionParameters(String command, String inputFile, String outputFile) {}

    private interface TriPredicate {
        boolean test(int bufferIndex, int i, int j);
    }
}
