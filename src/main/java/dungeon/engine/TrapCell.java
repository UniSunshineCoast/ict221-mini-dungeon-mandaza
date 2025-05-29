package dungeon.engine;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Represents a trap cell in the dungeon. Damages the player when stepped on.
 */
public class TrapCell implements Cell {
    private boolean triggered;

    public TrapCell() {
        this.triggered = false;
    }

    /**
     * Returns the symbol for this cell.
     * @return 'T' if not triggered, '.' if triggered
     */
    @Override
    public char getSymbol() {
        return triggered ? '.' : 'T';
    }

    /**
     * Handles interaction with the player (damages player, trap remains active).
     * @param player the player
     * @param engine the game engine
     */
    @Override
    public void interact(Player player, GameEngine engine) {
        player.decreaseHP(2);
        engine.addStatus("You fell into a trap and lost 2 HP.");
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
        if (!triggered) {
            javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(
                    getClass().getResource("/trap.png").toExternalForm()
            );
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            pane.getChildren().add(imageView);
        }
        return pane;
    }
}