package networking.client;

import networking.Protocol;
import networking.exceptions.*;
import networking.exceptions.ProtocolException;
import othellogame.ai.*;
import othellogame.model.*;
import othellogame.ui.*;

import java.io.*;
import java.net.*;

/**
 * Represents a client for the Othello game.
 * It manages the communication with the server, user interaction, and game logic.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see ClientView
 * @see ServerHandler
 * @see Game
 * @see Player
 * @see UserExitRequestException
 * @see ServerUnavailableException
 * @see TUI
 * @see Protocol
 * @see Move
 * @see OthelloMove
 * @see AbstractPlayer
 * @see ComputerPlayer
 * @see ProtocolException
 * @see Mark
 * @see HumanPlayer
 * @see NaiveStrategy
 * @see SmartStrategy
 */
public class OthelloClient {
    /**
     * The name of the client handler (not the client's username).
     */
    private static final String CLIENTHANDLER_NAME = "NhatZy's Client";

    /**
     * The view associated with the client for user interaction.
     */
    private ClientView view;

    /**
     * The socket for communication with the server.
     */
    private Socket socket;

    /**
     * The server handler responsible for handling server communication.
     */
    public ServerHandler serverHandler;

    /**
     * The Othello game instance.
     */
    private Game game;

    /**
     * The main player representing the client.
     */
    private Player mainPlayer;

    /**
     * Another player participating in the game.
     */
    private Player anotherPlayer;

    /**
     * The username of another player.
     */
    private String anotherPlayerUsername;

    /**
     * A flag indicating whether the client is logged in.
     */
    private boolean loggedIn = false;

    /**
     * The username of the client.
     */
    private String username;

    /**
     * A flag indicating whether the client is in the queue.
     */
    private boolean inQueue = false;

    /**
     * A flag indicating whether the client wants to leave the queue.
     */
    private boolean leaveQueue = false;

    /**
     * A flag indicating whether the client is in an active game.
     */
    private boolean inGame = false;

    /**
     * A flag indicating whether the client is using a bot to play the game.
     */
    private boolean botUsing = false;

    /**
     * The choice of bot type made by the client.
     */
    private String botChoice = null;

    /**
     * A flag indicating whether it's the first move for a bot.
     */
    private boolean firstBotMove = true;

    /**
     * Constructs an {@code OthelloClient} instance with the associated view.
     */
    public OthelloClient() {
        this.view = new ClientTUI(this);
    }

    /**
     * Creates a connection to the server and initializes the server handler.
     * @throws UserExitRequestException if the user chooses to exit during connection setup.
     */
    public void createConnection() throws UserExitRequestException {
        while (this.socket == null) {
            InetAddress address = view.getIP();
            int portNumber = view.getUserInputAsInt("Please input server port number: ");
            try {
                view.showMessage("Attempting to connect to server " + address + " with port " + portNumber + " ...\n");
                this.socket = new Socket(address, portNumber);
                this.serverHandler = new ServerHandler(socket, this);
                new Thread(serverHandler).start();
            } catch (IOException e) {
                view.showMessage("ERROR: Unfortunately, the program cannot create a socket on server " + address + " with port " + portNumber + "!\n");
                throw new UserExitRequestException("User exited!\n");
            }
        }
    }

    /**
     * Retrieves the current game instance.
     * @return the current game instance.
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Checks if it's the player's turn.
     * @return true if it's the player's turn, false otherwise.
     */
    public boolean isPlayerTurn() {
        return this.mainPlayer == game.getTurn();
    }

    /**
     * Displays a message on the user's screen using the associated view.
     * @param serverOutput the message to be displayed.
     */
    public void showToUserScreen(String serverOutput) {
        view.showMessage(serverOutput);
    }

    /**
     * Starts the Othello client, prompting the user to choose between online and offline play.
     */
    public void start() {
        boolean onlinePlaying = view.getUserInputAsBoolean("Do you want to play the game online? (Please input \"yes\" for online Othello Game and \"no\" for offline one): ");
        if (onlinePlaying) {
            boolean connectingToServer = true;
            while (connectingToServer) {
                try {
                    createConnection();
                    sendHello();
                    view.printHelpMenu();
                    view.start();
                } catch (ServerUnavailableException e) {
                    view.showMessage("Unfortunately, the program cannot connect to the server\n");
                } catch (UserExitRequestException e) {}
                connectingToServer = view.getUserInputAsBoolean("Do you want to connect to a new server? (Please input \"yes\" or \"no\"): ");
            }
            view.showMessage("Thank you so much for playing my Othello Game. I hope to see you later and a nice day ahead as well!\n");
            view.shutDown();
        } else {
            TUI object = new TUI();
            object.run();
        }
    }

