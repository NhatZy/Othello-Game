package othellogame.model;

import java.util.*;

/**
 * The Game interface represents the contract for the Othello game. It defines methods to manage the board, players, moves, and the game state.
 * <p>
 * Othello is a two-player strategy game where players take turns placing their pieces on the board with the goal of capturing the opponent's pieces and dominating the majority of the board.
 * <p>
 * Implementations of this interface should provide functionality for:
 * <p>
 * - Getting the current turn's player.
 * <p>
 * - Swapping the turn to the next player.
 * <p>
 * - Accessing the game board.
 * <p>
 * - Counting the number of black and white pieces on the board.
 * <p>
 * - Validating moves and obtaining lists of valid moves for the current player and the opponent.
 * <p>
 * - Flipping pieces on the board according to game rules.
 * <p>
 * - Executing player moves.
 * <p>
 * - Determining if the game is over and identifying the winner.
 * <p>
 * - Checking if it's the first player's turn.
 * <p>
 * - Creating deep copies of the game state.
 * <p>
 * - Calculating a heuristic score for the current game state.
 * <p>
 * - Retrieving a list of positions to flip after a move.
 * <p>
 * The interface follows the Othello game rules and provides a way to interact with and manipulate the game state.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see Player
 * @see Board
 * @see Move
 * @see Mark
 * @see OthelloGame
 */
public interface Game {
    /**
     * Gets the player whose turn is currently is.
     * @return the current turn's player.
     */
    Player getTurn();

    /**
     * Swaps the turn to the next player.
     */
    void swapTurn();

    /**
     * Gets the game board.
     * @return the game board.
     */
    Board getBoard();

    /**
     * Counts the number of black pieces on the board.
     * @return the count of black pieces.
     */
    int countBlack();

    /**
     * Counts the number of white pieces on the board.
     * @return the count of white pieces.
     */
    int countWhite();

    /**
     * Checks if the given move is a valid move for the current player.
     * @param move the move to validate.
     * @return true if the move is valid, false otherwise.
     */
    boolean isValidMove(Move move);

    /**
     * Gets a list of valid moves for the current player.
     * @return a list of valid moves.
     */
    List<Move> getValidMoves();

    /**
     * Gets a list of valid moves for the opponent of the current player.
     * @return a list of valid moves for the opponent.
     */
    List<Move> getOpponentValidMoves();

    /**
     * Flips the pieces on the board according to the Othello game rules.
     * @param index the index of the move to be executed.
     */
    void flip(int index);

    /**
     * Executes the specified move on the game board.
     * @param move the move to be executed.
     */
    void doMove(Move move);

    /**
     * Checks if the game is over.
     * @return true if the game is over, false otherwise.
     */
    boolean isGameOver();

    /**
     * Gets the winner of the game.
     * @return the mark of the winner (BLACK, WHITE, or EMPTY if the game is a tie).
     */
    Mark getWinner();

    /**
     * Checks if it's the first player's turn.
     * @return true if it's the first player's turn, false otherwise.
     */
    boolean isFirstPlayerTurn();

    /**
     * Creates a deep copy of the current game state.
     * @return a new OthelloGame object with the same state as the current game.
     */
    OthelloGame deepCopy();

    /**
     * Calculates a heuristic score for the current game state.
     * @return the heuristic score.
     */
    double calculateHeuristicScore();

    /**
     * Gets a list of positions representing the fields to flip after the last move.
     * @return a list of lists where each inner list contains row and column indices of fields to flip.
     */
    List<List<Integer>> getFieldsToFlipList();
}