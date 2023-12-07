import org.junit.jupiter.api.*;
import othellogame.ai.*;
import othellogame.model.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test class for the interaction between different player implementations and strategies in the Othello game.
 * <p>
 * This class includes tests for player names, player marks, and strategies such as {@link NaiveStrategy} and {@link SmartStrategy}.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see AbstractPlayer
 * @see OthelloGame
 * @see HumanPlayer
 * @see Mark
 * @see ComputerPlayer
 * @see NaiveStrategy
 * @see SmartStrategy
 * @see Move
 */
class PlayerAndStrategyTest {
    /**
     * The first human player in the Othello game, initialized with the mark BLACK.
     */
    private AbstractPlayer player1;

    /**
     * The second human player in the Othello game, initialized with the mark WHITE.
     */
    private AbstractPlayer player2;

    /**
     * A computer player with the {@code NaiveStrategy} in the Othello game, initialized with the mark BLACK.
     */
    private AbstractPlayer player3;

    /**
     * A computer player with the {@code SmartStrategy} in the Othello game, initialized with the mark WHITE.
     */
    private AbstractPlayer player4;

    /**
     * A randomly created Othello game used for testing various player strategies.
     */
    private OthelloGame randomGame;

    /**
     * Default constructor for the {@code PlayerAndStrategyTest} class.
     * <p>
     * Note: this constructor is automatically generated by the compiler and is not intended for direct use.
     */
    @SuppressWarnings("unused")
    PlayerAndStrategyTest() {}

    /**
     * Sets up the initial state for each test case. Initializes human players, computer players, and the Othello game.
     */
    @BeforeEach
    public void setUp() {
        this.player1 = new HumanPlayer("1st human player", Mark.BLACK);
        this.player2 = new HumanPlayer("2nd human player", Mark.WHITE);
        this.player3 = new ComputerPlayer(new NaiveStrategy(), Mark.BLACK);
        this.player4 = new ComputerPlayer(new SmartStrategy(), Mark.WHITE);
        this.randomGame = new OthelloGame(player3, player4);
    }

    /**
     * Tests the retrieval of the player names for human players and computer players with different strategies.
     */
    @Test
    void testGetName() {
        assertEquals("1st human player", player1.getName());
        assertEquals("2nd human player", player2.getName());
        assertEquals("Naive AI", player3.getName());
        assertEquals("Smart AI", player4.getName());
    }

    /**
     * Tests the retrieval of player marks for human players and computer players.
     */
    @Test
    void testGetMark() {
        assertEquals(Mark.BLACK, player1.getMark());
        assertEquals(Mark.WHITE, player2.getMark());
        assertEquals(Mark.BLACK, player3.getMark());
        assertEquals(Mark.WHITE, player4.getMark());
    }

    /**
     * Tests the {@code NaiveStrategy} by making a move in a randomly created Othello game and ensuring the move is valid.
     */
    @Test
    void testNaiveStrategy() {
        Move naiveAIMove = player3.determineMove(randomGame);
        assertTrue(randomGame.isValidMove(naiveAIMove));
    }

    /**
     * Tests the {@code SmartStrategy} by making moves in a randomly created Othello game until the game is over, and then checks if the correct player is declared as the winner.
     */
    @Test
    void testSmartStrategy() {
        Move smartAIMove = player4.determineMove(randomGame);
        assertTrue(randomGame.isValidMove(smartAIMove));
        while (!randomGame.isGameOver()) {
            AbstractPlayer currentPlayer = (AbstractPlayer) randomGame.getTurn();
            if (randomGame.getValidMoves().isEmpty()) {
                randomGame.swapTurn();
            } else {
                randomGame.doMove(currentPlayer.determineMove(randomGame));
            }
        }
        assertEquals(player4.getMark(), randomGame.getWinner());
    }
}