    /**
     * Sends a HELLO message to the server.
     * @throws ServerUnavailableException if the server is currently unavailable.
     */
    public void sendHello() throws ServerUnavailableException {
        serverHandler.sendInputToServer(Protocol.HELLO + Protocol.SEPARATOR + CLIENTHANDLER_NAME);
    }

    /**
     * Sends a LOGIN message to the server with the specified username.
     * @param username the username to log in with.
     * @throws ServerUnavailableException if the server isn't available.
     */
    public void sendLogin(String username) throws ServerUnavailableException {
        if (this.loggedIn) {
            view.showMessage("You're already logged into the server!\n");
        } else {
            if (username.equals("")) {
                this.username = view.getUserInputAsString("What is your username?: ");
            } else {
                this.username = username;
            }
            serverHandler.sendInputToServer(Protocol.LOGIN + Protocol.SEPARATOR + username);
        }
    }

    /**
     * Sends a LIST message to the server to request a list of currently logged-in clients.
     * @throws ServerUnavailableException if the server isn't available.
     */
    public void sendList() throws ServerUnavailableException {
        if (this.loggedIn) {
            serverHandler.sendInputToServer(Protocol.LIST);
        } else {
            view.showMessage("You need to login before performing this action!\n");
        }
    }

    /**
     * Sends a QUEUE message to the server, indicating the client's desire to join or leave the queue.
     * @throws ServerUnavailableException if the server isn't available.
     */
    public void sendQueue() throws ServerUnavailableException {
        if (this.loggedIn) {
            if (!this.inGame) {
                if (this.inQueue) {
                    this.leaveQueue = view.getUserInputAsBoolean("You've been already in the queue. Are you sure you want to leave the queue?: ");
                    if (leaveQueue) {
                        serverHandler.sendInputToServer(Protocol.QUEUE);
                        this.inQueue = false;
                        this.botUsing = false;
                        this.botChoice = null;
                        view.showMessage("You successfully left the queue\n");
                    } else {
                        this.inQueue = true;
                        view.showMessage("You're still in the queue\n");
                    }
                } else {
                    this.botUsing = view.getUserInputAsBoolean("Do you want bot to play this game for you? (Please input \"yes\" or \"no\"): ");
                    if (botUsing) {
                        do {
                            this.botChoice = view.getUserInputAsString("Do you want the NAIVE or SMART bot to play this game? (Please input \"-N\" to choose NAIVE bot or \"-S\" to choose SMART bot): ");
                            if (botChoice.equalsIgnoreCase("-N")) {
                                this.botChoice = "-N";
                            } else if (botChoice.equalsIgnoreCase("-S")) {
                                this.botChoice = "-S";
                            } else {
                                view.showMessage("The bot " + botChoice + " is unknown. You can only answer with just \"-N\" or \"-S\". Please try again!\n");
                            }
                        } while (!this.botChoice.equalsIgnoreCase("-N") && !this.botChoice.equalsIgnoreCase("-S"));
                    }
                    view.showMessage("You successfully joined the queue. To leave the queue, input \"QUEUE\" again\n");
                    serverHandler.sendInputToServer(Protocol.QUEUE);
                    this.inQueue = true;
                }
            } else {
                view.showMessage("You cannot join the queue since you're currently in a game!\n");
            }
        } else {
            view.showMessage("You need to login before performing this action!\n");
        }
    }

    /**
     * Sends a MOVE message to the server with the specified index.
     * @param index the index indicating the move to be made.
     * @throws ServerUnavailableException if the server isn't available.
     */
    public void sendMove(int index) throws ServerUnavailableException {
        if (this.inGame) {
            if (isPlayerTurn()) {
                String answer = Protocol.MOVE + Protocol.SEPARATOR + index;
                serverHandler.sendInputToServer(answer);
            } else {
                view.showMessage("This is not your turn. Please wait for another player to perform the move first!\n");
            }
        } else {
            view.showMessage("You're currently not in a game; therefore, you cannot push a move!\n");
        }
    }

