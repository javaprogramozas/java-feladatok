package oo.labyrinth.generator.model;

public enum MazeGeneratorType {

    BACKTRACK("Backtrack"), ELLERS("Eller's algorithm");

    public final String label;

    MazeGeneratorType(String label) {
        this.label = label;
    }
}
