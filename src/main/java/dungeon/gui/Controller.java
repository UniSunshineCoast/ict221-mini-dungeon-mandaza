package dungeon.gui;

import dungeon.engine.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import java.io.*;
import java.util.Optional;
import javafx.scene.layout.StackPane;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/**
 * The main controller for the MiniDungeon JavaFX GUI.
 * Handles user input, updates the game state, and manages the GUI components.
 */
public class Controller {
    @FXML private GridPane gridPane;
    @FXML private Label goldLabel;
    @FXML private Label healthLabel;
    @FXML private Label stepsLabel;
    @FXML private TextArea gameLog;
    @FXML private Button upButton;
    @FXML private Button downButton;
    @FXML private Button leftButton;
    @FXML private Button rightButton;
    @FXML private Button helpButton;
    @FXML private Button saveButton;
    @FXML private Button loadButton;
    @FXML private Spinner<Integer> difficultySpinner;
    @FXML private ListView<String> topScoresList;
    @FXML private Label scoreLabel;

    private GameEngine engine;
    private static final String SAVE_FILE = "minidungeon_save.dat";
    private static final String TOP_SCORES_FILE = "top_scores.dat";
    private List<ScoreEntry> topScores = new ArrayList<>();
    private int lastStatusIndex = 0;

    /**
     * Initializes the controller and starts a new game.
     */
    @FXML
    public void initialize() {
        engine = new GameEngine(10, 10);
        updateGameGrid();
        updateStatus();
        updateTopScoresDisplay();
        logMessage("Welcome to MiniDungeon! Use the arrow buttons to move.");
    }

    /**
     * Updates the game grid display based on the current game state.
     */
    public void updateGameGrid() {
        gridPane.getChildren().clear();
        for (int row = 0; row < engine.getHeight(); row++) {
            for (int col = 0; col < engine.getWidth(); col++) {
                StackPane cellPane = new StackPane();
                // Add the cell's node
                cellPane.getChildren().add(engine.getCell(row, col).getNode());
                // If player is at this position, add the player node on top
                if (engine.getPlayer().getX() == row && engine.getPlayer().getY() == col) {
                    cellPane.getChildren().add(engine.getPlayer().getNode());
                }
                gridPane.add(cellPane, col, row);
            }
        }
    }

    /**
     * Updates the status labels (health, gold, steps, score).
     */
    public void updateStatus() {
        Player player = engine.getPlayer();
        goldLabel.setText("Gold: " + player.getGold());
        healthLabel.setText("Health: " + player.getHealth() + "/100");
        stepsLabel.setText("Steps: " + player.getSteps());
        scoreLabel.setText("Score: " + player.getGold());
    }

    /**
     * Appends a message to the game log.
     * @param message the message to log
     */
    public void logMessage(String message) {
        gameLog.appendText(message + "\n");
        gameLog.setScrollTop(Double.MAX_VALUE);
    }

    /**
     * Appends any new status messages from the engine to the game log.
     */
    private void appendNewStatusMessages() {
        List<String> statusLog = engine.getStatusLog();
        while (lastStatusIndex < statusLog.size()) {
            String msg = statusLog.get(lastStatusIndex);
            if (msg.equals("ADVANCE_LEVEL_UPDATE_TOP_SCORE")) {
                updateTopScores(engine.getScore());
            } else {
                logMessage(msg);
            }
            lastStatusIndex++;
        }
    }

    /**
     * Handles the up button action (move player up).
     */
    @FXML
    private void handleUpButton() {
        if (engine.movePlayer(-1, 0)) {
            logMessage("You moved up one step.");
        } else {
            logMessage("You tried to move up but hit a wall.");
        }
        updateGameGrid();
        updateStatus();
        appendNewStatusMessages();
        checkGameState();
    }

    /**
     * Handles the down button action (move player down).
     */
    @FXML
    private void handleDownButton() {
        if (engine.movePlayer(1, 0)) {
            logMessage("You moved down one step.");
        } else {
            logMessage("You tried to move down but hit a wall.");
        }
        updateGameGrid();
        updateStatus();
        appendNewStatusMessages();
        checkGameState();
    }

    /**
     * Handles the left button action (move player left).
     */
    @FXML
    private void handleLeftButton() {
        if (engine.movePlayer(0, -1)) {
            logMessage("You moved left one step.");
        } else {
            logMessage("You tried to move left but hit a wall.");
        }
        updateGameGrid();
        updateStatus();
        appendNewStatusMessages();
        checkGameState();
    }

    /**
     * Handles the right button action (move player right).
     */
    @FXML
    private void handleRightButton() {
        if (engine.movePlayer(0, 1)) {
            logMessage("You moved right one step.");
        } else {
            logMessage("You tried to move right but hit a wall.");
        }
        updateGameGrid();
        updateStatus();
        appendNewStatusMessages();
        checkGameState();
    }

    /**
     * Handles the help button action (show instructions).
     */
    @FXML
    private void handleHelpButton() {
        String helpText = """
            MiniDungeon Game Instructions:
            
            • Use the arrow buttons to move your character
            • Collect gold to increase your score
            • Avoid traps and enemies
            • Find the exit to complete the level
            • Health potions restore 20 HP
            • Melee mutants deal 10 damage
            • Ranged mutants deal 5 damage
            • Traps deal 15 damage
            
            Good luck adventurer!""";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Help");
        alert.setHeaderText("MiniDungeon Instructions");
        alert.setContentText(helpText);
        alert.showAndWait();
    }

