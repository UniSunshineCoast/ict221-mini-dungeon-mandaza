package dungeon.engine;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Represents the entry cell in the dungeon. The starting position for the player.
 */
public class EntryCell implements Cell {
    /**
     * Returns the symbol for this cell.
     * @return 'E'
     */
    @Override
    public char getSymbol() {
        return 'E';
    }

    /**
     * Handles interaction with the player (does nothing).
     * @param player the player
     * @param engine the game engine
     */
    @Override
    public void interact(Player player, GameEngine engine) {
        // Entry cells have no interaction
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
        // Transparent/empty cell for entry
        return pane;
    }
}