package alapfeladatok.huffman;

import java.util.List;

public record EncodingResults(List<CharacterCode> codes, int numberOfBits, byte[] data) {
}
