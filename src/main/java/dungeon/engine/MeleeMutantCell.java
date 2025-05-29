package dungeon.engine;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Represents a melee mutant cell in the dungeon. Damages the player and is removed when defeated.
 */
public class MeleeMutantCell implements Cell {
    private boolean defeated;

    public MeleeMutantCell() {
        this.defeated = false;
    }

    /**
     * Returns the symbol for this cell.
     * @return 'M' if not defeated, '.' if defeated
     */
    @Override
    public char getSymbol() {
        return defeated ? '.' : 'M';
    }

    /**
     * Handles interaction with the player (damages player, gives score, mutant is removed).
     * @param player the player
     * @param engine the game engine
     */
    @Override
    public void interact(Player player, GameEngine engine) {
        if (!defeated) {
            player.decreaseHP(2);
            player.addGold(2);
            defeated = true;
            engine.replaceCell(player.getX(), player.getY(), new EmptyCell());
            engine.addStatus("You attacked a melee mutant and won. Lost 2 HP, gained 2 score.");
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
                    getClass().getResource("/malee-mutant.png").toExternalForm()
            );
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            pane.getChildren().add(imageView);
        }
        return pane;
    }
}