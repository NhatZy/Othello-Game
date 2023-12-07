package othellogame.model;

/**
 * Represents a move in the Othello game.
 * <p>
 * Each move is associated with a player's mark and the index where the move is made on the game board.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see Move
 * @see Mark
 */
public class OthelloMove implements Move {
    /**
     * The player's mark associated with this move (BLACK or WHITE).
     */
    private Mark mark;

    /**
     * The index on the game board where the move is made.
     */
    private int index;

    /**
     * Constructs an {@code OthelloMove} object with the specified player's mark and board index.
     * @param mark the player's mark (BLACK or WHITE).
     * @param index the index on the game board where the move is made.
     */
    public OthelloMove(Mark mark, int index) {
        this.mark = mark;
        this.index = index;
    }

    /**
     * Gets the player's mark associated with this move.
     * @return the player's mark.
     */
    public Mark getMark() {
        return mark;
    }

    /**
     * Gets the board index where the move is made.
     * @return the index on the game board.
     */
    public int getIndex() {
        return index;
    }
}