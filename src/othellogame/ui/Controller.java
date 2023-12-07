package othellogame.ui;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import othellogame.model.*;
import othellogame.ui.view.*;

import java.util.*;

/**
 * The main controller class that manages the flow of the Othello game application, following the Model-View-Controller (MVC) pattern.
 * <p>
 * It controls the transition between different scenes, updates the game state, and handles user interactions.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see Model
 * @see MenuView
 * @see OfflinePlayingView
 * @see OfflinePlayingMainView
 * @see ExitView
 * @see ThankYouView
 * @see GameInstructionsView
 * @see Sound
 * @see Mark
 */
public class Controller {
    /**
     * The game model.
     */
    Model model;

    /**
     * The view for the {@code MenuScene}, which is used to display the main menu.
     */
    MenuView menuView;

    /**
     * The view for the {@code OfflinePlayingScene}, which is used to display the offline gameplay setup.
     */
    OfflinePlayingView offlinePlayingView;

    /**
     * The view for the {@code OfflinePlayingMainScene}, which is used to display the offline gameplay.
     */
    OfflinePlayingMainView offlinePlayingMainView;

    /**
     * The view for the {@code ExitScene}, which is used to display the game results and provide options for replaying the game or exiting the Othello game application.
     */
    ExitView exitView;

    /**
     * The view for the {@code ThankYouScene}, which is used to thank the player.
     */
    ThankYouView thankYouView;

    /**
     * The view for the {@code GameInstructionsScene}, which is used to display game instructions.
     */
    GameInstructionsView gameInstructionsView;

    /**
     * The singleton instance of the {@code Controller} class.
     */
    static Controller instance;

    /**
     * The JavaFX stage for displaying scenes.
     */
    Stage stage;

    /**
     * The sound manager for controlling audio.
     */
    Sound sound;

    /**
     * The identifier of the previously displayed scene.
     */
    public static String previousScene;

    /**
     * Flag to track the first load of the {@code OfflinePlayingMainScene}.
     */
    private boolean offlinePlayingMainSceneFirstLoad;

    /**
     * Flag to indicate whether the {@code MenuScene} has been reloaded.
     */
    public static boolean menuSceneReloaded = false;

    /**
     * Constructor for the {@code Controller} class.
     * <p>
     * Initializes the game model, various views, and sets up the stage.
     */
    public Controller() {
        this.model = new Model();
        this.menuView = new MenuView();
        this.offlinePlayingView = new OfflinePlayingView();
        this.offlinePlayingMainView = new OfflinePlayingMainView();
        this.exitView = new ExitView();
        this.thankYouView = new ThankYouView();
        this.gameInstructionsView = new GameInstructionsView();
        this.stage = new Stage();
        this.sound = new Sound();
        this.offlinePlayingMainSceneFirstLoad = true;
        sound.playSound("menu");
        stage.setTitle("Othello");
        stage.setScene(new Scene(menuView.menu, 1080, 720));
    }

