package tömbök.sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class T3 {

    public static void main(String[] args) {
        SudokuTable sudokuTable = new SudokuTable();

        List<Command> commands = List.of(
                new NewCommand(sudokuTable),
                new ExitCommand(sudokuTable),
                new PutCommand(sudokuTable)
        );

        try (Scanner input = new Scanner(System.in)){
            do {
                System.out.print("? ");
                String line = input.nextLine();
                String[] cmdBits = line.split(" ");
                Optional<Command> matchingCommand = findCommand(commands, cmdBits[0]);
                if (matchingCommand.isPresent()) {
                    matchingCommand.get().execute(cmdBits);
                } else {
                    System.out.println("Unknown command");
                }
            } while(true);
        }
    }

    private static Optional<Command> findCommand(List<Command> commands, String enteredCommand) {
        for (Command command : commands) {
            if (command.acceptsCommand(enteredCommand)) {
                return Optional.of(command);
            }
        }
        return Optional.empty();
    }
}
