package networking.server;

/**
 * Interface representing the view for the Othello game server.
 * <p>
 * This interface defines methods for interacting with the user and displaying messages.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 */
public interface ServerView {
    /**
     * Prompts the user with a question and retrieves their input as a string.
     * @param question the question to prompt the user.
     * @return the user's input as a string.
     */
    String getUserInputAsString(String question);

    /**
     * Prompts the user with a question and retrieves their input as an integer.
     * @param question the question to prompt the user.
     * @return the user's input as an integer.
     */
    int getUserInputAsInt(String question);

    /**
     * Prompts the user with a question and retrieves their input as a boolean.
     * @param question the question to prompt the user.
     * @return the user's input as a boolean.
     */
    boolean getUserInputAsBoolean(String question);

    /**
     * Displays a message to the user.
     * @param message the message to be displayed.
     */
    void showMessage(String message);
}