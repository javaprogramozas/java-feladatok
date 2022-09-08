package alapfeladatok.huffman;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class A21 {

    // java A21 <command> -in <filename> -out <filename>
    public static void main(String[] args) throws IOException {
        ExecutionParameters params = null;

        try {
            params = processProgramArguments(args);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Usage: java A21 <command> -in <filename> -out <filename>");
            System.exit(-1);
        }

        switch (params.command()) {
            case "encode" -> encode(params.inputFile, params.outputFile);
            case "decode" -> decode(params.inputFile, params.outputFile);
        }
    }

    private static ExecutionParameters processProgramArguments(String[] args) {
        if (args.length != 5) {
            throw new IllegalArgumentException("Invalid number of parameters");
        }

        String command = args[0];
        if (!"encode".equalsIgnoreCase(command) && !"decode".equalsIgnoreCase(command)) {
            throw new IllegalArgumentException("Unknown command: " + args[0] + "\nAcceptable commands: encode, decode");
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

    private static void encode(String inputFile, String outputFile) throws IOException {
        String text = Files.readString(Path.of(inputFile));
        EncodingResults results = Huffman.encode(text);
        System.out.println(results);
        byte[] content = writeAsByteArray(results);
        Files.write(Path.of(outputFile), content);
    }

    private static void decode(String inputFile, String outputFile) throws IOException {
        byte[] content = Files.readAllBytes(Path.of(inputFile));
        DecodingInputs input = readByteStreamAsDecodingInput(content);
        System.out.println(input);
        String decodedText = Huffman.decode(input);
        Files.writeString(Path.of(outputFile), decodedText);
    }

    private static byte[] writeAsByteArray(EncodingResults results) throws IOException {
        // # of characters |  characters array | # of code length | code length array | # of bits | data
        //     4 bytes     | # of ch x 2 bytes |      4 bytes     |  # of cd x bytes  |  4 bytes  | ~ # of bits
        int numberOfCharacters = results.codes().size();
        System.out.println("# of characters: " + numberOfCharacters);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);
        dataOutput.writeInt(numberOfCharacters);
        byte[] codeLength = new byte[results.codes().get(results.codes().size() - 1).code().length()];
        for (CharacterCode code : results.codes()) {
            dataOutput.writeChar(code.character());
            codeLength[code.code().length() - 1]++;
        }
        dataOutput.writeInt(codeLength.length);
        dataOutput.write(codeLength);

        dataOutput.writeInt(results.numberOfBits());
        dataOutput.write(results.data());
        return outputStream.toByteArray();
    }

    private static DecodingInputs readByteStreamAsDecodingInput(byte[] content) throws IOException {
        // # of characters |  characters array | # of code length | code length array | # of bits | data
        //     4 bytes     | # of ch x 2 bytes |      4 bytes     |  # of cd x bytes  |  4 bytes  | ~ # of bits
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        DataInputStream dataInput = new DataInputStream(inputStream);
        int numberOfCharacters = dataInput.readInt();
        System.out.println("# of characters: " + numberOfCharacters);
        char[] characters = new char[numberOfCharacters];
        for (int i = 0; i < numberOfCharacters; i++) {
            characters[i] = dataInput.readChar();
        }
        int codeLengthSize = dataInput.readInt();
        byte[] codeLength = new byte[codeLengthSize];
        dataInput.read(codeLength, 0, codeLengthSize);
        int numberOfBits = dataInput.readInt();
        byte[] data = dataInput.readAllBytes();

        return new DecodingInputs(characters, codeLength, numberOfBits, data);
    }

    private record ExecutionParameters(String command, String inputFile, String outputFile) {}

}
