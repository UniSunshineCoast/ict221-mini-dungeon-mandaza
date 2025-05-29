package dungeon.engine;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Represents an exit cell in the dungeon. Used to exit the level (or win the game if on the final level).
 */
public class ExitCell implements Cell {
    /**
     * Returns the symbol for this cell.
     * @return 'X'
     */
    @Override
    public char getSymbol() {
        return 'X';
    }

    /**
     * Handles interaction with the player (exits the level or wins the game).
     * @param player the player
     * @param engine the game engine
     */
    @Override
    public void interact(Player player, GameEngine engine) {
        engine.setGameOver(true);
        engine.addStatus("You found the exit!");
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
     * @return the node (for example, an ImageView using an exit asset)
     */
    @Override
    public javafx.scene.Node getNode() {
        StackPane pane = new StackPane();
        // Transparent/empty cell for exit
        return pane;
    }
}