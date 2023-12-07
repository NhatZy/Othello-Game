package networking.client;

import networking.Protocol;
import networking.exceptions.*;

import java.io.*;
import java.net.*;

/**
 * The text-based user interface (TUI) for the Othello game client.
 * <p>
 * This class provides methods to interact with the user through the console, handle user input, and communicate with the Othello server using the provided {@link OthelloClient}.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see ClientView
 * @see OthelloClient
 * @see ServerUnavailableException
 * @see UserExitRequestException
 * @see Protocol
 */
public class ClientTUI implements ClientView {
    /**
     * The Othello client associated with this TUI.
     */
    private OthelloClient client;

    /**
     * Reader to read input from the console.
     */
    private BufferedReader consoleReader;

    /**
     * Writer to print output to the console.
     */
    private PrintWriter consoleWriter;

    /**
     * A flag indicating whether the user has requested to exit the program.
     */
    private boolean exit = false;

    /**
     * A constant string for formatting console output.
     */
    public static final String FORMATTING = "%-17s %20s";

    /**
     * Constructs a new {@code ClientTUI} instance associated with the provided {@code OthelloClient}.
     * @param client the {@code OthelloClient} associated with this TUI.
     */
    public ClientTUI(OthelloClient client) {
        this.client = client;
        this.consoleReader = new BufferedReader(new InputStreamReader(System.in));
        this.consoleWriter = new PrintWriter(System.out);
    }

