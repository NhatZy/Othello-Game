package othellogame.ai;

import othellogame.model.*;

/**
 * Represents a strategy for determining moves in the Othello game.
 * <p>
 * Implementations of this interface provide different strategies for making moves in the game, encompassing both naive and smart strategies.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see Move
 * @see Game
 */
public interface Strategy {
    /**
     * Gets the name of the strategy.
     * @return the name of the strategy.
     */
    String getStrategy();

    /**
     * Determines the next move based on the strategy.
     * @param game the current Othello game.
     * @return the determined move.
     */
    Move determineMove(Game game);
}