    /**
     * Initiates the execution of a move in the game.
     * @param index the index indicating the move to be made.
     * @throws ServerUnavailableException if the server isn't available.
     */
    public void doMove(int index) throws ServerUnavailableException {
        if (!game.isGameOver()) {
            if (index == 64) {
                game.swapTurn();
            } else {
                Move currentMove;
                if (isPlayerTurn()) {
                    currentMove = new OthelloMove(((AbstractPlayer) mainPlayer).getMark(), index);
                } else {
                    currentMove = new OthelloMove(((AbstractPlayer) anotherPlayer).getMark(), index);
                }
                game.doMove(currentMove);
                view.showMessage(game.getBoard().toIndexString() + "\n");
            }
            if (isPlayerTurn()) {
                if (!game.isGameOver()) {
                    view.showMessage("This is your turn\n");
                    if (mainPlayer instanceof ComputerPlayer) {
                        if (!game.getValidMoves().isEmpty()) {
                            if (!this.firstBotMove) {
                                OthelloMove botMove = (OthelloMove) ((ComputerPlayer) mainPlayer).determineMove(game);
                                int botIndex = botMove.getIndex();
                                sendMove(botIndex);
                            } else {
                                OthelloMove botMove = (OthelloMove) ((ComputerPlayer) mainPlayer).determineMove(game);
                                int botIndex = botMove.getIndex();
                                sendMove(botIndex);
                                this.firstBotMove = false;
                            }
                        } else {
                            sendMove(64);
                        }
                    } else {
                        if (game.getValidMoves().isEmpty()) {
                            view.showMessage("There is no more valid move for you to make. Please input \"MOVE 64\" to skip your turn\n");
                        }
                    }
                }
            } else {
                if (!game.isGameOver()) {
                    view.showMessage("Waiting for another player's move ...\n");
                }
            }
        }
    }

    /**
     * Requests a hint from the server, suggesting a valid move for the current player.
     */
    public void doHint() {
        if (this.inGame) {
            if (isPlayerTurn()) {
                OthelloMove randomMove = (OthelloMove) game.getValidMoves().get((int) Math.floor(Math.random() * game.getValidMoves().size()));
                int randomIndex = randomMove.getIndex();
                view.showMessage("You can choose this move: " + randomIndex + "\n");
            } else {
                view.showMessage("This is not your turn. Please wait for another player to perform the move first!\n");
            }
        } else {
            view.showMessage("You're currently not in a game; therefore, you cannot perform this action!\n");
        }
    }

    /**
     * Handles the response from the server after sending a HELLO message.
     * @param serverOutput the server's response containing connection information.
     * @throws ProtocolException if the server's output doesn't correspond to the protocol.
     */
    public void handleHello(String serverOutput) throws ProtocolException {
        String[] splitServerOutput = serverOutput.split(Protocol.SEPARATOR);
        int splitServerOutputLength = splitServerOutput.length;
        if (splitServerOutputLength == 2) {
            view.showMessage("You're successfully connected to " + splitServerOutput[1] + "\n");
        } else {
            throw new ProtocolException("Server's output doesn't correspond to the protocol. Please try connecting to a new server!\n");
        }
    }

    /**
     * Handles the response from the server after sending a LOGIN message.
     * @param serverOutput the server's response indicating the success or failure of the login attempt.
     */
    public void handleLogin(String serverOutput) {
        if (serverOutput.equals(Protocol.LOGIN)) {
            view.showMessage("You're successfully logged in as " + this.username + "\n");
            this.loggedIn = true;
        } else if (serverOutput.equals(Protocol.ALREADYLOGGEDIN)) {
            view.showMessage("The username \"" + this.username + "\" is already claimed. Please try logging in again with a different username!\n");
        }
    }

    /**
     * Handles the response from the server after sending a LIST message.
     * @param serverOutput the server's response containing a list of currently logged-in clients.
     */
    public void handleList(String serverOutput) {
        String[] usernamesList = serverOutput.split(Protocol.SEPARATOR);
        String list = "A list of clients who are currently logged into the server: ";
        for (int i = 1; i < usernamesList.length; i++) {
            list = list + "\n" + i + ". " + usernamesList[i];
        }
        view.showMessage(list + "\n");
    }

