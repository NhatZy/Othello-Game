package othellogame.ui.view;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.image.*;
import othellogame.ui.*;

import java.io.*;
import java.util.*;

/**
 * Represents the view for the main menu in the Othello game application.
 * <p>
 * It handles the initialization of the menu, language changes, and various actions triggered by user interactions.
 * <p>
 * This class is associated with the {@code MenuScene} FXML file.
 * <p>
 * Follows the Model-View-Controller (MVC) pattern.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 * @see Controller
 */
public class MenuView {
    /**
     * The FXMLLoader responsible for loading the FXML file and initializing the view components.
     */
    private FXMLLoader loader;

    /**
     * The main pane representing the {@code MenuScene}.
     */
    @FXML
    public Pane menu;

    /**
     * The container for language options in the menu.
     */
    @FXML
    private VBox languageOptions;

    /**
     * The text element displaying instructions in the menu.
     */
    @FXML
    private Text instructions;

    /**
     * The text element representing the play option in the menu.
     */
    @FXML
    private Text play;

    /**
     * The image view representing the game board in the menu.
     */
    @FXML
    private ImageView board;

    /**
     * The image view representing the sound icon in the menu.
     */
    @FXML
    public ImageView soundIcon;

    /**
     * The group containing the exit application confirmation dialog components.
     */
    @FXML
    private Group exitGameQuestion;

    /**
     * Default constructor for the {@code MenuScene} class.
     * <p>
     * Initializes the view with the default locale and loads the associated FXML file.
     */
    public MenuView() {
        Locale.setDefault(new Locale("en_GB"));
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("othellogame.ui.languages", Locale.getDefault());
            this.loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MenuScene.fxml"));
            loader.setResources(bundle);
            loader.setController(this);
            this.menu = loader.load();
        } catch (IOException e) {
            System.out.println("ERROR: [MenuScene] - " + e.getMessage());
        }
    }

    /**
     * Loads the {@code MenuScene} into the specified parent pane.
     * @param currentPane the current pane from which the scene is loaded.
     */
    public void loadMenuScene(Pane currentPane) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("othellogame.ui.languages", Locale.getDefault());
            this.loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MenuScene.fxml"));
            loader.setResources(bundle);
            loader.setController(this);
            Stage stage = (Stage) currentPane.getScene().getWindow();
            Parent root = loader.load();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("ERROR: [MenuScene] - " + e.getMessage());
        }
    }

    /**
     * Changes the language of the application to German (DE).
     */
    @FXML
    private void changeLanguageToDE() {
        loadLanguage("de_DE");
    }

    /**
     * Changes the language of the application to English (EN).
     */
    @FXML
    private void changeLanguageToEN() {
        loadLanguage("en_GB");
    }

    /**
     * Changes the language of the application to Vietnamese (VI).
     */
    @FXML
    private void changeLanguageToVI() {
        loadLanguage("vi_VN");
    }

    /**
     * Loads the specified language and updates the scene.
     * @param language the language code to load.
     */
    private void loadLanguage(String language) {
        Locale.setDefault(new Locale(language));
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("othellogame.ui.languages", Locale.getDefault());
            this.loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MenuScene.fxml"));
            loader.setResources(bundle);
            loader.setController(this);
            Stage stage = (Stage) menu.getScene().getWindow();
            Parent root = loader.load();
            stage.getScene().setRoot(root);
            System.out.println("Language has been changed to " + language);
        } catch (IOException e) {
            System.out.println("ERROR: [MenuScene] - " + e.getMessage());
        }
    }

    /**
     * Handles the click event on the "instructions" text, loading the {@code GameInstructionsScene}.
     */
    @FXML
    private void onInstructionsClick() {
        Controller.previousScene = "menu";
        Controller.getInstance().loadScene("gameInstructions", menu);
    }

    /**
     * Handles the click event on the "play" text, loading the {@code OfflinePlayingScene}.
     */
    @FXML
    private void playGame() {
        Controller.getInstance().loadScene("offlinePlaying", menu);
    }

    /**
     * Handles the click event on the close icon in the exit application confirmation dialog, hiding the dialog.
     */
    @FXML
    private void onCloseIconClick() {
        this.exitGameQuestion.setVisible(false);
        this.board.setVisible(true);
        this.play.setVisible(true);
        this.languageOptions.setVisible(true);
        this.instructions.setVisible(true);
    }

    /**
     * Handles the click event on the sound control icon in the {@code MenuScene}, triggering menu sound control.
     */
    @FXML
    public void menuSoundControl() {
        Controller.getInstance().menuSoundControl("menuView");
    }

    /**
     * Handles the click event on the shutdown icon, displaying the exit application confirmation dialog.
     */
    @FXML
    private void onShutDownIconClick() {
        this.board.setVisible(false);
        this.play.setVisible(false);
        this.languageOptions.setVisible(false);
        this.instructions.setVisible(false);
        this.exitGameQuestion.setVisible(true);
    }

    /**
     * Handles the click event on the yes button in the exit application confirmation dialog, shutting down the application.
     */
    @FXML
    private void shutDown() {
        Controller.getInstance().loadScene("thankYou", menu);
    }
}