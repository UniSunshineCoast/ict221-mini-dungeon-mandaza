package dungeon.gui;

import dungeon.engine.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * GUI for the Maze Runner Game.
 *
 * NOTE: Do NOT run this class directly in IntelliJ - run 'RunGame' instead.
 */
public class GameGUI extends Application {
    private GameEngine engine;
    private GridPane gridPane;
    private Label statusLabel;
    private Label goldLabel;
    private Label healthLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dungeon/gui/game_gui.fxml"));
        BorderPane root = loader.load();

        // Get controller and initialize game
        Controller controller = (Controller) loader.getController();
        engine = new GameEngine(10, 10);
        controller.initialize();

        // Create the scene
        Scene scene = new Scene(root, 1000, 800);

        // Set up key press handler
        scene.setOnKeyPressed(event -> {
            if (engine.isGameOver()) {
                return;
            }

            switch (event.getCode()) {
                case UP:
                    engine.movePlayer(GameEngine.Direction.UP);
                    break;
                case DOWN:
                    engine.movePlayer(GameEngine.Direction.DOWN);
                    break;
                case LEFT:
                    engine.movePlayer(GameEngine.Direction.LEFT);
                    break;
                case RIGHT:
                    engine.movePlayer(GameEngine.Direction.RIGHT);
                    break;
            }

            controller.updateGameGrid();
            controller.updateStatus();

            if (engine.isGameOver()) {
                controller.logMessage("Game Over! Final Gold: " + engine.getPlayer().getGold());
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("MiniDungeon Game");
        primaryStage.show();
    }

    /** In IntelliJ, do NOT run this method.  Run 'RunGame.main()' instead. */
    public static void main(String[] args) {
        launch(args);
    }
}