    /**
     * Handles the response from the server after sending a QUEUE message.
     * @param serverOutput the server's response containing information about the new game.
     * @throws ProtocolException if the server's output doesn't correspond to the protocol.
     * @throws ServerUnavailableException if the server isn't available.
     */
    public void handleNewGame(String serverOutput) throws ProtocolException, ServerUnavailableException {
        String[] splitServerOutput = serverOutput.split(Protocol.SEPARATOR);
        if (splitServerOutput.length == 3) {
            Mark mainMark;
            this.inQueue = false;
            this.inGame = true;
            this.mainPlayer = null;
            if (splitServerOutput[1].equals(this.username)) {
                mainMark = Mark.BLACK;
                this.anotherPlayer = new HumanPlayer(splitServerOutput[2], mainMark.otherMark());
                this.anotherPlayerUsername = splitServerOutput[2];
                view.showMessage("New Othello Game with " + splitServerOutput[2] + " has started\n");
            } else {
                mainMark = Mark.WHITE;
                this.anotherPlayer = new HumanPlayer(splitServerOutput[1], mainMark.otherMark());
                this.anotherPlayerUsername = splitServerOutput[1];
                view.showMessage("New Othello Game with " + splitServerOutput[1] + " has started\n");
            }
            if (this.botUsing) {
                if (this.botChoice.equals("-N")) {
                    this.mainPlayer = new ComputerPlayer(new NaiveStrategy(), mainMark);
                } else {
                    this.mainPlayer = new ComputerPlayer(new SmartStrategy(), mainMark);
                }
            } else {
                this.mainPlayer = new HumanPlayer(this.username, mainMark);
            }
            if (mainMark == Mark.BLACK) {
                this.game = new OthelloGame(mainPlayer, anotherPlayer);
                view.showMessage("This is your turn\n");
                view.showMessage(game.getBoard().toIndexString() + "\n");
                if (mainPlayer instanceof ComputerPlayer) {
                    OthelloMove botMove = (OthelloMove) ((ComputerPlayer) mainPlayer).determineMove(game);
                    int botIndex = botMove.getIndex();
                    sendMove(botIndex);
                    this.firstBotMove = false;
                }
            } else {
                this.game = new OthelloGame(anotherPlayer, mainPlayer);
                view.showMessage(game.getBoard().toIndexString() + "\n");
                view.showMessage("Waiting for another player's move ...\n");
            }
        } else {
            throw new ProtocolException("Server's output doesn't correspond to the protocol. Please try connecting to a new server!\n");
        }
    }

    /**
     * Handles the response from the server after receiving a MOVE message.
     * @param serverOutput the server's response containing information about the move made the players.
     * @throws ProtocolException if the server's output doesn't correspond to the protocol.
     * @throws ServerUnavailableException if the server isn't available.
     */
    public void handleMove(String serverOutput) throws ProtocolException, ServerUnavailableException {
        String[] spitServerOutput = serverOutput.split(Protocol.SEPARATOR);
        if (spitServerOutput.length == 2) {
            try {
                int index = Integer.parseInt(spitServerOutput[1]);
                doMove(index);
            } catch (NumberFormatException e) {
                throw new ProtocolException("Server's output doesn't correspond to the protocol. Please try connecting to a new server!\n");
            }
        } else {
            throw new ProtocolException("Server's output doesn't correspond to the protocol. Please try connecting to a new server!\n");
        }
    }

