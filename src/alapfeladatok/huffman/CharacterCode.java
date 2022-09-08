package alapfeladatok.huffman;

import java.util.Comparator;

public record CharacterCode(char character, String code) implements Comparable<CharacterCode> {

    @Override
    public int compareTo(CharacterCode other) {
        return Comparator.<CharacterCode>comparingInt(x -> x.code.length())
                .thenComparing(x -> x.character)
                .compare(this, other);
    }
}
