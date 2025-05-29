package dungeon.engine;

import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import java.io.Serializable;

public interface Cell extends Serializable {
    char getSymbol();
    void interact(Player player, GameEngine engine);
    boolean isPassable();
    Node getNode();
}
