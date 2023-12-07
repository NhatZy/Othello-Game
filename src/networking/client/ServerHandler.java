package networking.client;

import networking.Protocol;
import networking.exceptions.ProtocolException;
import networking.exceptions.*;

import java.io.*;
import java.net.*;

/**
 * Represents a handler for communication with the Othello game server.
 * <p>
 * It implements the {@link Runnable} interface to handle communication in a separate thread.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see OthelloClient
 * @see ProtocolException
 * @see ServerUnavailableException
 */
public class ServerHandler implements Runnable {
    /**
     * The reader for reading input from the server.
     */
    private BufferedReader reader;

    /**
     * The writer for sending output to the server.
     */
    private BufferedWriter writer;

    /**
     * The socket for the communication channel with the server.
     */
    private Socket socket;

    /**
     * The Othello client associated with this server handler.
     */
    private OthelloClient client;

    /**
     * A boolean flag indicating whether the server handler is running.
     */
    private boolean isRunning = true;

    /**
     * Constructs a {@code ServerHandler} instance with the given socket and client.
     * @param socket the socket for communication with the server.
     * @param client the Othello client associated with this handler.
     * @throws IOException if an I/O error occurs while creating the handler.
     */
    public ServerHandler(Socket socket, OthelloClient client) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.socket = socket;
        this.client = client;
    }

    /**
     * Runs the server handler, continuously reading and processing server output.
     * <p>
     * Handles various exceptions that may occur during communication.
     */
    @Override
    public void run() {
        String serverOutput;
        try {
            serverOutput = reader.readLine();
            while (serverOutput != null && isRunning) {
                handleServerCommand(serverOutput);
                serverOutput = reader.readLine();
            }
            if (isRunning) {
                clearConnection();
            }
        } catch (ProtocolException e) {
            client.showToUserScreen("Unfortunately, the connection is closed since the server's output doesn't correspond to the protocol\n");
            client.closeConnection(false);
        } catch (ServerUnavailableException e) {
            client.showToUserScreen("Unfortunately, the connection is closed since the server is currently not available\n");
            client.closeConnection(false);
        } catch (IOException e1) {
            client.showToUserScreen("ERROR: " + e1.getMessage() + "\n");
            if (e1.getMessage().equals("Connection reset")) {
                client.showToUserScreen("Unfortunately, the server shut down!\n");
                try {
                    client.showToUserScreen("Thank you so much for using my Othello Game Server. I hope to see you later and a nice day ahead as well!\n");
                    clearConnection();
                    System.exit(0);
                } catch (IOException e2) {}
            }
        }
    }

    /**
     * Sends user input to the server.
     * @param userInput the user's input to be sent to the server.
     * @throws ServerUnavailableException if the server is currently unavailable.
     */
    public void sendInputToServer(String userInput) throws ServerUnavailableException {
        if (writer != null) {
            try {
                writer.write(userInput);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                throw new ServerUnavailableException("Unfortunately, the server is currently unavailable\n");
            }
        }
    }

    /**
     * Handles a server command by interpreting and processing the received command.
     * @param receivedCommand the command received from the server.
     * @throws ProtocolException if the server sends an unknown or invalid command.
     * @throws ServerUnavailableException if the server is currently unavailable.
     */
    private void handleServerCommand(String receivedCommand) throws ProtocolException, ServerUnavailableException {
        String[] splitReceivedCommand = receivedCommand.split(Protocol.SEPARATOR);
        switch (splitReceivedCommand[0]) {
            case Protocol.HELLO -> client.handleHello(receivedCommand);
            case Protocol.LOGIN, Protocol.ALREADYLOGGEDIN -> client.handleLogin(receivedCommand);
            case Protocol.LIST -> client.handleList(receivedCommand);
            case Protocol.NEWGAME -> client.handleNewGame(receivedCommand);
            case Protocol.MOVE -> client.handleMove(receivedCommand);
            case Protocol.GAMEOVER -> client.handleGameOver(receivedCommand);
            case Protocol.ERROR -> client.showToUserScreen("[Server]: ERROR - " + splitReceivedCommand[1] + "\n");
            default -> throw new ProtocolException("Server sent an unknown command: " + receivedCommand + "\n");
        }
    }

    /**
     * Closes the connection by closing the reader, writer, and socket.
     * @throws IOException if an I/O error occurs while closing the connection.
     */
    public void clearConnection() throws IOException {
        reader.close();
        writer.close();
        socket.close();
        this.isRunning = false;
    }
}