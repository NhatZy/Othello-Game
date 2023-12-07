package networking.server;

import networking.Protocol;
import networking.exceptions.*;
import othellogame.model.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Represents the main server for handling Othello game connections.
 * <p>
 * It listens for incoming client connections, manages user authentication, and facilitates the creation and handling of game sessions between connected clients.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see ServerView
 * @see ClientHandler
 * @see ServerTUI
 * @see UserExitRequestException
 * @see ClientUnavailableException
 * @see Protocol
 * @see HumanPlayer
 * @see Mark
 * @see ServerGame
 * @see AbstractPlayer
 * @see OthelloMove
 */
public class OthelloServer implements Runnable {
    /**
     * The name of the Othello server.
     */
    private static final String SERVER_NAME = "NhatZy's Server";

    /**
     * The view associated with the server, responsible for user interactions and message.
     */
    private ServerView view;

    /**
     * The {@link ServerSocket} used for accepting incoming client connections.
     */
    private ServerSocket serverSocket;

    /**
     * A list of usernames currently logged into the server.
     */
    private List<String> usernamesList = new ArrayList<>();

    /**
     * A list of active {@link ClientHandler} instances, representing connected clients.
     */
    private LinkedList<ClientHandler> clientHandlersList = new LinkedList<>();

    /**
     * Constructs an {@code OthelloServer} instance with a {@link ServerTUI} as the view.
     */
    public OthelloServer() {
        this.view = new ServerTUI();
    }

    /**
     * Sets up the server connection by creating a {@code ServerSocket} on a specified port.
     * @throws UserExitRequestException if the user requests to exit during the setup.
     */
    public void setUpConnection() throws UserExitRequestException {
        this.serverSocket = null;
        while (serverSocket == null) {
            int portNumber = view.getUserInputAsInt("Please input server port number: ");
            try {
                view.showMessage("Attempting to open a socket at port " + portNumber + " ...\n");
                serverSocket = new ServerSocket(portNumber);
                view.showMessage("Server is running on port " + portNumber + "\n");
            } catch (IOException e) {
                view.showMessage("ERROR: The program cannot create a socket on port " + portNumber + "\n");
                boolean tryAgain = view.getUserInputAsBoolean("Do you want to try connecting with the server again?: ");
                if (!tryAgain) {
                    throw new UserExitRequestException("User did confirm their exit\n");
                }
            } catch (IllegalArgumentException e) {
                view.showMessage("ERROR: " + e.getMessage() + "\n");
                boolean tryAgain = view.getUserInputAsBoolean("Do you want to try connecting with the server again?: ");
                if (!tryAgain) {
                    throw new UserExitRequestException("User did confirm their exit\n");
                }
            }
        }
    }

    /**
     * The main run method of the server, continuously accepting client connections and handling incoming requests.
     */
    @Override
    public void run() {
        try {
            setUpConnection();
        } catch (UserExitRequestException e) {
            view.showMessage("The program was closed!\n");
            return;
        }
        while (true) {
            Socket socket;
            try {
                socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                view.showMessage("ERROR: " + e.getMessage() + "\n");
            }
        }
    }

    /**
     * Removes a {@code ClientHandler} from the server's lists upon disconnection.
     * @param clientHandler the {@code ClientHandler} to be removed.
     */
    public void removeClientHandler(ClientHandler clientHandler) {
        synchronized (this.usernamesList) {
            if (usernamesList.contains(clientHandler.getUsername())) {
                usernamesList.remove(clientHandler.getUsername());
            }
        }
        synchronized (this.clientHandlersList) {
            if (clientHandlersList.contains(clientHandler)) {
                clientHandlersList.remove(clientHandler);
            }
        }
    }

    /**
     * Sends a HELLO message to a specific client.
     * @param clientHandler the {@code ClientHandler} to receive the HELLO message.
     * @throws ClientUnavailableException if the client is unavailable.
     */
    public void sendHello(ClientHandler clientHandler) throws ClientUnavailableException {
        clientHandler.sendInputToClient(Protocol.HELLO + Protocol.SEPARATOR + SERVER_NAME);
    }

    /**
     * Handles the login request from a client.
     * @param clientHandler the {@code ClientHandler} requesting login.
     * @param clientUsername the username provided by the client.
     * @throws ClientUnavailableException if the client is unavailable.
     */
    public void sendLogin(ClientHandler clientHandler, String clientUsername) throws ClientUnavailableException {
        if (clientHandler.getUsername() != null) {
            sendError(clientHandler, "You've already logged in as " + clientHandler.getUsername());
            return;
        }
        synchronized (this.usernamesList) {
            if (!usernamesList.contains(clientUsername)) {
                clientHandler.sendInputToClient(Protocol.LOGIN);
                usernamesList.add(clientUsername);
                clientHandler.setUsername(clientUsername);
            } else {
                clientHandler.sendInputToClient(Protocol.ALREADYLOGGEDIN);
            }
        }
    }

