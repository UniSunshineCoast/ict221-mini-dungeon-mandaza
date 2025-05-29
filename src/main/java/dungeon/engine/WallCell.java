package dungeon.engine;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents a wall cell in the dungeon. Not passable by the player.
 */
public class WallCell implements Cell {
    /**
     * Returns the symbol for this cell.
     * @return '#'
     */
    @Override
    public char getSymbol() { return '#'; }
    /**
     * Handles interaction with the player (does nothing).
     * @param player the player
     * @param engine the game engine
     */
    @Override
    public void interact(Player player, GameEngine engine) { /* nothing */ }
    /**
     * Returns whether the cell is passable.
     * @return false
     */
    @Override
    public boolean isPassable() { return false; }

    /**
     * Returns the JavaFX node for this cell.
     * @return the node
     */
    @Override
    public javafx.scene.Node getNode() {
        StackPane pane = new StackPane();
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(
                getClass().getResource("/wall.png").toExternalForm()
        );
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        pane.getChildren().add(imageView);
        return pane;
    }
}