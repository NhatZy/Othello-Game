package networking;

/**
 * Defines constants for the communication protocol between the server and clients in the Othello game system.
 * <p>
 * It includes command names, separators, and error indicators.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 */
public final class Protocol {
    /**
     * The separator used to delimit different parts of a command.
     */
    public static final String SEPARATOR = "~";

    /**
     * Command representing the initial message sent by the client once a connection has been established.
     */
    public static final String HELLO = "HELLO";

    /**
     * Command representing the client's request to claim a username on the server during the handshake.
     */
    public static final String LOGIN = "LOGIN";

    /**
     * Command indicating that the chosen username is already logged in or claimed by another client.
     */
    public static final String ALREADYLOGGEDIN = "ALREADYLOGGEDIN";

    /**
     * Command for requesting a list of clients currently logged into the server.
     */
    public static final String LIST = "LIST";

    /**
     * Command sent by the server to all players that are put into a newly-started game.
     */
    public static final String NEWGAME = "NEWGAME";

    /**
     * Command used by clients to indicate the index they want to push during their turn.
     */
    public static final String MOVE = "MOVE";

    /**
     * Command sent by the server to indicate the end of a game, providing information about the game's conclusion.
     * <p>
     * This command includes the reasons for the game ending and, if applicable, the winner's username.
     */
    public static final String GAMEOVER = "GAMEOVER";

    /**
     * Command indicating the end of a game due to losing connection to one of the players.
     */
    public static final String DISCONNECT = "DISCONNECT";

    /**
     * Command indicating the end of a game with one of the players winning.
     */
    public static final String VICTORY = "VICTORY";

    /**
     * Command indicating the end of a game in a draw.
     */
    public static final String DRAW = "DRAW";

    /**
     * Command used by clients to indicate their desire to participate in a game or leave the game queue.
     * <p>
     * Clients send this command to express their intention to either join the game or withdraw from it. If issued again by a client already in the queue, it signals the client's decision to leave the game queue.
     */
    public static final String QUEUE = "QUEUE";

    /**
     * Command indicating an error in the communication process, encompassing issues beyond the protocol.
     * <p>
     * This includes communication failures, unexpected behavior, or any other error that hinders the communication flow.
     * <p>
     * An optional error description may provide additional information about the nature of the error.
     */
    public static final String ERROR = "ERROR";

    /**
     * Default constructor for the {@code Protocol} class.
     * <p>
     * Note: this constructor is automatically generated by the compiler and is not intended for direct use.
     */
    @SuppressWarnings("unused")
    Protocol() {}
}