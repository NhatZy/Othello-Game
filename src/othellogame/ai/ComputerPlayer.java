package othellogame.ai;

import othellogame.model.*;

/**
 * Represents a computer player in the Othello game.
 * <p>
 * The computer player uses a specific strategy to determine its move in the game.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see AbstractPlayer
 * @see Strategy
 * @see Mark
 * @see Move
 * @see Game
 */
public class ComputerPlayer extends AbstractPlayer {
    /**
     * The strategy used by the computer player to determine its moves.
     */
    private Strategy strategy;

    /**
     * The mark associated with the computer player.
     */
    private final Mark mark;

    /**
     * Constructs a {@code ComputerPlayer} object with a specified strategy and mark.
     * @param strategy the strategy to be used by the computer player.
     * @param mark the mark associated with the computer player.
     */
    public ComputerPlayer(Strategy strategy, Mark mark) {
        super(strategy.getStrategy(), mark);
        this.strategy = strategy;
        this.mark = mark;
    }

    /**
     * Gets the mark associated with the computer player.
     * @return the mark of the computer player.
     */
    @Override
    public Mark getMark() {
        return mark;
    }

    /**
     * Determines the move to be played by the computer player based on its strategy.
     * @param game the current Othello game.
     * @return the move chosen by the computer player.
     */
    @Override
    public Move determineMove(Game game) {
        return strategy.determineMove(game);
    }
}