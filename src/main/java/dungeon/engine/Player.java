package dungeon.engine;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.io.Serializable;

public class Player implements Cell, Serializable {
    private static final long serialVersionUID = 1L;
    private int x;
    private int y;
    private int health;
    private int gold;
    private int steps;
    private final int maxHP = 10;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.health = maxHP;
        this.gold = 0;
        this.steps = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public int getSteps() {
        return steps;
    }

    public void incrementSteps() {
        this.steps++;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void increaseHP(int amount) {
        health = Math.min(maxHP, health + amount);
    }

    public void decreaseHP(int amount) {
        health = Math.max(0, health - amount);
    }

    public boolean isDead() {
        return health <= 0;
    }

    @Override
    public void interact(Player player, GameEngine engine) {
        // Player doesn't interact with itself
    }

    @Override
    public boolean isPassable() {
        return true; // Player is always passable
    }

    @Override
    public char getSymbol() {
        return 'P'; // 'P' for Player
    }

    @Override
    public javafx.scene.Node getNode() {
        StackPane stackPane = new StackPane();
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(
                getClass().getResource("/player.png").toExternalForm()
        );
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        stackPane.getChildren().add(imageView);
        return stackPane;
    }
}