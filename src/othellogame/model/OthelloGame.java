package othellogame.model;

import java.util.*;

/**
 * Represents an instance of the Othello game, including the game board, players, and game logic.
 * <p>
 * It implements the {@link Game} interface and provides methods to play the Othello game, including determining valid moves, making moves, checking for game over, and calculating heuristic scores.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see Game
 * @see Board
 * @see Player
 * @see Move
 * @see Mark
 * @see OthelloGame
 */
public class OthelloGame implements Game {
    /**
     * Represents the game board for the Othello game.
     */
    private final Board board;

    /**
     * Represents the first player in the Othello game.
     */
    private final Player player1;

    /**
     * Represents the second player in the Othello game.
     */
    private final Player player2;

    /**
     * Represents the index of the current player. It's used to determine which player's turn it is.
     */
    private int playerIndex;

    /**
     * Represents the list of fields to flip for the last move.
     * <p>
     * This list is updated after each move to store the fields that were flipped as a result of the move.
     */
    private List<List<Integer>> fieldsToFlipList = new ArrayList<>();

    /**
     * Constructs an {@code OthelloGame} object with a specified game board, 2 players, and the index of the current player.
     * @param board the game board.
     * @param player1 first player.
     * @param player2 second player.
     * @param playerIndex index of the current player.
     */
    public OthelloGame(Board board, Player player1, Player player2, int playerIndex) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.playerIndex = playerIndex;
    }

    /**
     * Constructs an {@code OthelloGame} object with a default game board and 2 players.
     * @param player1 first player.
     * @param player2 second player.
     */
    public OthelloGame(Player player1, Player player2) {
        this.board = new Board();
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Gets the player whose turn it is.
     * @return the current player.
     */
    @Override
    public Player getTurn() {
        if (playerIndex == 0) {
            return player1;
        } else {
            return player2;
        }
    }

    /**
     * Swaps the turn to the next player.
     */
    @Override
    public void swapTurn() {
        playerIndex = playerIndex ^ 1;
    }

    /**
     * Gets the game board.
     * @return the game board.
     */
    @Override
    public Board getBoard() {
        return board;
    }

    /**
     * Counts the number of black discs on the board.
     * @return the number of black discs.
     */
    @Override
    public int countBlack() {
        if (isGameOver()) {
            int count = 0;
            for (int i = 0; i < Board.TOTALDIM; i++) {
                if (board.getField(i) == Mark.BLACK) {
                    count++;
                }
            }
            return count;
        } else {
            return -1;
        }
    }

    /**
     * Counts the number of white discs on the board.
     * @return the number of white discs.
     */
    @Override
    public int countWhite() {
        if (isGameOver()) {
            int count = 0;
            for (int i = 0; i < Board.TOTALDIM; i++) {
                if (board.getField(i) == Mark.WHITE) {
                    count++;
                }
            }
            return count;
        } else {
            return -1;
        }
    }

    /**
     * Checks if a move is valid.
     * @param move the move to check.
     * @return true if the move is valid, false otherwise.
     */
    @Override
    public boolean isValidMove(Move move) {
        if (!(move instanceof OthelloMove)) {
            return false;
        }
        OthelloMove othelloMove = (OthelloMove) move;
        Mark currentMark = playerIndex == 0 ? Mark.BLACK : Mark.WHITE;
        Mark opponentMark = currentMark.otherMark();
        int row = othelloMove.getIndex() / Board.DIM;
        int col = othelloMove.getIndex() % Board.DIM;
        if (!board.isField(board.index(row, col)) || board.getField(board.index(row, col)) != Mark.EMPTY) {
            return false;
        }

        {
            boolean firstCheck = false;
            int i = row - 1;
            int j = col - 1;
            while (i >= 0 && j >= 0) {
                if (firstCheck) {
                    if (board.getField(board.index(i, j)) == currentMark) {
                        return true;
                    }
                }
                if (board.getField(board.index(i, j)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                }
                i--;
                j--;
            }
        }

        {
            boolean firstCheck = false;
            for (int i = row - 1; i >= 0; i--) {
                if (firstCheck) {
                    if (board.getField(board.index(i, col)) == currentMark) {
                        return true;
                    }
                }
                if (board.getField(board.index(i, col)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                }
            }
        }

        {
            boolean firstCheck = false;
            int i = row - 1;
            int j = col + 1;
            while (i >= 0 && j < Board.DIM) {
                if (firstCheck) {
                    if (board.getField(board.index(i, j)) == currentMark) {
                        return true;
                    }
                }
                if (board.getField(board.index(i, j)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                }
                i--;
                j++;
            }
        }

        {
            boolean firstCheck = false;
            for (int j = col + 1; j < Board.DIM; j++) {
                if (firstCheck) {
                    if (board.getField(board.index(row, j)) == currentMark) {
                        return true;
                    }
                }
                if (board.getField(board.index(row, j)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                }
            }
        }

        {
            boolean firstCheck = false;
            int i = row + 1;
            int j = col + 1;
            while (i < Board.DIM && j < Board.DIM) {
                if (firstCheck) {
                    if (board.getField(board.index(i, j)) == currentMark) {
                        return true;
                    }
                }
                if (board.getField(board.index(i, j)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                }
                i++;
                j++;
            }
        }

        {
            boolean firstCheck = false;
            for (int i = row + 1; i < Board.DIM; i++) {
                if (firstCheck) {
                    if (board.getField(board.index(i, col)) == currentMark) {
                        return true;
                    }
                }
                if (board.getField(board.index(i, col)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                }
            }
        }

        {
            boolean firstCheck = false;
            int i = row + 1;
            int j = col - 1;
            while (i < Board.DIM && j >= 0) {
                if (firstCheck) {
                    if (board.getField(board.index(i, j)) == currentMark) {
                        return true;
                    }
                }
                if (board.getField(board.index(i, j)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                }
                i++;
                j--;
            }
        }

        {
            boolean firstCheck = false;
            for (int j = col - 1; j >= 0; j--) {
                if (firstCheck) {
                    if (board.getField(board.index(row, j)) == currentMark) {
                        return true;
                    }
                }
                if (board.getField(board.index(row, j)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                }
            }
        }

        return false;
    }

    /**
     * Gets a list of valid moves for the current player.
     * @return a list of valid moves for the current player.
     */
    @Override
    public List<Move> getValidMoves() {
        List<Move> validMoves = new ArrayList<>();
        Mark currentMark = playerIndex == 0 ? Mark.BLACK : Mark.WHITE;
        for (int i = 0; i < Board.TOTALDIM; i++) {
            for (int j = 0; j < Board.DIM; j++) {
                Move currentMove = new OthelloMove(currentMark, board.index(i, j));
                if (isValidMove(currentMove)) {
                    validMoves.add(currentMove);
                }
            }
        }
        return validMoves;
    }

    /**
     * Gets a list of valid moves for the opponent.
     * @return a list of valid moves for the opponent.
     */
    @Override
    public List<Move> getOpponentValidMoves() {
        List<Move> validMoves = new ArrayList<>();
        Mark currentMark = playerIndex == 0 ? Mark.BLACK : Mark.WHITE;
        swapTurn();
        for (int i = 0; i < Board.TOTALDIM; i++) {
            for (int j = 0; j < Board.DIM; j++) {
                Move currentMove = new OthelloMove(currentMark.otherMark(), board.index(i, j));
                if (isValidMove(currentMove)) {
                    validMoves.add(currentMove);
                }
            }
        }
        swapTurn();
        return validMoves;
    }

    /**
     * Flips the discs on the board based on the specified move.
     * @param index the index of the move.
     */
    @Override
    public void flip(int index) {
        if (board.getField(index) == Mark.BLACK) {
            board.setField(index, Mark.WHITE);
        } else if (board.getField(index) == Mark.WHITE) {
            board.setField(index, Mark.BLACK);
        }
    }

    /**
     * Performs the specified move on the board.
     * @param move the move to perform.
     */
    @Override
    public void doMove(Move move) {
        OthelloMove othelloMove = (OthelloMove) move;
        if (othelloMove == null) {
            return;
        }
        board.setField(board.index(othelloMove.getIndex() / Board.DIM, othelloMove.getIndex() % Board.DIM), othelloMove.getMark());
        swapTurn();
        int row = othelloMove.getIndex() / Board.DIM;
        int col = othelloMove.getIndex() % Board.DIM;
        Mark currentMark = othelloMove.getMark();
        Mark opponentMark = currentMark.otherMark();
        this.fieldsToFlipList.clear();

        {
            List<Integer> fieldsToFlip = new ArrayList<>();
            boolean firstCheck = false, stop = false;
            int i = row - 1;
            int j = col - 1;
            while (i >= 0 && j >= 0) {
                if (firstCheck) {
                    if (board.getField(board.index(i, j)) == currentMark) {
                        stop = true;
                        break;
                    }
                }
                if (board.getField(board.index(i, j)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                    fieldsToFlip.add(board.index(i, j));
                }
                i--;
                j--;
            }
            if (stop) {
                for (Integer index : fieldsToFlip) {
                    flip(index);
                }
                fieldsToFlipList.add(fieldsToFlip);
            }
        }

        {
            List<Integer> fieldsToFlip = new ArrayList<>();
            boolean firstCheck = false, stop = false;
            for (int i = row - 1; i >= 0; i--) {
                if (firstCheck) {
                    if (board.getField(board.index(i, col)) == currentMark) {
                        stop = true;
                        break;
                    }
                }
                if (board.getField(board.index(i, col)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                    fieldsToFlip.add(board.index(i, col));
                }
            }
            if (stop) {
                for (Integer index : fieldsToFlip) {
                    flip(index);
                }
                fieldsToFlipList.add(fieldsToFlip);
            }
        }

        {
            List<Integer> fieldsToFlip = new ArrayList<>();
            boolean firstCheck = false, stop = false;
            int i = row - 1;
            int j = col + 1;
            while (i >= 0 && j < Board.DIM) {
                if (firstCheck) {
                    if (board.getField(board.index(i, j)) == currentMark) {
                        stop = true;
                        break;
                    }
                }
                if (board.getField(board.index(i, j)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                    fieldsToFlip.add(board.index(i, j));
                }
                i--;
                j++;
            }
            if (stop) {
                for (Integer index : fieldsToFlip) {
                    flip(index);
                }
                fieldsToFlipList.add(fieldsToFlip);
            }
        }

        {
            List<Integer> fieldsToFlip = new ArrayList<>();
            boolean firstCheck = false, stop = false;
            for (int j = col + 1; j < Board.DIM; j++) {
                if (firstCheck) {
                    if (board.getField(board.index(row, j)) == currentMark) {
                        stop = true;
                        break;
                    }
                }
                if (board.getField(board.index(row, j)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                    fieldsToFlip.add(board.index(row, j));
                }
            }
            if (stop) {
                for (Integer index : fieldsToFlip) {
                    flip(index);
                }
                fieldsToFlipList.add(fieldsToFlip);
            }
        }

        {
            List<Integer> fieldsToFlip = new ArrayList<>();
            boolean firstCheck = false, stop = false;
            int i = row + 1;
            int j = col + 1;
            while (i < Board.DIM && j < Board.DIM) {
                if (firstCheck) {
                    if (board.getField(board.index(i, j)) == currentMark) {
                        stop = true;
                        break;
                    }
                }
                if (board.getField(board.index(i, j)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                    fieldsToFlip.add(board.index(i, j));
                }
                i++;
                j++;
            }
            if (stop) {
                for (Integer index : fieldsToFlip) {
                    flip(index);
                }
                fieldsToFlipList.add(fieldsToFlip);
            }
        }

        {
            List<Integer> fieldsToFlip = new ArrayList<>();
            boolean firstCheck = false, stop = false;
            for (int i = row + 1; i < Board.DIM; i++) {
                if (firstCheck) {
                    if (board.getField(board.index(i, col)) == currentMark) {
                        stop = true;
                        break;
                    }
                }
                if (board.getField(board.index(i, col)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                    fieldsToFlip.add(board.index(i, col));
                }
            }
            if (stop) {
                for (Integer index : fieldsToFlip) {
                    flip(index);
                }
                fieldsToFlipList.add(fieldsToFlip);
            }
        }

        {
            List<Integer> fieldsToFlip = new ArrayList<>();
            boolean firstCheck = false, stop = false;
            int i = row + 1;
            int j = col - 1;
            while (i < Board.DIM && j >= 0) {
                if (firstCheck) {
                    if (board.getField(board.index(i, j)) == currentMark) {
                        stop = true;
                        break;
                    }
                }
                if (board.getField(board.index(i, j)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                    fieldsToFlip.add(board.index(i, j));
                }
                i++;
                j--;
            }
            if (stop) {
                for (Integer index : fieldsToFlip) {
                    flip(index);
                }
                fieldsToFlipList.add(fieldsToFlip);
            }
        }

        {
            List<Integer> fieldsToFlip = new ArrayList<>();
            boolean firstCheck = false, stop = false;
            for (int j = col - 1; j >= 0; j--) {
                if (firstCheck) {
                    if (board.getField(board.index(row, j)) == currentMark) {
                        stop = true;
                        break;
                    }
                }
                if (board.getField(board.index(row, j)) != opponentMark) {
                    break;
                } else {
                    firstCheck = true;
                    fieldsToFlip.add(board.index(row, j));
                }
            }
            if (stop) {
                for (Integer index : fieldsToFlip) {
                    flip(index);
                }
                fieldsToFlipList.add(fieldsToFlip);
            }
        }
    }

    /**
     * Checks if the game is over.
     * @return true if the game is over, false otherwise.
     */
    @Override
    public boolean isGameOver() {
        return getValidMoves().isEmpty() && getOpponentValidMoves().isEmpty();
    }

    /**
     * Gets the winner of the game.
     * @return the winner (BLACK, WHITE, or EMPTY if a tie).
     */
    @Override
    public Mark getWinner() {
        int black = countBlack();
        int white = countWhite();
        if (black > white) {
            return Mark.BLACK;
        } else if (black < white) {
            return Mark.WHITE;
        } else {
            return Mark.EMPTY;
        }
    }

    /**
     * Checks if it's the first player's turn.
     * @return true if it's the first player's turn, false otherwise.
     */
    @Override
    public boolean isFirstPlayerTurn() {
        return playerIndex == 0;
    }

    /**
     * Creates a deep copy of the Othello game.
     * @return a deep copy of the game.
     */
    @Override
    public OthelloGame deepCopy() {
        return new OthelloGame(board.deepCopy(), player1, player2, playerIndex);
    }

    /**
     * Calculates the heuristic score of the current game state.
     * <p>
     * The heuristic considers 4 factors: disc parity, mobility, captured corners, and stability.
     * @return the heuristic score of the current game state.
     */
    @Override
    public double calculateHeuristicScore() {
        double totalScore = 0;
        int[] adjX = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] adjY = {-1, 0, 1, 1, 1, 0, -1, -1};
        double[] cellValue = {4, -3, 2, 2, 2, 2, -3, 4,
            -3, -4, -1, -1, -1, -1, -4, -3,
            2, -1, 1, 0, 0, 1, -1, 2,
            2, -1, 0, 1, 1, 0, -1, 2,
            2, -1, 0, 1, 1, 0, -1, 2,
            2, -1, 1, 0, 0, 1, -1, 2,
            -3, -4, -1, -1, -1, -1, -4, -3,
            4, -3, 2, 2, 2, 2, -3, 4};
        if (board.getField(board.index(0, 0)) != Mark.EMPTY) {
            cellValue[1] = 0;
            cellValue[2] = 0;
            cellValue[3] = 0;
            cellValue[8] = 0;
            cellValue[9] = 0;
            cellValue[10] = 0;
            cellValue[11] = 0;
            cellValue[16] = 0;
            cellValue[17] = 0;
            cellValue[18] = 0;
            cellValue[24] = 0;
            cellValue[25] = 0;
        }
        if (board.getField(board.index(0, 7)) != Mark.EMPTY) {
            cellValue[4] = 0;
            cellValue[5] = 0;
            cellValue[6] = 0;
            cellValue[12] = 0;
            cellValue[13] = 0;
            cellValue[14] = 0;
            cellValue[15] = 0;
            cellValue[21] = 0;
            cellValue[22] = 0;
            cellValue[23] = 0;
            cellValue[30] = 0;
            cellValue[31] = 0;
        }
        if (board.getField(board.index(7, 0)) != Mark.EMPTY) {
            cellValue[32] = 0;
            cellValue[33] = 0;
            cellValue[40] = 0;
            cellValue[41] = 0;
            cellValue[42] = 0;
            cellValue[48] = 0;
            cellValue[49] = 0;
            cellValue[50] = 0;
            cellValue[51] = 0;
            cellValue[57] = 0;
            cellValue[58] = 0;
            cellValue[59] = 0;
        }
        if (board.getField(board.index(7, 7)) != Mark.EMPTY) {
            cellValue[38] = 0;
            cellValue[39] = 0;
            cellValue[45] = 0;
            cellValue[46] = 0;
            cellValue[47] = 0;
            cellValue[52] = 0;
            cellValue[53] = 0;
            cellValue[54] = 0;
            cellValue[55] = 0;
            cellValue[60] = 0;
            cellValue[61] = 0;
            cellValue[62] = 0;
        }
        double discsParity;
        double blackDiscsParity = 0, whiteDiscsParity = 0;
        for (int i = 0; i < Board.TOTALDIM; i++) {
            if (board.getField(i) == Mark.BLACK) {
                blackDiscsParity += cellValue[i];
            } else if (board.getField(i) == Mark.WHITE) {
                whiteDiscsParity += cellValue[i];
            }
        }
        if (blackDiscsParity + whiteDiscsParity != 0) {
            discsParity = (blackDiscsParity - whiteDiscsParity) / (blackDiscsParity + whiteDiscsParity);
        } else {
            discsParity = 0;
        }
        double mobility;
        double blackMoveMobility = 0, whiteMoveMobility = 0;
        int currentPlayerIndex = playerIndex;
        playerIndex = 0;
        blackMoveMobility += 2.5 * getValidMoves().size();
        playerIndex = 1;
        whiteMoveMobility += 2.5 * getValidMoves().size();
        playerIndex = currentPlayerIndex;
        for (int i = 0; i < Board.DIM; i++) {
            for (int j = 0; j < Board.DIM; j++) {
                currentPlayerIndex = playerIndex;
                playerIndex = 0;
                Move blackMove = new OthelloMove(Mark.BLACK, board.index(i, j));
                if (board.getField(board.index(i, j)) == Mark.EMPTY && !isValidMove(blackMove)) {
                    for (int k = 0; k < 8; k++) {
                        int curX = i + adjX[k], curY = j + adjY[k];
                        if (board.isField(board.index(curX, curY))) {
                            if (board.getField(board.index(curX, curY)) == Mark.WHITE) {
                                blackMoveMobility++;
                            }
                        }
                    }
                }
                playerIndex = 1;
                Move whiteMove = new OthelloMove(Mark.WHITE, board.index(i, j));
                if (board.getField(board.index(i, j)) == Mark.EMPTY && !isValidMove(whiteMove)) {
                    for (int k = 0; k < 8; k++) {
                        int curX = i + adjX[k], curY = j + adjY[k];
                        if (board.isField(board.index(curX, curY))) {
                            if (board.getField(board.index(curX, curY)) == Mark.BLACK) {
                                whiteMoveMobility++;
                            }
                        }
                    }
                }
                playerIndex = currentPlayerIndex;
            }
        }
        if (blackMoveMobility + whiteMoveMobility != 0) {
            mobility = (blackMoveMobility - whiteMoveMobility) / (blackMoveMobility + whiteMoveMobility);
        } else {
            mobility = 0;
        }
        double capturedCorners;
        double blackCorners = 0, whiteCorners = 0;
        int[] cornerXIndex = {0, 0, 7, 7};
        int[] cornerYIndex = {0, 7, 0, 7};
        for (int i = 0; i < 4; i++) {
            if (board.getField(board.index(cornerXIndex[i], cornerYIndex[i])) == Mark.BLACK) {
                blackCorners += 3;
            }
            if (board.getField(board.index(cornerXIndex[i], cornerYIndex[i])) == Mark.WHITE) {
                whiteCorners += 3;
            }
        }
        if (blackCorners + whiteCorners != 0) {
            capturedCorners = (blackCorners - whiteCorners) / (blackCorners + whiteCorners);
        } else {
            capturedCorners = 0;
        }
        double stability;
        double blackDiscsStability = 0, whiteDiscsStability = 0;
        for (int i = 0; i < Board.DIM; i++) {
            for (int j = 0; j < Board.DIM; j++) {
                blackDiscsStability += board.checkStability(Mark.BLACK, i, j);
                whiteDiscsStability += board.checkStability(Mark.WHITE, i, j);
            }
        }
        if (blackDiscsStability + whiteDiscsStability != 0) {
            stability = (blackDiscsStability - whiteDiscsStability) / (blackDiscsStability + whiteDiscsStability);
        } else {
            stability = 0;
        }
        int totalDiscs = countBlack() + countWhite();
        if (totalDiscs < 20) {
            totalScore += 40 * capturedCorners;
            totalScore += 30 * stability;
            totalScore += 15 * mobility;
            totalScore += 20 * discsParity;
        } else if (totalDiscs < 45) {
            totalScore += 45 * capturedCorners;
            totalScore += 35 * stability;
            totalScore += 17 * mobility;
            totalScore += 17 * discsParity;
            totalScore += 5 * totalDiscs;
        } else {
            totalScore += 45 * capturedCorners;
            totalScore += 30 * stability;
            totalScore += 17 * mobility;
            totalScore += 25 * discsParity;
            totalScore += 25 * totalDiscs;
        }
        return totalScore;
    }

    /**
     * Retrieves the list of fields to flip for the last move.
     * @return the list of fields to flip.
     */
    @Override
    public List<List<Integer>> getFieldsToFlipList() {
        return this.fieldsToFlipList;
    }
}