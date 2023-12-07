package othellogame.model;

import java.util.*;

/**
 * Represents a human player in the Othello game. It extends the {@link AbstractPlayer} class and implements the behavior of a player who can make moves interactively through the console.
 * <p>
 * The {@code HumanPlayer} class relies on the {@link Scanner} class for console input.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see AbstractPlayer
 * @see Mark
 * @see Move
 * @see Game
 */
public class HumanPlayer extends AbstractPlayer {
    /**
     * The mark associated with the human player.
     */
    private final Mark mark;

    /**
     * Constructs a {@code HumanPlayer} object with the specified name and mark.
     * @param name the name of the human player.
     * @param mark the mark associated with the player.
     */
    public HumanPlayer(String name, Mark mark) {
        super(name, mark);
        this.mark = mark;
    }

    /**
     * Allows the human player to interactively determine their move by entering a move index via the console.
     * <p>
     * Handles input validation and ensures the entered move is valid on the current game board.
     * <p>
     * If the player enters "64", it indicates an inability to play, and the turn is swapped to the next player.
     * @param game the current Othello game.
     * @return an OthelloMove object representing the chosen move.
     */
    @Override
    public Move determineMove(Game game) {
        Scanner scanner = new Scanner(System.in);
        OthelloGame othelloGame = (OthelloGame) game;
        OthelloMove move = null;
        int index;
        System.out.println("If you are unable to play, please enter 64");
        boolean validInput;
        do {
            if (game.getValidMoves().isEmpty()) {
                System.out.println("There is no more valid move for you to make");
            }
            System.out.print(game.getTurn() + ", please enter an index: ");
            try {
                index = scanner.nextInt();
                if (index == 64) {
                    if (game.getValidMoves().isEmpty()) {
                        move = null;
                        scanner.nextLine();
                        game.swapTurn();
                        validInput = true;
                    } else {
                        System.out.println("You still have possible move(s); therefore, you cannot skip your turn!");
                        scanner.nextLine();
                        validInput = false;
                    }
                } else {
                    move = new OthelloMove(mark, index);
                    if (othelloGame.isValidMove(move)) {
                        validInput = true;
                    } else {
                        System.out.println(index + " is an invalid move!");
                        scanner.nextLine();
                        validInput = false;
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a whole number for a move!");
                scanner.nextLine();
                validInput = false;
            }
        } while (!validInput);
        return move;
    }
}