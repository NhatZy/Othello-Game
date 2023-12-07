import networking.Protocol;
import networking.client.*;
import networking.exceptions.*;
import networking.exceptions.ProtocolException;
import org.junit.jupiter.api.*;
import othellogame.model.*;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test class for the {@link OthelloClient} class, which represents the client-side functionality of the Othello game application, including interactions with the server.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see OthelloClient
 * @see ProtocolException
 * @see Protocol
 * @see ServerUnavailableException
 * @see ServerHandler
 */
class ClientTest {
    /**
     * The {@link ByteArrayOutputStream} used to capture standard output during testing.
     */
    private final static ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    /**
     * The original standard output, preserved for restoring after capturing during testing.
     */
    private final static PrintStream originalOut = System.out;

    /**
     * An instance of the {@link OthelloClient} class, which represents the client-side functionality for the Othello game.
     */
    private OthelloClient client = new OthelloClient();

    /**
     * Default constructor for the {@code ClientTest} class.
     * <p>
     * Note: this constructor is automatically generated by the compiler and is not intended for direct use.
     */
    @SuppressWarnings("unused")
    ClientTest() {}

    /**
     * Sets up the output stream redirection to capture "System.out" during testing.
     */
    @BeforeAll
    public static void setUpStream() {
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Tests the {@link OthelloClient#handleHello(String)} method, which processes the server's greeting message.
     * @throws ProtocolException if an error occurs during protocol handling.
     */
    @Test
    void testHandleHello() throws ProtocolException {
        client.handleHello(Protocol.HELLO + Protocol.SEPARATOR + "Tester Server");
        assertThat(outputStream.toString(), containsString("Tester Server"));
        outputStream.reset();
    }

    /**
     * Tests the {@link OthelloClient#handleLogin(String)} method, which processes the server's response to a login attempt.
     */
    @Test
    void testHandleLogin() {
        client.handleLogin(Protocol.LOGIN);
        assertThat(outputStream.toString(), containsString("You're successfully logged in as "));
        outputStream.reset();
        client.handleLogin(Protocol.ALREADYLOGGEDIN);
        assertThat(outputStream.toString(), containsString("Please try logging in again with a different username!"));
        outputStream.reset();
    }

    /**
     * Tests the {@link OthelloClient#handleList(String)} method, which processes the server's response to a list request, displaying the available players in the game lobby.
     */
    @Test
    void testHandleList() {
        client.handleList(Protocol.LIST + Protocol.SEPARATOR + "Tester Server 1" + Protocol.SEPARATOR + "Tester Server 2");
        String list = outputStream.toString();
        assertThat(list, containsString("1. Tester Server 1"));
        assertThat(list, containsString("2. Tester Server 2"));
        outputStream.reset();
    }

    /**
     * Tests the {@link OthelloClient#handleNewGame(String)} method, which processes the server's response to starting a new game, including initializing the game board.
     * @throws ProtocolException if an error occurs during protocol handling.
     * @throws ServerUnavailableException if the server is not available during the test.
     */
    @Test
    void testHandleNewGame() throws ProtocolException, ServerUnavailableException {
        client.setLoggedInAs("Tester Server 2");
        client.handleNewGame("NEWGAME~Tester Server 1~Tester Server 2");
        Board board = new Board();
        String output = outputStream.toString();
        assertThat(output, containsString("New Othello Game with Tester Server 1 has started"));
        assertThat(output, containsString(board.toIndexString()));
        assertThat(output, containsString("Waiting for another player's move ..."));
        outputStream.reset();
    }

    /**
     * Tests the {@link OthelloClient#handleMove(String)} method, which processes the server's response to a player's move during an ongoing game.
     * @throws ProtocolException if an error occurs during protocol handling.
     * @throws ServerUnavailableException if the server is not available during the test.
     */
    @Test
    void testHandleMove() throws ProtocolException, ServerUnavailableException {
        client.setLoggedInAs("Tester Server 1");
        client.handleNewGame("NEWGAME~Tester Server 1~Tester Server 2");
        outputStream.reset();
        try {
            client.handleMove(Protocol.MOVE + Protocol.SEPARATOR + "26");
        } catch (ServerUnavailableException e) {}
        Board board = new Board();
        board.setField(26, Mark.BLACK);
        board.setField(27, Mark.BLACK);
        String output = outputStream.toString();
        assertThat(output, containsString(board.toIndexString()));
        assertThat(output, containsString("Waiting for another player's move ..."));
        outputStream.reset();
    }

    /**
     * Tests the {@link OthelloClient#handleGameOver(String)} method, which processes the server's response when a game is concluded, providing the outcome of the game.
     * @throws ProtocolException if an error occurs during protocol handling.
     */
    @Test
    void testHandleGameOver() throws ProtocolException {
        client.setLoggedInAs("Tester Server 1");

        //Checks handleGameOver(String serverOutput) method when the reason is "DISCONNECT"
        client.handleGameOver(Protocol.GAMEOVER + Protocol.SEPARATOR + Protocol.DISCONNECT + Protocol.SEPARATOR + "Tester Server 1");
        assertThat(outputStream.toString(), containsString("Another player has disconnected, so the game is won by you. Congratulations!"));
        outputStream.reset();

        //Checks handleGameOver(String serverOutput) method when the reason is "VICTORY"
        client.setGame("Tester Server 2", Mark.BLACK, Mark.WHITE);
        client.handleGameOver(Protocol.GAMEOVER + Protocol.SEPARATOR + Protocol.VICTORY + Protocol.SEPARATOR + "Tester Server 1");
        assertThat(outputStream.toString(), containsString("The game ended with the winner is you. Congratulations!"));
        outputStream.reset();
        client.setGame("Tester Server 2", Mark.BLACK, Mark.WHITE);
        client.handleGameOver(Protocol.GAMEOVER + Protocol.SEPARATOR + Protocol.VICTORY + Protocol.SEPARATOR + "Tester Server 2");
        assertThat(outputStream.toString(), containsString("So sorry, the game ended with the winner is Tester Server 2. Better luck next time!"));
        outputStream.reset();

        //Checks handleGameOver(String serverOutput) method when the reason is "DRAW"
        client.handleGameOver(Protocol.GAMEOVER + Protocol.SEPARATOR + Protocol.DRAW);
        assertThat(outputStream.toString(), containsString("The game ended in a draw, it was a good effort though!"));
        outputStream.reset();
    }

    /**
     * Tests the various send methods in the {@code OthelloClient} class and the {@code handleServerCommand} method of the {@link ServerHandler} class that communicate with the server, ensuring the proper handling of server responses and correct interactions between the client and server.
     * <p>
     * This test covers the following scenarios:
     * <p>
     * - Sending a hello message and handling the server's response.
     * <p>
     * - Attempting to list available players without logging in.
     * <p>
     * - Attempting to queue for a game without logging in.
     * <p>
     * - Logging in with a username and handling the server's response.
     * <p>
     * - Attempting to log in again after already being logged in.
     * <p>
     * - Sending a list request and handling the server's response.
     * <p>
     * - Trying to push a move when not in a game and validating the response.
     * <p>
     * - Joining and leaving the game queue and validating the responses.
     * <p>
     * - Pushing a move while in a game and validating the response.
     * <p>
     * - Handling server commands such as ERROR and testing the server's response.
     * <p>
     * - Handling an unknown command from the server and checking for proper exception handling.
     * <p>
     * - Testing the behavior when the server is unavailable.
     * @throws ServerUnavailableException if there is an issue with the server connection during testing.
     */
    @Test
    void testSendMethods() throws ServerUnavailableException {
        try {
            client = new OthelloClient();
            ServerSocket serverSocket = new ServerSocket(8080);
            Socket socket = new Socket("localhost", 8080);
            client.setServerSocket(socket);
            Socket socketServer = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socketServer.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socketServer.getOutputStream()));

            //Checks sendHello() method
            client.sendHello();
            assertThat(reader.readLine(), containsString(Protocol.HELLO));

            writer.write(Protocol.HELLO + Protocol.SEPARATOR + "NhatZy's Server");
            writer.newLine();
            writer.flush();

            //Checks sendList() method when the client hasn't logged in yet
            client.sendList();
            assertThat(outputStream.toString(), containsString("You need to login before performing this action!"));
            outputStream.reset();

            //Checks sendQueue() method when the client hasn't logged in yet
            client.sendQueue();
            assertThat(outputStream.toString(), containsString("You need to login before performing this action!"));
            outputStream.reset();

            //Checks sendLogin(String username) method
            client.sendLogin("Tester Server");
            assertThat(reader.readLine(), containsString(Protocol.LOGIN + Protocol.SEPARATOR + "Tester Server"));

            writer.write(Protocol.LOGIN);
            writer.newLine();
            writer.flush();
            Thread.sleep(100); //Introduce a short delay to wait for the server's response

            //Checks sendLogin(String username) method when the client has already logged into the server
            client.sendLogin("Tester Server Again");
            assertThat(outputStream.toString(), containsString("You're already logged into the server!"));
            outputStream.reset();

            //Checks sendList() method
            client.sendList();
            assertThat(reader.readLine(), containsString(Protocol.LIST));

            writer.write(Protocol.LIST + Protocol.SEPARATOR + "Tester Server");
            writer.newLine();
            writer.flush();
            Thread.sleep(100); //Introduce a short delay to wait for the server's response

            //Checks sendMove(int index) method when the client is currently not in a game
            client.sendMove(0);
            assertThat(outputStream.toString(), containsString("You're currently not in a game; therefore, you cannot push a move!"));
            outputStream.reset();

            String userTestInput = "no" + System.lineSeparator() + "yes" + System.lineSeparator() + "no" + System.lineSeparator();
            BufferedReader testReader = new BufferedReader(new StringReader(userTestInput));
            ((ClientTUI) client.getClientView()).replaceReader(testReader);

            //Checks sendQueue() method
            client.sendQueue();
            assertThat(reader.readLine(), containsString(Protocol.QUEUE));
            assertThat(outputStream.toString(), containsString("You successfully joined the queue."));
            outputStream.reset();
            client.sendQueue();
            assertThat(outputStream.toString(), containsString("You successfully left the queue"));
            outputStream.reset();
            client.sendQueue();
            assertThat(reader.readLine(), containsString(Protocol.QUEUE));
            assertThat(outputStream.toString(), containsString("You successfully joined the queue."));
            writer.write("NEWGAME~Tester Server~Another Player");
            writer.newLine();
            writer.flush();
            Thread.sleep(100); //Introduce a short delay to wait for the server's response
            assertThat(outputStream.toString(), containsString("This is your turn"));
            outputStream.reset();
            client.sendQueue();
            assertThat(outputStream.toString(), containsString("You cannot join the queue since you're currently in a game!"));
            outputStream.reset();
            reader.readLine();

            //Checks sendMove(int index) method
            client.doHint();
            assertThat(outputStream.toString(), containsString("You can choose this move: "));
            outputStream.reset();
            client.sendMove(26);
            assertThat(reader.readLine(), containsString(Protocol.MOVE + Protocol.SEPARATOR + "26"));
            writer.write(Protocol.MOVE + Protocol.SEPARATOR + "26");
            writer.newLine();
            writer.flush();
            Thread.sleep(100); //Introduce a short delay to wait for the server's response
            client.sendMove(0);
            assertThat(outputStream.toString(), containsString("This is not your turn. Please wait for another player to perform the move first!"));
            outputStream.reset();

            //Checks handleServerCommand(String receivedCommand) method when the command "ERROR" is received
            writer.write(Protocol.ERROR + Protocol.SEPARATOR + "Test Error");
            writer.newLine();
            writer.flush();
            Thread.sleep(100); //Introduce a short delay to wait for the server's response
            assertThat(outputStream.toString(), containsString("[Server]: ERROR - Test Error"));
            outputStream.reset();

            //Checks handleServerCommand(String receivedCommand) method when the server sent an unknown command
            ServerHandler serverHandler = new ServerHandler(socket, client);
            String serverUnknownCommand = "Test Command - This is not a valid command";
            ProtocolException thrownException = assertThrows(ProtocolException.class, () -> invokeServerHandlerPrivateMethod(serverHandler, "handleServerCommand", serverUnknownCommand));
            assertTrue(thrownException.getMessage().contains("Server sent an unknown command: "));

            //Tests ServerUnavailableException
            client = new OthelloClient();
            socket = new Socket("localhost", 8080);
            client.setServerSocket(socket);
            socketServer = serverSocket.accept();
            writer = new BufferedWriter(new OutputStreamWriter(socketServer.getOutputStream()));
            client.sendHello();
            writer.write(Protocol.HELLO + Protocol.SEPARATOR + "NhatZy's Server");
            writer.newLine();
            writer.flush();
            Thread.sleep(100); //Introduce a short delay to wait for the server's response
            socketServer.close();
            serverSocket.close();
            userTestInput = "LIST" + System.lineSeparator();
            testReader = new BufferedReader(new StringReader(userTestInput));
            ((ClientTUI) client.getClientView()).replaceReader(testReader);
            assertThrows(ServerUnavailableException.class, () -> client.sendHello());
        } catch (IOException e) {
            assert false : "Address localhost:8080 is already in use";
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Invokes a private method of the {@code ServerHandler} class for testing purposes.
     * @param serverHandler the instance of the {@code ServerHandler} class.
     * @param methodName the name of the private method to invoke.
     * @param argument the argument to pass to the invoked method.
     * @throws Exception if an error occurs during the method invocation. The exception is thrown by the invoked method.
     */
    private void invokeServerHandlerPrivateMethod(ServerHandler serverHandler, String methodName, String argument) throws Exception {
        try {
            Method method = ServerHandler.class.getDeclaredMethod(methodName, String.class);
            method.setAccessible(true);
            method.invoke(serverHandler, argument);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e1) {
            throw (Exception) e1.getTargetException();
        }
    }

    /**
     * Restores the original "System.out" stream after all tests have been executed.
     */
    @AfterAll
    static void restoreStream() {
        System.setOut(originalOut);
    }
}