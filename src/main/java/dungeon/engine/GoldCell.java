package dungeon.engine;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Represents a gold cell in the dungeon. Collecting it increases the player's score.
 */
public class GoldCell implements Cell {
    private int value;
    private boolean collected;

    /**
     * Creates a gold cell with the specified value.
     * @param value the value of the gold
     */
    public GoldCell(int value) {
        this.value = value;
        this.collected = false;
    }

    /**
     * Returns the symbol for this cell.
     * @return '$' if not collected, '.' if collected
     */
    @Override
    public char getSymbol() {
        return collected ? '.' : '$';
    }

    /**
     * Handles interaction with the player (collects gold).
     * @param player the player
     * @param engine the game engine
     */
    @Override
    public void interact(Player player, GameEngine engine) {
        if (!collected) {
            player.addGold(2);
            collected = true;
            engine.replaceCell(player.getX(), player.getY(), new EmptyCell());
            engine.addStatus("You picked up a gold.");
        }
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
        if (!collected) {
            javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(
                    getClass().getResource("/gold.png").toExternalForm()
            );
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            pane.getChildren().add(imageView);
        }
        return pane;
    }
}