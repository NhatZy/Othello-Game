package networking.client;

import networking.exceptions.*;

import java.net.*;

/**
 * Defines the contract for a client's view in an Othello game. It provides methods for starting, handling user input, displaying messages, and shutting down the client.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see ServerUnavailableException
 * @see UserExitRequestException
 */
public interface ClientView {
    /**
     * Starts the client view, initiating interaction with the user.
     */
    void start();

    /**
     * Retrieves the IP address for the server from the user.
     * @return the IP address of the server.
     */
    InetAddress getIP();

    /**
     * Retrieves user input as a string for the given question.
     * @param question the question prompt to display to the user.
     * @return the user's input as a string.
     */
    String getUserInputAsString(String question);

    /**
     * Retrieves user input as an integer for the given question.
     * @param question the question prompt to display to the user.
     * @return the user's input as an integer.
     */
    int getUserInputAsInt(String question);

    /**
     * Retrieves user input as a boolean for the given question.
     * @param question the question prompt to display to the user.
     * @return the user's input as a boolean.
     */
    boolean getUserInputAsBoolean(String question);

    /**
     * Handles user input by interpreting and processing the provided input string.
     * @param userInput the user's input string.
     * @throws ServerUnavailableException if the server is unavailable.
     * @throws UserExitRequestException if the user requests to exit the program.
     */
    void handleUserInput(String userInput) throws ServerUnavailableException, UserExitRequestException;

    /**
     * Displays a message to the user.
     * @param message the message to be displayed.
     */
    void showMessage(String message);

    /**
     * Prints the help menu, displaying a list of available commands to the user.
     */
    void printHelpMenu();

    /**
     * Shuts down the client view.
     */
    void shutDown();
}