package oo.labyrinth.generator;

import oo.labyrinth.generator.model.Maze;

public interface MazeGenerator {

    Maze generate(int rows, int columns);

}
