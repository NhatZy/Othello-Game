package networking.server;

import othellogame.model.*;

/**
 * Extension of the {@link OthelloGame} class for server-side game management.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see OthelloGame
 * @see ClientHandler
 * @see Player
 */
public class ServerGame extends OthelloGame {
    /**
     * The client handler for the first player in the game.
     */
    private ClientHandler clientHandler1;

    /**
     * The client handler for the second player in the game.
     */
    private ClientHandler clientHandler2;

    /**
     * Constructs a {@code ServerGame} object with the specified players and their associated client handlers.
     * @param player1 the first player in the game.
     * @param player2 the second player in the game.
     * @param clientHandler1 the client handler for the first player.
     * @param clientHandler2 the client handler for the second player.
     */
    public ServerGame(Player player1, Player player2, ClientHandler clientHandler1, ClientHandler clientHandler2) {
        super(player1, player2);
        this.clientHandler1 = clientHandler1;
        this.clientHandler2 = clientHandler2;
    }

    /**
     * Gets the client handler for the first player.
     * @return the client handler for the first player.
     */
    public ClientHandler getClientHandler1() {
        return clientHandler1;
    }

    /**
     * Gets the client handler for the second player.
     * @return the client handler for the second player.
     */
    public ClientHandler getClientHandler2() {
        return clientHandler2;
    }
}