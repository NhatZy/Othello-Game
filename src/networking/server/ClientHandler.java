package networking.server;

import networking.Protocol;
import networking.exceptions.*;
import othellogame.model.*;

import java.io.*;
import java.net.*;

/**
 * Represents a client handler responsible for managing communication with an individual client in the Othello game server.
 * <p>
 * Each instance of this class is associated with a specific client, handling input and output streams for communication.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see OthelloServer
 * @see ServerGame
 * @see Mark
 * @see ClientUnavailableException
 */
public class ClientHandler implements Runnable {
    /**
     * The {@code BufferedReader} used for reading input from the client's socket.
     */
    private BufferedReader reader;

    /**
     * The {@code BufferedWriter} used for writing output to the client's socket.
     */
    private BufferedWriter writer;

    /**
     * The socket associated with the client, used for communication.
     */
    private Socket socket;

    /**
     * The {@link OthelloServer} instance managing the overall game.
     */
    private OthelloServer server;

    /**
     * The {@link ServerGame} instance representing the game the client is part of.
     */
    private ServerGame game;

    /**
     * The username associated with the client. Null if not yet set.
     */
    private String username = null;

    /**
     * The Othello game mark (BLACK or WHITE) assigned to the client.
     */
    private Mark mark;

    /**
     * Constructs a {@code ClientHandler} instance with the specified socket and {@code OthelloServer}.
     * @param socket the socket associated with the client.
     * @param server the {@code OthelloServer} managing the game.
     * @throws IOException if an I/O error occurs while creating input and output streams.
     */
    public ClientHandler(Socket socket, OthelloServer server) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.socket = socket;
        this.server = server;
        this.game = null;
    }

    /**
     * Gets the username associated with the client.
     * @return the username of the client.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the client.
     * @param username the username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the {@code ServerGame} instance associated with the client.
     * @return the {@code ServerGame} instance associated with the client.
     */
    public ServerGame getGame() {
        return game;
    }

    /**
     * Sets the {@code ServerGame} instance associated with the client.
     * @param game the {@code ServerGame} instance to set.
     */
    public void setGame(ServerGame game) {
        this.game = game;
    }

    /**
     * Gets the mark assigned to the client.
     * @return the mark assigned to the client.
     */
    public Mark getMark() {
        return mark;
    }

    /**
     * Sets the mark assigned to the client.
     * @param mark the mark to assign to the client.
     */
    public void setMark(Mark mark) {
        this.mark = mark;
    }

    /**
     * Executes the client handler thread, continuously reading client input and handling commands.
     */
    @Override
    public void run() {
        String clientOutput;
        try {
            clientOutput = reader.readLine();
            while (clientOutput != null) {
                System.out.println("[" + username + "]: " + clientOutput);
                handleClientCommand(clientOutput);
                clientOutput = reader.readLine();
            }
            shutDown();
        } catch (ClientUnavailableException e) {
            handleClientDisconnection();
        } catch (IOException e) {
            handleClientDisconnection();
        }
    }

    /**
     * Sends the specified server input to the associated client.
     * @param serverInput the server input to be sent to the client.
     * @throws ClientUnavailableException if the client is currently unavailable.
     */
    public void sendInputToClient(String serverInput) throws ClientUnavailableException {
        if (writer != null) {
            try {
                writer.write(serverInput);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                throw new ClientUnavailableException("Unfortunately, the client is currently unavailable\n");
            }
        }
    }

    /**
     * Handles the client command by parsing and executing the corresponding action.
     * @param receivedCommand the command received from the client.
     * @throws ClientUnavailableException if the client is currently unavailable.
     */
    private void handleClientCommand(String receivedCommand) throws ClientUnavailableException {
        String[] splitReceivedCommand = receivedCommand.split(Protocol.SEPARATOR);
        switch (splitReceivedCommand[0]) {
            case Protocol.HELLO -> server.sendHello(this);
            case Protocol.LOGIN -> server.sendLogin(this, splitReceivedCommand[1]);
            case Protocol.LIST -> server.sendList(this);
            case Protocol.QUEUE -> server.handleQueue(this);
            case Protocol.MOVE -> server.handleMove(this, receivedCommand);
            default -> server.sendError(this, "Client sent an unknown command " + receivedCommand);
        }
    }

    /**
     * Handles the disconnection of the client, notifying the server and shutting down the client handler.
     */
    private void handleClientDisconnection() {
        server.handleDisconnection(this);
        shutDown();
    }

    /**
     * Shuts down the client handler by closing streams, sockets, and removing itself from the server's client handlers.
     */
    protected void shutDown() {
        System.out.println("[" + username + "]: User exited!");
        try {
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {}
        server.removeClientHandler(this);
    }
}