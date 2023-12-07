package othellogame.model;

/**
 * Represents the game board for Othello, a two-player strategy game.
 * <p>
 * The board is a 8x8 grid, and each cell can be occupied by one of three possible marks:
 * <p>
 * - "Mark.BLACK": Represents a cell occupied by a black piece.
 * <p>
 * - "Mark.WHITE": Represents a cell occupied by a white piece.
 * <p>
 * - "Mark.EMPTY": Represents an empty cell.
 * <p>
 * The class provides methods for querying and manipulating the state of the board, including setting and getting the mark at a specific position, checking if a cell is empty, and performing deep copies of the board.
 * <p>
 * Additionally, the class includes methods for visualizing the board as a string and checking the stability of a move for a given player. Stability is determined by examining the surrounding cells and assessing whether a move creates a stable position for the player.
 * <p>
 * The class also includes constants for the dimension of the board, color codes for console output, and methods for resetting the board to its initial state.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see Mark
 */
public class Board {
    /**
     * The dimension of the square game board.
     */
    public static final int DIM = 8;

    /**
     * The total number of cells on the game board.
     */
    public static final int TOTALDIM = DIM * DIM;

    /**
     * Array representing the state of each cell on the board.
     */
    private final Mark[] fields = new Mark[TOTALDIM];

    /**
     * Delimiter for formatting console output.
     */
    private static final String DELIM = "     ";

    /**
     * Line used for formatting console output.
     */
    private static final String SPLITTER = "-----+-----+-----+-----+-----+-----+-----+-----";

    /**
     * ANSI color code for black text.
     */
    private static final String BLACK = "\033[38;2;0;0;0m";

    /**
     * ANSI escape code for bold text.
     */
    private static final String BOLD = "\033[0;1m";

    /**
     * ANSI color code for maroon text.
     */
    private static final String MAROON = "\033[38;2;128;0;0m";

    /**
     * ANSI escape code to reset text formatting.
     */
    private static final String RESET = "\033[0m";

    /**
     * ANSI color code for forest green background.
     */
    private static final String FORESTGREEN_BACKGROUND = "\033[48;2;34;139;34m";

    /**
     * Initializes the board with the initial Othello game setup.
     * <p>
     * Sets up the starting positions for black and white pieces.
     */
    public Board() {
        for (int i = 0; i < TOTALDIM; i++) {
            fields[i] = Mark.EMPTY;
        }
        fields[index(3, 3)] = Mark.WHITE;
        fields[index(3, 4)] = Mark.BLACK;
        fields[index(4, 3)] = Mark.BLACK;
        fields[index(4, 4)] = Mark.WHITE;
    }

    /**
     * Creates a deep copy of the current board.
     * @return a new {@code Board} object with the same state as the current board.
     */
    public Board deepCopy() {
        Board board = new Board();
        for (int i = 0; i < TOTALDIM; i++) {
            board.setField(i, getField(i));
        }
        return board;
    }

    /**
     * Converts row and column coordinates to a one-dimensional index.
     * @param row the row coordinate.
     * @param col the column coordinate.
     * @return the one-dimensional index corresponding to the specified coordinates.
     */
    public int index(int row, int col) {
        return row * DIM + col;
    }

    /**
     * Checks if the given index is a valid field on the board.
     * @param index the one-dimensional index to check.
     * @return true if the index is valid, false otherwise.
     */
    public boolean isField(int index) {
        return 0 <= index && index < TOTALDIM;
    }

    /**
     * Gets the mark at the specified index on the board.
     * @param index the one-dimensional index to retrieve the mark from.
     * @return the mark at the specified index.
     */
    public Mark getField(int index) {
        return fields[index];
    }

    /**
     * Checks if the specified index on the board is an empty field.
     * @param index the one-dimensional index to check.
     * @return true if the field is empty, false otherwise.
     */
    public boolean isEmptyField(int index) {
        return getField(index) == Mark.EMPTY;
    }

    /**
     * Sets the mark at the specified index on the board.
     * @param index the one-dimensional index to set the mark at.
     * @param m the mark to set at the specified index.
     */
    public void setField(int index, Mark m) {
        if (isField(index)) {
            fields[index] = m;
        }
    }

