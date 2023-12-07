package othellogame.model;

/**
 * Represents the possible states of a cell on the Othello game board. Each cell can be in one of 3 states:
 * <p>
 * - "BLACK": Represents a cell occupied by a black game piece.
 * <p>
 * - "WHITE": Represents a cell occupied by a white game piece.
 * <p>
 * - "EMPTY": Represents an empty cell.
 * <p>
 * This enum is also used to facilitate the determination of the opponent's mark during gameplay by providing the method "otherMark()".
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 */
public enum Mark {
    /**
     * Represents a cell occupied by a black game piece.
     */
    BLACK,
    /**
     * Represents a cell occupied by a white game piece.
     */
    WHITE,
    /**
     * Represents an empty cell.
     */
    EMPTY;

    /**
     * Returns the opposite mark.
     * @return the opposite mark.
     */
    public Mark otherMark() {
        if (this == BLACK) {
            return WHITE;
        } else if (this == WHITE) {
            return BLACK;
        } else {
            return EMPTY;
        }
    }
}