    /**
     * Shows an error alert dialog to the user.
     * @param title the alert title
     * @param header the alert header
     * @param content the alert content
     */
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Handles the save button action (save the game state).
     */
    @FXML
    private void handleSaveButton() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(engine);
            logMessage("Game saved successfully!");
            updateTopScores(engine.getScore());
        } catch (IOException e) {
            logMessage("Error saving game: " + e.getMessage());
            showErrorAlert("Save Error", "Could not save the game.", e.getMessage());
        }
    }

    /**
     * Handles the load button action (load the game state).
     */
    @FXML
    private void handleLoadButton() {
        File saveFile = new File(SAVE_FILE);
        if (!saveFile.exists()) {
            logMessage("No save file found!");
            showErrorAlert("Load Error", "No save file found.", null);
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            engine = (GameEngine) ois.readObject();
            updateGameGrid();
            updateStatus();
            logMessage("Game loaded successfully!");
            lastStatusIndex = 0;
            appendNewStatusMessages();
        } catch (IOException | ClassNotFoundException e) {
            logMessage("Error loading game: " + e.getMessage());
            showErrorAlert("Load Error", "Could not load the game.", e.getMessage());
        }
    }

    /**
     * Handles the run button action (start a new game with selected difficulty).
     */
    @FXML
    private void handleRunButton() {
        int difficulty = 3;
        if (difficultySpinner != null) {
            try {
                difficulty = difficultySpinner.getValue();
            } catch (Exception e) {
                difficulty = 3;
            }
        }
        engine = new GameEngine(10, 10); // Default size
        engine.initializeLevel(difficulty);
        updateGameGrid();
        updateStatus();
        logMessage("New game started with difficulty: " + difficulty);
        lastStatusIndex = 0;
        appendNewStatusMessages();
    }

    /**
     * Handles the Top Score button action (show top scores popup).
     */
    @FXML
    private void handleLoadHighScoreButton() {
        loadTopScores();
        StringBuilder sb = new StringBuilder();
        int rank = 1;
        for (ScoreEntry entry : topScores) {
            sb.append("#").append(rank).append(" ")
                    .append(entry.getScore()).append(" ")
                    .append(entry.getDate()).append("\n");
            rank++;
        }
        if (topScores.isEmpty()) {
            sb.append("No top scores yet.");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Top 5 Scores");
        alert.setHeaderText("Top 5 Scores");
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }

    /**
     * Checks the game state for win/lose/level up and handles transitions.
     */
    private void checkGameState() {
        if (engine.isGameOver()) {
            String message;
            int finalScore = engine.getPlayer().getGold();
            if (engine.hasWon()) {
                boolean isTopScore = updateTopScores(finalScore);
                message = "Congratulations! You've completed the dungeon!\nFinal Score: " + finalScore;
                if (isTopScore) {
                    message += "\nNew Top 5 Score!";
                }
            } else {
                updateTopScores(-1);
                message = "Game Over! You've been defeated.\nFinal Score: -1";
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
            // Reset the game
            engine = new GameEngine(10, 10);
            updateGameGrid();
            updateStatus();
            lastStatusIndex = 0;
            appendNewStatusMessages();
            logMessage("Welcome to MiniDungeon! Use the arrow buttons to move.");
        } else if (engine.getLevel() == 2 && engine.getPlayer().getX() == 1 && engine.getPlayer().getY() == 1) {
            // Just advanced to Level 2
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Level Up");
            alert.setHeaderText(null);
            alert.setContentText("You have advanced to Level 2! Difficulty increased.");
            alert.showAndWait();
        }
    }

    /**
     * Loads the top scores from file.
     */
    private void loadTopScores() {
        File file = new File(TOP_SCORES_FILE);
        if (!file.exists()) {
            System.out.println("[DEBUG] Top scores file does not exist.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            topScores = (List<ScoreEntry>) ois.readObject();
            System.out.println("[DEBUG] Loaded top scores: " + topScores);
        } catch (Exception e) {
            System.out.println("[DEBUG] Error loading top scores: " + e.getMessage());
            topScores = new ArrayList<>();
        }
    }

    /**
     * Saves the top scores to file.
     */
    private void saveTopScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TOP_SCORES_FILE))) {
            oos.writeObject(topScores);
            System.out.println("[DEBUG] Saved top scores: " + topScores);
        } catch (IOException e) {
            System.out.println("[DEBUG] Error saving top scores: " + e.getMessage());
        }
    }

    /**
     * Updates the top scores list and returns true if the score is in the top 5.
     * @param score the score to add
     * @return true if the score is in the top 5
     */
    private boolean updateTopScores(int score) {
        loadTopScores();
        ScoreEntry entry = new ScoreEntry(score, LocalDate.now());
        topScores.add(entry);
        Collections.sort(topScores);
        if (topScores.size() > 5) {
            topScores = topScores.subList(0, 5);
        }
        saveTopScores();
        updateTopScoresDisplay();
        // Return true if this score is in the top 5
        return topScores.contains(entry);
    }

    /**
     * Updates the top scores display in the GUI.
     */
    private void updateTopScoresDisplay() {
        loadTopScores();
        if (topScoresList != null) {
            topScoresList.getItems().clear();
            int rank = 1;
            for (ScoreEntry entry : topScores) {
                String dateStr = entry.getDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                topScoresList.getItems().add("#" + rank + " " + entry.getScore() + " " + dateStr);
                rank++;
            }
        }
    }
}