    /**
     * Resets the board to its initial state, clearing all marks except for the starting positions of black and white pieces.
     */
    public void reset() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if ((i == 3 && j == 3) || (i == 3 && j == 4) || (i == 4 && j == 3) || (i == 4 && j == 4)) {
                    continue;
                } else {
                    setField(index(i, j), Mark.EMPTY);
                }
            }
        }
    }

    /**
     * Generates a string representation of the board with index information.
     * <p>
     * Displays the marks, index, and background color for each cell.
     * @return a formatted string representing the board.
     */
    public String toIndexString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < DIM; i++) {
            StringBuilder sbForRow = new StringBuilder();
            for (int j = 0; j < DIM; j++) {
                sbForRow.append(FORESTGREEN_BACKGROUND).append("  ").append(getField(index(i, j)).toString().substring(0, 1).
                        replace("B", FORESTGREEN_BACKGROUND + BLACK + FORESTGREEN_BACKGROUND + "B" + RESET + FORESTGREEN_BACKGROUND)
                        .replace("W", FORESTGREEN_BACKGROUND + BOLD + FORESTGREEN_BACKGROUND + "W" + RESET + FORESTGREEN_BACKGROUND)
                        .replace("E", FORESTGREEN_BACKGROUND + MAROON + FORESTGREEN_BACKGROUND + index(i, j) + RESET + FORESTGREEN_BACKGROUND));
                if (index(i, j) < 10 || getField(index(i, j)) != Mark.EMPTY) {
                    sbForRow.append(" ");
                }
                if (j < DIM - 1) {
                    sbForRow.append(" |");
                }
            }
            sb.append("  ").append(FORESTGREEN_BACKGROUND).append(sbForRow).append(" " + RESET).append(DELIM).append("   ");
            if (i < DIM - 1) {
                sb.append("\n").append("  ").append(FORESTGREEN_BACKGROUND).append(SPLITTER).append(RESET).append(DELIM).append("\n");
            }
        }
        return "\n" + sb + "\n";
    }

    /**
     * Checks the stability of a move for a given player at the specified coordinates.
     * <p>
     * Stability is determined by examining the surrounding cells.
     * @param m the mark of the player making the move.
     * @param x the row coordinate of the move.
     * @param y the column coordinate of the move.
     * @return a stability value: 1 for stable, 0 for semi-stable, -1 for unstable.
     */
    public double checkStability(Mark m, int x, int y) {
        if (getField(index(x, y)) != m) {
            return 0;
        }
        int[] adjX = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] adjY = {-1, 0, 1, 1, 1, 0, -1, -1};
        double firstStabilityChecking = 1, finalStabilityChecking = 1;
        for (int i = 0; i < 8; i++) {
            int curX1 = x + adjX[i], curY1 = y + adjY[i];
            int curX2 = x - adjX[i], curY2 = y - adjY[i];
            while (isField(index(curX1, curY1))) {
                if (getField(index(curX1, curY1)) == m.otherMark()) {
                    firstStabilityChecking = 2;
                    break;
                } else if (getField(index(curX1, curY1)) == Mark.EMPTY) {
                    firstStabilityChecking = 1;
                    break;
                }
                curX1 += adjX[i];
                curY1 += adjY[i];
            }
            if (firstStabilityChecking == 0) {
                continue;
            }
            while (isField(index(curX2, curY2))) {
                if (getField(index(curX2, curY2)) == m.otherMark()) {
                    if (firstStabilityChecking == 2) {
                        firstStabilityChecking = 1;
                    } else if (firstStabilityChecking == 1) {
                        firstStabilityChecking = -1;
                    } else if (firstStabilityChecking == 0) {
                        firstStabilityChecking = 1;
                    }
                    break;
                } else if (getField(index(curX2, curY2)) == Mark.EMPTY) {
                    if (firstStabilityChecking == 2) {
                        firstStabilityChecking = -1;
                    } else if (firstStabilityChecking == 1) {
                        firstStabilityChecking = 0;
                    } else if (firstStabilityChecking == 0) {
                        firstStabilityChecking = 1;
                    }
                    break;
                }
                curX2 -= adjX[i];
                curY2 -= adjY[i];
            }
            if (firstStabilityChecking == -1) {
                finalStabilityChecking = -1;
                break;
            }
            if (firstStabilityChecking == 0) {
                finalStabilityChecking = 0;
            }
        }
        return finalStabilityChecking;
    }
}