    /**
     * Gets the instance of the {@code Controller} class (singleton pattern).
     * @return the instance of the {@code Controller} class.
     */
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
            System.out.println("A new controller has been created");
        }
        return instance;
    }

    /**
     * Loads the specified scene based on the provided scene name.
     * @param scene the name of the scene to be loaded
     * @param pane the current pane from which the method is called. It's used to obtain the current stage to set the new scene.
     */
    public void loadScene(String scene, Pane pane) {
        Image soundOnIcon = new Image("file:src/images/SoundOn_Icon.png");
        Image soundOffIcon = new Image("file:src/images/SoundOff_Icon.png");
        switch (scene) {
            case "menu":
                menuView.loadMenuScene(pane);
                if (menuSceneReloaded) {
                    sound.stopSound("exit");
                    Sound.canPlayMenuSound = true;
                    sound.playSound("menu");
                    menuSceneReloaded = false;
                }
                if (Sound.canPlayMenuSound) {
                    menuView.soundIcon.setImage(soundOnIcon);
                    soundOnIcon.isPreserveRatio();
                    menuView.soundIcon.setFitHeight(75.0);
                    menuView.soundIcon.setFitWidth(75.0);
                    menuView.soundIcon.setLayoutX(-6.0);
                } else {
                    menuView.soundIcon.setImage(soundOffIcon);
                    soundOffIcon.isPreserveRatio();
                    menuView.soundIcon.setFitHeight(52.0);
                    menuView.soundIcon.setFitWidth(52.0);
                    menuView.soundIcon.setLayoutX(7.0);
                }
                Sound.canPlayGameSound = true;
                break;
            case "offlinePlaying":
                offlinePlayingView.loadOfflinePlayingScene(pane);
                if (Sound.canPlayMenuSound) {
                    offlinePlayingView.soundIcon.setImage(soundOnIcon);
                    soundOnIcon.isPreserveRatio();
                    offlinePlayingView.soundIcon.setFitHeight(75.0);
                    offlinePlayingView.soundIcon.setFitWidth(75.0);
                    offlinePlayingView.soundIcon.setLayoutX(-6.0);
                } else {
                    offlinePlayingView.soundIcon.setImage(soundOffIcon);
                    soundOffIcon.isPreserveRatio();
                    offlinePlayingView.soundIcon.setFitHeight(52.0);
                    offlinePlayingView.soundIcon.setFitWidth(52.0);
                    offlinePlayingView.soundIcon.setLayoutX(7.0);
                }
                break;
            case "offlinePlayingMain":
                if (Sound.canPlayMenuSound) {
                    sound.stopSound("menu");
                }
                offlinePlayingMainView.loadOfflinePlayingMainScene(pane);
                if (offlinePlayingMainSceneFirstLoad) {
                    sound.playSound("game");
                    this.offlinePlayingMainSceneFirstLoad = false;
                }
                model.createNewGame(OfflinePlayingView.firstUsername, OfflinePlayingView.secondUsername);
                break;
            case "exit":
                exitView.loadExitScene(pane);
                this.offlinePlayingMainSceneFirstLoad = true;
                sound.stopSound("game");
                sound.playSound("exit");
                break;
            case "thankYou":
                thankYouView.loadThankYouScene(pane);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> Platform.exit());
                    }
                }, 10000);
                break;
            case "gameInstructions":
                gameInstructionsView.loadGameInstructionsScene(pane);
                break;
        }
    }

    /**
     * Updates the primary stage to show the current scene.
     */
    public void update() {
        stage.show();
    }

    /**
     * Controls the menu sound state for the {@code MenuScene} and the {@code OfflinePlayingScene}.
     * @param currentView the current view (menu view or offline playing view) to determine the sound icon.
     */
    public void menuSoundControl(String currentView) {
        Image replacedSoundIcon;
        if (Sound.canPlayMenuSound) {
            Sound.canPlayMenuSound = false;
            sound.stopSound("menu");
            replacedSoundIcon = new Image("file:src/images/SoundOff_Icon.png");
            if (currentView.equals("menuView")) {
                menuView.soundIcon.setImage(replacedSoundIcon);
                replacedSoundIcon.isPreserveRatio();
                menuView.soundIcon.setFitHeight(52.0);
                menuView.soundIcon.setFitWidth(52.0);
                menuView.soundIcon.setLayoutX(7.0);
            } else if (currentView.equals("offlinePlayingView")) {
                offlinePlayingView.soundIcon.setImage(replacedSoundIcon);
                replacedSoundIcon.isPreserveRatio();
                offlinePlayingView.soundIcon.setFitHeight(52.0);
                offlinePlayingView.soundIcon.setFitWidth(52.0);
                offlinePlayingView.soundIcon.setLayoutX(7.0);
            }
            System.out.println("Menu sound has been turned off");
        } else {
            Sound.canPlayMenuSound = true;
            sound.playSound("menu");
            replacedSoundIcon = new Image("file:src/images/SoundOn_Icon.png");
            if (currentView.equals("menuView")) {
                menuView.soundIcon.setImage(replacedSoundIcon);
                replacedSoundIcon.isPreserveRatio();
                menuView.soundIcon.setFitHeight(75.0);
                menuView.soundIcon.setFitWidth(75.0);
                menuView.soundIcon.setLayoutX(-6.0);
            } else if (currentView.equals("offlinePlayingView")) {
                offlinePlayingView.soundIcon.setImage(replacedSoundIcon);
                replacedSoundIcon.isPreserveRatio();
                offlinePlayingView.soundIcon.setFitHeight(75.0);
                offlinePlayingView.soundIcon.setFitWidth(75.0);
                offlinePlayingView.soundIcon.setLayoutX(-6.0);
            }
            System.out.println("Menu sound has been turned on");
        }
    }

    /**
     * Controls the game sound state for the {@code OfflinePlayingMainScene}.
     */
    public void gameSoundControl() {
        Image replacedSoundIcon;
        if (Sound.canPlayGameSound) {
            Sound.canPlayGameSound = false;
            sound.stopSound("game");
            replacedSoundIcon = new Image("file:src/images/SoundOff_Icon.png");
            offlinePlayingMainView.soundIcon.setImage(replacedSoundIcon);
            replacedSoundIcon.isPreserveRatio();
            offlinePlayingMainView.soundIcon.setFitHeight(52.0);
            offlinePlayingMainView.soundIcon.setFitWidth(52.0);
            offlinePlayingMainView.soundIcon.setLayoutX(7.0);
            System.out.println("Game sound has been turned off");
        } else {
            Sound.canPlayGameSound = true;
            sound.playSound("game");
            replacedSoundIcon = new Image("file:src/images/SoundOn_Icon.png");
            offlinePlayingMainView.soundIcon.setImage(replacedSoundIcon);
            replacedSoundIcon.isPreserveRatio();
            offlinePlayingMainView.soundIcon.setFitHeight(75.0);
            offlinePlayingMainView.soundIcon.setFitWidth(75.0);
            offlinePlayingMainView.soundIcon.setLayoutX(-6.0);
            System.out.println("Game sound has been turned on");
        }
    }

    /**
     * Updates the announcement of the current player in the {@code OfflinePlayingMainScene}.
     * @param mark the mark (BLACK or WHITE) of the current player.
     * @param name the name of the current player.
     */
    public void updateCurrentPlayerAnnouncement(Mark mark, String name) {
        offlinePlayingMainView.updateCurrentPlayerAnnouncement(mark, name);
    }

    /**
     * Handles the user move in the game.
     * @param index the index where the user wants to place a disc.
     */
    public void handleMove(int index) {
        model.updateCurrentMove(index);
    }

    /**
     * Updates the announcement of the current move in the {@code OfflinePlayingMainScene}.
     * @param validMove indicates whether the move is valid.
     * @param name the name of the player making the move.
     * @param mark the mark (BLACK or WHITE) of the player making the move.
     * @param index the index of the move on the game board.
     */
    public void updateCurrentMoveAnnouncement(boolean validMove, String name, Mark mark, int index) {
        offlinePlayingMainView.updateCurrentMoveAnnouncement(validMove, name, mark, index);
    }

    /**
     * Updates the game board in the {@code OfflinePlayingMainScene} based on the specified move, indicating whether a disc is added or flipped.
     * @param addDisc indicates whether to add a disc to the board. If true, indicates that a disc is added to the specified index; if false, indicates that the disc at the specified index is flipped.
     * @param mark the mark (BLACK or WHITE) associated with the disc being added or flipped.
     * @param index the index on the game board where the disc is added or flipped.
     */
    public void updateBoard(boolean addDisc, Mark mark, int index) {
        offlinePlayingMainView.updateBoard(addDisc, mark, index);
    }

    /**
     * Ends the game and loads the {@code ExitScene}.
     */
    public void endGame() {
        loadScene("exit", offlinePlayingMainView.mainOfflinePlaying);
    }

    /**
     * Updates the player results in the {@code ExitScene}.
     * @param finalResult indicates whether the provided result is the final outcome of the game.
     * @param playerIndex the index of the player.
     * @param winner the name of the winner.
     * @param totalDiscs the total number of discs acquired by the player.
     * @param winnerMark the mark (BLACK or WHITE) of the winner.
     * @param resignedPlayer the name of the player who resigned, if a player has resigned; otherwise, set to "null".
     */
    public void updatePlayersResults(boolean finalResult, int playerIndex, String winner, int totalDiscs, Mark winnerMark, String resignedPlayer) {
        exitView.updatePlayersResults(finalResult, playerIndex, winner, totalDiscs, winnerMark, resignedPlayer);
    }

    /**
     * Handles the quitting of the game.
     */
    public void handleQuitGame() {
        model.handleQuitGame();
    }
}