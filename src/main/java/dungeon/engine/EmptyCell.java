package dungeon.engine;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents an empty cell in the dungeon.
 */
public class EmptyCell implements Cell {
    /**
     * Returns the symbol for this cell.
     * @return '.'
     */
    @Override
    public char getSymbol() {
        return '.';
    }

    /**
     * Handles interaction with the player (does nothing).
     * @param player the player
     * @param engine the game engine
     */
    @Override
    public void interact(Player player, GameEngine engine) {
        // Empty cells have no interaction
    }

    /**
     * Returns whether the cell is passable.
     * @return true
     */
    @Override
    public boolean isPassable() {
        return true;
    }

    /**
     * Returns the JavaFX node for this cell.
     * @return the node
     */
    @Override
    public javafx.scene.Node getNode() {
        StackPane pane = new StackPane();
        // Transparent/empty cell
        return pane;
    }
}