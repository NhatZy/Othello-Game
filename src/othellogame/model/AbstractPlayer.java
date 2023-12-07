package othellogame.model;

/**
 * An abstract class representing a player in the game.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see Player
 * @see Mark
 * @see Move
 * @see Game
 */
public abstract class AbstractPlayer implements Player {
    /**
     * The name of the player.
     */
    private final String name;

    /**
     * The mark associated with the player.
     */
    private final Mark mark;

    /**
     * Initializes a new instance of the {@code AbstractPlayer} class.
     * @param name the name of the player. If the name starts with "-N" or "-S", it's split to extract the actual name.
     * @param mark the mark associated with the player.
     */
    public AbstractPlayer(String name, Mark mark) {
        this.name = name;
        this.mark = mark;
    }

    /**
     * Gets the name of the player. If the name starts with "-N" or "-S", it's split to extract the actual name.
     * @return the name of the player.
     */
    public String getName() {
        if (this.name.equalsIgnoreCase("-N") || this.name.equalsIgnoreCase("-S")) {
            String[] splitName = name.split("-");
            return splitName[1];
        } else {
            return name;
        }
    }

    /**
     * Gets the mark associated with the player.
     * @return the mark of the player.
     */
    public Mark getMark() {
        return mark;
    }

    /**
     * Abstract method to determine the next move for the player.
     * @param game the current Othello game.
     * @return the move chosen by the player.
     */
    public abstract Move determineMove(Game game);

    /**
     * Returns a string representation of the player.
     * @return a string containing the player's name and mark.
     */
    public String toString() {
        return "Player " + name + " (" + mark + ")";
    }
}