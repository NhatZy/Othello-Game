package networking.server;

import java.io.*;

/**
 * Text-based user interface (TUI) implementation for the Othello game server.
 * <p>
 * This class provides methods to interact with the user through the console.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see ServerView
 */
public class ServerTUI implements ServerView {
    /**
     * The {@code BufferedReader} used for reading input from the console.
     */
    private BufferedReader consoleReader;

    /**
     * The {@code PrintWriter} used for writing output to the console.
     */
    private PrintWriter consoleWriter;

    /**
     * Constructs a {@code ServerTUI} instance with the necessary input and output streams.
     */
    public ServerTUI() {
        this.consoleReader = new BufferedReader(new InputStreamReader(System.in));
        this.consoleWriter = new PrintWriter(System.out);
    }

    /**
     * Prompts the user with a question and retrieves their input as a string.
     * @param question the question to prompt the user.
     * @return the user's input as a string.
     */
    @Override
    public String getUserInputAsString(String question) {
        String stringInput;
        showMessage(question);
        try {
            stringInput = consoleReader.readLine();
        } catch (IOException e) {
            showMessage("ERROR: Unfortunately, the program cannot read your input. Please try again!\n");
            return getUserInputAsString(question);
        }
        return stringInput;
    }

    /**
     * Prompts the user with a question and retrieves their input as an integer.
     * @param question the question to prompt the user.
     * @return the user's input as an integer.
     */
    @Override
    public int getUserInputAsInt(String question) {
        String stringInput;
        int intInput;
        showMessage(question);
        try {
            stringInput = consoleReader.readLine();
        } catch (IOException e) {
            showMessage("ERROR: Unfortunately, the program cannot read your input. Please try again!\n");
            return getUserInputAsInt(question);
        }
        try {
            intInput = Integer.parseInt(stringInput);
        } catch (NumberFormatException e) {
            showMessage("ERROR: This is not a number. Please try again!\n");
            return getUserInputAsInt(question);
        }
        return intInput;
    }

    /**
     * Prompts the user with a question and retrieves their input as a boolean.
     * @param question the question to prompt the user.
     * @return the user's input as a boolean.
     */
    @Override
    public boolean getUserInputAsBoolean(String question) {
        String userInputAsBoolean;
        showMessage(question);
        try {
            userInputAsBoolean = consoleReader.readLine().toLowerCase();
        } catch (IOException e) {
            showMessage("ERROR: Unfortunately, the program cannot read your input. Please try again!\n");
            return getUserInputAsBoolean(question);
        }
        if (userInputAsBoolean.equals("yes")) {
            return true;
        } else if (userInputAsBoolean.equals("no")) {
            return false;
        } else {
            showMessage("ERROR: You can only answer with just \"yes\" or \"no\". Please try again!\n");
            return getUserInputAsBoolean(question);
        }
    }

    /**
     * Displays a message to the user.
     * @param message the message to be displayed.
     */
    @Override
    public void showMessage(String message) {
        consoleWriter.print(message);
        consoleWriter.flush();
    }
}