    /**
     * Starts the client TUI, continuously reading user input from the console and handling it accordingly.
     */
    @Override
    public void start() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (!this.exit) {
            String userInput = null;
            try {
                userInput = reader.readLine();
            } catch (IOException e1) {
                showMessage("ERROR: Unfortunately, the program cannot read your input. Please try again!\n");
            }
            try {
                handleUserInput(userInput);
            } catch (ServerUnavailableException e) {
                showMessage("Unfortunately, the server is currently unavailable!\n");
                client.closeConnection(false);
            } catch (UserExitRequestException e) {
                this.exit = true;
                this.exit = client.closeConnection(true);
            }
        }
        this.exit = false;
    }

    /**
     * Retrieves the IP address of the server from the user input.
     * @return the InetAddress representing the server's IP address.
     */
    @Override
    public InetAddress getIP() {
        InetAddress address = null;
        while (address == null) {
            String userInput = getUserInputAsString("Please input server address: ");
            try {
                address = InetAddress.getByName(userInput);
            } catch (UnknownHostException e) {
                showMessage("ERROR: Host " + userInput + " is unknown!\n");
            }
        }
        return address;
    }

    /**
     * Retrieves user input as a string for the given question.
     * @param question the question prompt displayed to the user.
     * @return the user's input as a string.
     */
    @Override
    public String getUserInputAsString(String question) {
        showMessage(question);
        try {
            return consoleReader.readLine();
        } catch (IOException e) {
            showMessage("ERROR: Unfortunately, the program cannot read the input. Please try again!\n");
            return getUserInputAsString(question);
        }
    }

    /**
     * Retrieves user input as an integer for the given question.
     * @param question the question prompt displayed to the user.
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
            showMessage("ERROR: Unfortunately, the program cannot read the input. Please try again!\n");
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
     * Retrieves user input as a boolean for the given question.
     * @param question the question prompt displayed to the user.
     * @return the user's input as a boolean.
     */
    @Override
    public boolean getUserInputAsBoolean(String question) {
        String userInputAsBoolean;
        showMessage(question);
        try {
            userInputAsBoolean = consoleReader.readLine().toLowerCase();
        } catch (IOException e) {
            showMessage("ERROR: Unfortunately, the program cannot read the input. Please try again!\n");
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
     * Handles the user input, invoking appropriate methods based on the provided input.
     * @param userInput the user's input string.
     * @throws ServerUnavailableException if the server is unavailable during the operation.
     * @throws UserExitRequestException if the user requests to exit the program.
     */
    @Override
    public void handleUserInput(String userInput) throws ServerUnavailableException, UserExitRequestException {
        String[] splitInput = userInput.split("\\s+");
        String mainCommand = splitInput[0];
        int splitInputLength = splitInput.length;
        switch (mainCommand.toUpperCase()) {
            case Protocol.LOGIN:
                if (splitInputLength < 2) {
                    showMessage("Please add a username to login!\n");
                } else if (splitInputLength == 2) {
                    client.sendLogin(splitInput[1]);
                } else {
                    showMessage("Unfortunately, backspace is not allowed here. Please try again!\n");
                }
                break;
            case Protocol.LIST:
                if (splitInputLength == 1) {
                    client.sendList();
                } else {
                    showMessage("Invalid input for command " + Protocol.LIST + ". Please try again!\n");
                }
                break;
            case Protocol.QUEUE:
                if (splitInputLength == 1) {
                    client.sendQueue();
                } else {
                    showMessage("Invalid input for command " + Protocol.QUEUE + ". Please try again!\n");
                }
                break;
            case Protocol.MOVE:
                if (splitInputLength < 2) {
                    showMessage("Please add an index for the move to push!\n");
                } else if (splitInputLength == 2) {
                    try {
                        int index = Integer.parseInt(splitInput[1]);
                        client.sendMove(index);
                    } catch (NumberFormatException e) {
                        showMessage("Please enter a whole number for a move!\n");
                    }
                } else {
                    showMessage("Invalid input for command " + Protocol.MOVE + ". Please try again!\n");
                }
                break;
            case "HINT":
                if (splitInputLength == 1) {
                    client.doHint();
                } else {
                    showMessage("Invalid input for command HINT. Please try again!\n");
                }
                break;
            case "HELP":
                if (splitInputLength == 1) {
                    printHelpMenu();
                } else {
                    showMessage("Invalid input for command HELP. Please try again!\n");
                }
                break;
            case "EXIT":
                if (splitInputLength == 1) {
                    throw new UserExitRequestException("User did confirm their exit");
                } else {
                    showMessage("Invalid input for command EXIT. Please try again!\n");
                }
                break;
            default:
                showMessage("Command " + mainCommand + " is unknown!\n");
                printHelpMenu();
        }
    }

    /**
     * Displays a message to the user in the console.
     * @param message the message to be displayed.
     */
    @Override
    public void showMessage(String message) {
        consoleWriter.print(message);
        consoleWriter.flush();
    }

    /**
     * Prints the help menu, providing information about available commands to the user.
     */
    @Override
    public void printHelpMenu() {
        final String helpMenu = String.format("Here is the list of all available commands that you can use: \n") +
                String.format(FORMATTING, "LOGIN (username)", " - to claim a username on the server\n") +
                String.format(FORMATTING, "LIST", " - to request a list of clients who are currently logged into the server\n") +
                String.format(FORMATTING, "QUEUE", " - to indicate that you want to join or leave the queue\n") +
                String.format(FORMATTING, "MOVE (index)", " - to indicate which index you want to push during the game\n") +
                String.format(FORMATTING, "HINT", " - to request a possible valid move as a hint\n") +
                String.format(FORMATTING, "HELP", " - to print this help menu\n") +
                String.format(FORMATTING, "EXIT", " - to exit the program");
        showMessage(helpMenu + "\n");
    }

    /**
     * Signals the TUI to shut down, setting the exit flag to true.
     */
    @Override
    public void shutDown() {
        this.exit = true;
    }

    /**
     * Replaces the current console reader with the provided reader.
     * @param reader the new {@code BufferedReader} to replace the current console reader.
     * @return the original console reader.
     */
    //This method is used for testing purposes.
    public BufferedReader replaceReader(BufferedReader reader) {
        BufferedReader originalReader = consoleReader;
        consoleReader = reader;
        return originalReader;
    }
}