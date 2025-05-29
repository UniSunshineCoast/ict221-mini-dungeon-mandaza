package dungeon.engine;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Represents a ranged mutant cell in the dungeon. Can attack from a distance and is removed when defeated.
 */
public class RangedMutantCell implements Cell {
    private boolean defeated;

    public RangedMutantCell() {
        this.defeated = false;
    }

    /**
     * Returns the symbol for this cell.
     * @return 'R' if not defeated, '.' if defeated
     */
    @Override
    public char getSymbol() {
        return defeated ? '.' : 'R';
    }

    /**
     * Handles interaction with the player (gives score, mutant is removed).
     * @param player the player
     * @param engine the game engine
     */
    @Override
    public void interact(Player player, GameEngine engine) {
        if (!defeated) {
            player.addGold(2);
            defeated = true;
            engine.replaceCell(player.getX(), player.getY(), new EmptyCell());
            engine.addStatus("You attacked a ranged mutant and won. Gained 2 score.");
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
        if (!defeated) {
            javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(
                    getClass().getResource("/ranged-mutant.png").toExternalForm()
            );
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            pane.getChildren().add(imageView);
        }
        return pane;
    }
}