    /**
     * Sends the list of usernames currently logged into the server to a specific client.
     * @param clientHandler the {@code ClientHandler} to receive the list.
     * @throws ClientUnavailableException if the client is unavailable.
     */
    public void sendList(ClientHandler clientHandler) throws ClientUnavailableException {
        String answer = Protocol.LIST;
        for (String username : usernamesList) {
            answer += Protocol.SEPARATOR + username;
        }
        clientHandler.sendInputToClient(answer);
    }

    /**
     * Initiates a new Othello game between 2 clients.
     * @param clientHandler1 the {@code ClientHandler} representing the first player.
     * @param clientHandler2 the {@code ClientHandler} representing the second player.
     * @throws ClientUnavailableException if either client is unavailable.
     */
    public void sendNewGame(ClientHandler clientHandler1, ClientHandler clientHandler2) throws ClientUnavailableException {
        Player player1 = new HumanPlayer(clientHandler1.getUsername(), Mark.BLACK);
        Player player2 = new HumanPlayer(clientHandler2.getUsername(), Mark.WHITE);
        ServerGame game = new ServerGame(player1, player2, clientHandler1, clientHandler2);
        clientHandler1.setMark(Mark.BLACK);
        clientHandler2.setMark(Mark.WHITE);
        clientHandler1.setGame(game);
        clientHandler2.setGame(game);
        view.showMessage("A new Othello Game between " + clientHandler1.getUsername() + " and " + clientHandler2.getUsername() + " has started\n");
        String answer = Protocol.NEWGAME + Protocol.SEPARATOR + clientHandler1.getUsername() + Protocol.SEPARATOR + clientHandler2.getUsername();
        clientHandler1.sendInputToClient(answer);
        clientHandler2.sendInputToClient(answer);
    }

    /**
     * Notifies clients about the end of an Othello game and the reason for the game over.
     * @param clientHandler1 the {@code ClientHandler} of the first player.
     * @param clientHandler2 the {@code ClientHandler} of the second player.
     * @param reason the reason for the game over (DISCONNECT, VICTORY, DRAW).
     * @param winner the winner's username (null for DRAW).
     * @throws ClientUnavailableException if either client is unavailable.
     */
    public void sendGameOver(ClientHandler clientHandler1, ClientHandler clientHandler2, String reason, String winner) throws ClientUnavailableException {
        String answer = Protocol.GAMEOVER + Protocol.SEPARATOR;
        if (reason.equals(Protocol.DISCONNECT)) {
            answer += reason + Protocol.SEPARATOR + winner;
            if (clientHandler1 == null) {
                clientHandler2.sendInputToClient(answer);
                clientHandler2.setGame(null);
            } else {
                clientHandler1.sendInputToClient(answer);
                clientHandler1.setGame(null);
            }
        } else if (reason.equals(Protocol.VICTORY)) {
            answer += reason + Protocol.SEPARATOR + winner;
            clientHandler1.sendInputToClient(answer);
            clientHandler2.sendInputToClient(answer);
            clientHandler1.setGame(null);
            clientHandler2.setGame(null);
            view.showMessage("The Othello Game between " + clientHandler1.getUsername() + " and " + clientHandler2.getUsername() + " has ended\n");
        } else if (reason.equals(Protocol.DRAW)) {
            answer += reason;
            clientHandler1.sendInputToClient(answer);
            clientHandler2.sendInputToClient(answer);
            clientHandler1.setGame(null);
            clientHandler2.setGame(null);
            view.showMessage("The Othello Game between " + clientHandler1.getUsername() + " and " + clientHandler2.getUsername() + " has ended\n");
        }
    }

    /**
     * Sends an error message to a specific client.
     * @param clientHandler the {@code ClientHandler} to receive the error message.
     * @param errorDescription the description of the error.
     * @throws ClientUnavailableException if the client is unavailable.
     */
    public void sendError(ClientHandler clientHandler, String errorDescription) throws ClientUnavailableException {
        clientHandler.sendInputToClient(Protocol.ERROR + Protocol.SEPARATOR + errorDescription);
    }

    /**
     * Handles a client's request to join the game queue.
     * @param clientHandler the {@code ClientHandler} making the request.
     * @throws ClientUnavailableException if the client is unavailable.
     */
    public void handleQueue(ClientHandler clientHandler) throws ClientUnavailableException {
        if (clientHandler.getUsername() == null) {
            sendError(clientHandler, "You need to login before performing this action!");
            return;
        }
        if (clientHandler.getGame() != null) {
            sendError(clientHandler, "You cannot join the queue since you're currently in a game!");
            return;
        }
        synchronized (this.clientHandlersList) {
            if (!clientHandlersList.contains(clientHandler)) {
                clientHandlersList.add(clientHandler);
                if (clientHandlersList.size() >= 2) {
                    ClientHandler clientHandler1 = clientHandlersList.removeFirst();
                    ClientHandler clientHandler2 = clientHandlersList.removeFirst();
                    clientHandler1.setMark(Mark.BLACK);
                    clientHandler2.setMark(Mark.WHITE);
                    sendNewGame(clientHandler1, clientHandler2);
                }
            } else {
                clientHandlersList.remove(clientHandler);
            }
        }
    }

