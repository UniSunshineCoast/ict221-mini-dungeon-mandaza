package dungeon.engine;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Represents a ladder cell in the dungeon. Used to advance to the next level or win the game.
 */
public class LadderCell implements Cell {
    /**
     * Returns the symbol for this cell.
     * @return 'L'
     */
    @Override
    public char getSymbol() { return 'L'; }
    /**
     * Handles interaction with the player (advances level or wins game).
     * @param player the player
     * @param engine the game engine
     */
    @Override
    public void interact(Player player, GameEngine engine) {
        engine.addStatus("ADVANCE_LEVEL_UPDATE_TOP_SCORE");
        engine.advanceLevel();
    }
    /**
     * Returns whether the cell is passable.
     * @return true
     */
    @Override
    public boolean isPassable() { return true; }

    /**
     * Returns the JavaFX node for this cell.
     * @return the node
     */
    @Override
    public javafx.scene.Node getNode() {
        StackPane pane = new StackPane();
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(
                getClass().getResource("/ladder.png").toExternalForm()
        );
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        pane.getChildren().add(imageView);
        return pane;
    }
}