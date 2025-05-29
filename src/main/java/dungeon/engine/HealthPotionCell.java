package dungeon.engine;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Represents a health potion cell in the dungeon. Restores player HP when collected.
 */
public class HealthPotionCell implements Cell {
    private boolean collected;

    public HealthPotionCell() {
        this.collected = false;
    }

    /**
     * Returns the symbol for this cell.
     * @return 'H' if not collected, '.' if collected
     */
    @Override
    public char getSymbol() {
        return collected ? '.' : 'H';
    }

    /**
     * Handles interaction with the player (restores HP, cell becomes empty).
     * @param player the player
     * @param engine the game engine
     */
    @Override
    public void interact(Player player, GameEngine engine) {
        if (!collected) {
            player.increaseHP(4);
            collected = true;
            engine.replaceCell(player.getX(), player.getY(), new EmptyCell());
            engine.addStatus("You picked up a health potion and recovered 4 HP.");
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
                    getClass().getResource("/health-potion.png").toExternalForm()
            );
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            pane.getChildren().add(imageView);
        }
        return pane;
    }
}