package tömbök.caesar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class T4 {

    private static final String ALPHABET = "AÁBCDEÉFGHIÍJKLMNOÓÖŐPQRSTUÚÜŰVWXYZ";

    // java T4 <command> -k <n> [-in <filename>] [-out <filename>]
    public static void main(String[] args) throws IOException {
        ExecutionParameters params = null;

        try {
            params = processProgramArguments(args);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Usage: java T4 <command> -k <n> [-in <filename>] [-out <filename>]");
            System.exit(-1);
        }

        InputStream inputStream = params.inputFile() == null ? System.in : new FileInputStream(params.inputFile());
        OutputStream outputStream = params.outputFile() == null ? System.out : new FileOutputStream(params.outputFile());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             PrintWriter writer = new PrintWriter(outputStream)) {

            String line;
            while ((line = reader.readLine()) != null) {
                switch (params.command()) {
                    case "cypher" -> writer.println(cypher(line, params.key()));
                    case "decypher" -> writer.println(decypher(line, params.key()));
                }
            }
        }
    }

    private static ExecutionParameters processProgramArguments(String[] args) {
        if (args.length != 3 && args.length != 5 && args.length != 7) {
            throw new IllegalArgumentException("Invalid number of paramters");
        }

        String command = args[0];
        if (!"cypher".equalsIgnoreCase(command) && !"decypher".equalsIgnoreCase(command)) {
            throw new IllegalArgumentException("Unknown command: " + args[0] + "\nAcceptable commands: cypher, decypher");
        }

        Integer key = null;
        String input = null;
        String output = null;
        for (int i = 1; i < args.length; i += 2) {
            String nextValue = args[i + 1];
            switch (args[i]) {
                case "-k" -> key = tryParseKey(nextValue);
                case "-in" -> input = nextValue;
                case "-out" -> output = nextValue;
                default -> throw new IllegalArgumentException("Unknown parameter: " + args[i]);
            }
        }

        if (key == null || key < 1 || key >= ALPHABET.length()) {
            throw new IllegalArgumentException("Invalid key: " + key);
        }

        return new ExecutionParameters(command, key, input, output);
    }

    private static Integer tryParseKey(String keyValue) {
        try {
            return Integer.valueOf(keyValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid key: " + keyValue);
        }
    }

    private static String cypher(String line, int key) {
        StringBuilder builder = new StringBuilder(line.length());
        for (int i = 0; i < line.length(); i++) {
            char clear = Character.toUpperCase(line.charAt(i));
            char coded;
            int index = ALPHABET.indexOf(clear);
            if (index >= 0) {
                coded = ALPHABET.charAt((index + key) % ALPHABET.length());
            } else {
                coded = clear;
            }
            builder.append(coded);
        }
        return builder.toString();
    }

    private static String decypher(String line, int key) {
        StringBuilder builder = new StringBuilder(line.length());
        for (int i = 0; i < line.length(); i++) {
            char coded = Character.toUpperCase(line.charAt(i));
            char clear;
            int index = ALPHABET.indexOf(coded);
            if (index >= 0) {
                clear = ALPHABET.charAt((index - key + ALPHABET.length()) % ALPHABET.length());
            } else {
                clear = coded;
            }
            builder.append(clear);
        }
        return builder.toString();
    }

    private record ExecutionParameters(String command, int key, String inputFile, String outputFile) {}

}