    /**
     * Handles a client's move in the Othello game.
     * @param clientHandler the {@code ClientHandler} making the move.
     * @param clientInput the move input from the client.
     * @throws ClientUnavailableException if the client is unavailable.
     */
    public void handleMove(ClientHandler clientHandler, String clientInput) throws ClientUnavailableException {
        if (clientHandler.getUsername() == null) {
            sendError(clientHandler, "You need to login before performing this action!");
        } else {
            ServerGame currentGame = clientHandler.getGame();
            if (currentGame == null) {
                sendError(clientHandler, "You're currently not in a game; therefore, you cannot push a move!");
                return;
            }
            AbstractPlayer player = (AbstractPlayer) currentGame.getTurn();
            if (player.getMark() != clientHandler.getMark()) {
                sendError(clientHandler, "This is not your turn. Please wait for another player to perform the move first!");
                return;
            }
            String[] splitClientInput = clientInput.split(Protocol.SEPARATOR);
            try {
                int index = Integer.parseInt(splitClientInput[1]);
                Move currentMove = new OthelloMove(player.getMark(), index);
                if (index == 64) {
                    if (currentGame.getValidMoves().isEmpty()) {
                        clientHandler.getGame().getClientHandler1().sendInputToClient(Protocol.MOVE + Protocol.SEPARATOR + index);
                        clientHandler.getGame().getClientHandler2().sendInputToClient(Protocol.MOVE + Protocol.SEPARATOR + index);
                        doMove(currentMove, currentGame);
                    } else {
                        sendError(clientHandler, "You still have possible move(s); therefore, you cannot skip your turn!");
                    }
                } else {
                    if (currentGame.isValidMove(currentMove)) {
                        clientHandler.getGame().getClientHandler1().sendInputToClient(Protocol.MOVE + Protocol.SEPARATOR + index);
                        clientHandler.getGame().getClientHandler2().sendInputToClient(Protocol.MOVE + Protocol.SEPARATOR + index);
                        doMove(currentMove, currentGame);
                    } else {
                        sendError(clientHandler, splitClientInput[1] + " is an invalid move!");
                    }
                }
            } catch (NumberFormatException e) {
                sendError(clientHandler, "This is not a number. Please enter a whole number for a move!");
            }
        }
    }

    /**
     * Executes a move in the Othello game and handles the game state.
     * @param move the move to be executed.
     * @param game the Othello game instance.
     * @throws ClientUnavailableException if a client is unavailable.
     */
    public void doMove(Move move, ServerGame game) throws ClientUnavailableException {
        int index = ((OthelloMove) move).getIndex();
        if (index == 64) {
            game.swapTurn();
        } else {
            game.doMove(move);
        }
        if (game.isGameOver()) {
            if (game.getWinner() == Mark.BLACK) {
                sendGameOver(game.getClientHandler1(), game.getClientHandler2(), Protocol.VICTORY, game.getClientHandler1().getUsername());
            } else if (game.getWinner() == Mark.WHITE) {
                sendGameOver(game.getClientHandler1(), game.getClientHandler2(), Protocol.VICTORY, game.getClientHandler2().getUsername());
            } else {
                sendGameOver(game.getClientHandler1(), game.getClientHandler2(), Protocol.DRAW, null);
            }
        }
    }

    /**
     * Handles the disconnection of a client and takes appropriate actions.
     * @param disconnectedClient the {@code ClientHandler} representing the disconnected client.
     */
    public void handleDisconnection(ClientHandler disconnectedClient) {
        String disconnectedClientUsername = disconnectedClient.getUsername();
        clientHandlersList.remove(disconnectedClient);
        ServerGame currentGame = disconnectedClient.getGame();
        ClientHandler anotherClient;
        if (currentGame != null) {
            try {
                if (disconnectedClient == currentGame.getClientHandler1()) {
                    anotherClient = currentGame.getClientHandler2();
                    sendGameOver(null, anotherClient, Protocol.DISCONNECT, anotherClient.getUsername());
                    view.showMessage("The Othello Game between " + disconnectedClientUsername + " and " + anotherClient.getUsername() + " has ended\n");
                } else {
                    anotherClient = currentGame.getClientHandler1();
                    sendGameOver(anotherClient, null, Protocol.DISCONNECT, anotherClient.getUsername());
                    view.showMessage("The Othello Game between " + anotherClient.getUsername() + " and " + disconnectedClientUsername + " has ended\n");
                }
            } catch (ClientUnavailableException e) {}
        }
    }

    /**
     * The main entry point for starting the Othello server.
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        new Thread(new OthelloServer()).start();
    }
}