    /**
     * Handles the response from the server after receiving a GAMEOVER message.
     * @param serverOutput the server's response containing information about the end of the game.
     * @throws ProtocolException if the server's output doesn't correspond to the protocol.
     */
    public void handleGameOver(String serverOutput) throws ProtocolException {
        String[] splitServerOutput = serverOutput.split(Protocol.SEPARATOR);
        switch (splitServerOutput[1]) {
            case Protocol.DISCONNECT:
                if (splitServerOutput.length == 3) {
                    view.showMessage("Another player has disconnected, so the game is won by you. Congratulations!\n");
                    view.showMessage("Thank you so much for playing my Othello Game. If you wish to continue a new game, please input \"QUEUE\" to join the queue\n");
                } else {
                    throw new ProtocolException("Server's output doesn't correspond to the protocol. Please try connecting to a new server!\n");
                }
                break;
            case Protocol.VICTORY:
                if (splitServerOutput.length == 3) {
                    String winner = splitServerOutput[2];
                    if (((AbstractPlayer) mainPlayer).getMark() == Mark.BLACK) {
                        view.showMessage("You get " + game.countBlack() + " discs\n");
                        view.showMessage("Player " + anotherPlayerUsername + " gets " + game.countWhite() + " discs\n");
                    } else {
                        view.showMessage("Player " + anotherPlayerUsername + " gets " + game.countBlack() + " discs\n");
                        view.showMessage("You get " + game.countWhite() + " discs\n");
                    }
                    if (winner.equals(this.username)) {
                        view.showMessage("The game ended with the winner is you. Congratulations!\n");
                    } else {
                        view.showMessage("So sorry, the game ended with the winner is " + anotherPlayerUsername + ". Better luck next time!\n");
                    }
                    view.showMessage("Thank you so much for playing my Othello Game. If you wish to continue a new game, please input \"QUEUE\" to join the queue\n");
                } else {
                    throw new ProtocolException("Server's output doesn't correspond to the protocol. Please try connecting to a new server!\n");
                }
                break;
            case Protocol.DRAW:
                view.showMessage("The game ended in a draw, it was a good effort though!\n");
                view.showMessage("Thank you so much for playing my Othello Game. If you wish to continue a new game, please input \"QUEUE\" to join the queue\n");
                break;
        }
        this.mainPlayer = null;
        this.anotherPlayer = null;
        this.anotherPlayerUsername = null;
        this.inGame = false;
        this.botUsing = false;
        this.botChoice = null;
        this.firstBotMove = true;
    }

    /**
     * Closes the connection to the server, allowing the user to confirm the action.
     * @param canCloseConnection indicates whether the connection can be closed immediately.
     * @return true if the connection is successfully closed, false otherwise.
     */
    public boolean closeConnection(boolean canCloseConnection) {
        boolean closeConnection = true;
        if (canCloseConnection) {
            closeConnection = view.getUserInputAsBoolean("Are you sure you want to close the connection with the server?: ");
        }
        if (closeConnection) {
            view.showMessage("Thank you so much for using my Othello Game Server. I hope to see you later and a nice day ahead as well!\n");
            completelyShutDown();
            try {
                this.serverHandler.clearConnection();
                this.socket = null;
                this.serverHandler = null;
                this.game = null;
                this.mainPlayer = null;
                this.anotherPlayer = null;
                this.anotherPlayerUsername = null;
                this.loggedIn = false;
                this.username = null;
                this.inQueue = false;
                this.leaveQueue = false;
                this.inGame = false;
                this.botUsing = false;
                this.botChoice = null;
                this.firstBotMove = true;
                if (!canCloseConnection) {
                    view.shutDown();
                }
                return true;
            } catch (IOException e) {
                view.showMessage("Oh no! The program cannot close the connection. Please try again after a few seconds!\n");
                return false;
            }
        } else {
            view.showMessage("Exiting process is canceled\n");
            return false;
        }
    }

    /**
     * Shuts down the client completely, exiting the program.
     */
    public void completelyShutDown() {
        System.exit(0);
    }

    /**
     * Sets the server socket for the client.
     * @param socket the server socket to be set.
     */
    //This method is used for testing purposes.
    public void setServerSocket(Socket socket) {
        try {
            this.serverHandler = new ServerHandler(socket, this);
            new Thread(serverHandler).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the client's view.
     * @return the associated {@link ClientView} instance.
     */
    //This method is used for testing purposes.
    public ClientView getClientView() {
        return this.view;
    }

    /**
     * Sets the username for the logged-in client.
     * @param username the username to be set.
     */
    //This method is used for testing purposes.
    public void setLoggedInAs(String username) {
        this.username = username;
    }

    /**
     * Sets up the game with the specified parameters.
     * @param anotherPlayerUsername the username of the other player.
     * @param mainPlayerMark the mark assigned to the main player.
     * @param anotherPlayerMark the mark assigned to the other player.
     */
    //This method is used for testing purposes.
    public void setGame(String anotherPlayerUsername, Mark mainPlayerMark, Mark anotherPlayerMark) {
        this.mainPlayer = new HumanPlayer(username, mainPlayerMark);
        this.anotherPlayer = new HumanPlayer(anotherPlayerUsername, anotherPlayerMark);
        this.game = new OthelloGame(mainPlayer, anotherPlayer);
        this.anotherPlayerUsername = anotherPlayerUsername;
    }

    /**
     * The main entry point for the Othello client application.
     * @param args the command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        (new OthelloClient()).start();
    }
}