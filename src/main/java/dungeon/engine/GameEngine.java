package dungeon.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * The main game engine for MiniDungeon.
 * Handles map generation, player movement, item interactions, level advancement, and game state.
 */
public class GameEngine implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * An example board to store the current game state.
     *
     * Note: depending on your game, you might want to change this from 'int' to String or something?
     */
    private Cell[][] map;
    private Player player;
    private List<String> statusLog;
    private int level = 1;
    private int difficulty = 3;
    private int maxSteps = 100;
    private int steps = 0;
    private int height;
    private int width;
    private Random random = new Random();
    private boolean gameOver;
    private boolean won;

    public enum Direction { UP, DOWN, LEFT, RIGHT }

    /**
     * Creates a square game board with the given height and width.
     * @param height the height of the board
     * @param width the width of the board
     */
    public GameEngine(int height, int width) {
        this.height = height;
        this.width = width;
        map = new Cell[height][width];
        statusLog = new ArrayList<>();
        gameOver = false;
        won = false;
        initializeLevel(difficulty);
    }

    /**
     * Creates a square game board with the given size for both height and width.
     * @param size the size of the board
     */
    public GameEngine(int size) {
        this(size, size);
    }

    /**
     * Initializes the current level with the given difficulty.
     * @param difficulty the difficulty level
     */
    public void initializeLevel(int difficulty) {
        // Fill map with empty cells
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = new EmptyCell();
            }
        }
        // Place walls on boundaries
        for (int i = 0; i < height; i++) {
            map[i][0] = new WallCell();
            map[i][width-1] = new WallCell();
        }
        for (int j = 0; j < width; j++) {
            map[0][j] = new WallCell();
            map[height-1][j] = new WallCell();
        }
        // Place Entry at [1][1]
        map[1][1] = new EntryCell();
        player = new Player(1, 1);
        // Place Ladder at random location (not on wall or entry)
        int ladderX, ladderY;
        do {
            ladderX = 1 + random.nextInt(height-2);
            ladderY = 1 + random.nextInt(width-2);
        } while (map[ladderX][ladderY] instanceof EntryCell);
        map[ladderX][ladderY] = new LadderCell();
        // Place Gold (5)
        placeRandomItems(new GoldCell(random.nextInt(5) + 1), 5);
        // Place Traps (5)
        placeRandomItems(new TrapCell(), 5);
        // Place Melee Mutants (3)
        placeRandomItems(new MeleeMutantCell(), 3);
        // Place Ranged Mutants (1)
        placeRandomItems(new RangedMutantCell(), 1);
        // Place Health Potions (2)
        placeRandomItems(new HealthPotionCell(), 2);
    }

    private void placeRandomItems(Cell cellType, int count) {
        int placed = 0;
        while (placed < count) {
            int x = 1 + random.nextInt(height-2);
            int y = 1 + random.nextInt(width-2);
            if (map[x][y] instanceof EmptyCell) {
                map[x][y] = cellType instanceof RangedMutantCell ? new RangedMutantCell() :
                        cellType instanceof MeleeMutantCell ? new MeleeMutantCell() :
                                cellType instanceof GoldCell ? new GoldCell(random.nextInt(5) + 1) :
                                        cellType instanceof TrapCell ? new TrapCell() :
                                                cellType instanceof HealthPotionCell ? new HealthPotionCell() :
                                                        cellType instanceof LadderCell ? new LadderCell() :
                                                                cellType instanceof EntryCell ? new EntryCell() :
                                                                        new EmptyCell();
                placed++;
            }
        }
    }

    /**
     * Returns the height of the game board.
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the width of the game board.
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the map of the current game.
     * @return the 2D array of cells
     */
    public Cell[][] getMap() {
        return map;
    }

    /**
     * Returns the player object.
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player object.
     * @param player the player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Replaces the cell at the given coordinates with a new cell.
     * @param x the row
     * @param y the column
     * @param newCell the new cell to place
     */
    public void replaceCell(int x, int y, Cell newCell) {
        map[x][y] = newCell;
    }

    /**
     * Adds a status message to the log.
     * @param message the message to add
     */
    public void addStatus(String message) {
        statusLog.add(message);
        System.out.println(message); // For text-based UI
    }

    /**
     * Returns the list of status log messages.
     * @return the status log
     */
    public List<String> getStatusLog() {
        return statusLog;
    }

    /**
     * Advances the game to the next level or ends the game if on Level 2.
     */
    public void advanceLevel() {
        if (level == 1) {
            level = 2;
            difficulty += 2;
            addStatus("Advancing to Level 2! Difficulty increased to " + difficulty + ".");
            initializeLevel(difficulty);
            // Keep player HP, gold, steps
            // Place player at entry
            player.setX(1);
            player.setY(1);
            addStatus("You are now on Level 2!");
        } else if (level == 2) {
            addStatus("You reached the ladder on Level 2. You win!");
            gameOver = true;
            won = true;
        }
    }

    /**
     * Moves the player by the given row and column delta.
     * @param rowDelta the row change
     * @param colDelta the column change
     * @return true if the move was successful, false otherwise
     */
    public boolean movePlayer(int rowDelta, int colDelta) {
        if (isGameOver()) {
            return false;
        }
        int x = player.getX();
        int y = player.getY();
        int newX = x + rowDelta;
        int newY = y + colDelta;

        // Check bounds
        if (newX < 0 || newX >= height || newY < 0 || newY >= width) {
            addStatus("You tried to move out of bounds.");
            return false;
        }

        // Check wall
        if (!map[newX][newY].isPassable()) {
            addStatus("You tried to move but it is a wall.");
            return false;
        }

        // Move player
        player.setX(newX);
        player.setY(newY);
        player.incrementSteps();
        steps++;

        // Interact with cell
        map[newX][newY].interact(player, this);

        // Check for ranged mutant attacks
        checkRangedMutantAttack(newX, newY);

        // Check win/lose
        if (player.getHealth() <= 0) {
            addStatus("You lost! HP dropped to 0.");
            return false;
        }
        if (steps >= maxSteps) {
            addStatus("You lost! Maximum steps reached.");
            return false;
        }
        return true;
    }

    /**
     * Moves the player in the given direction.
     * @param dir the direction to move
     * @return true if the move was successful, false otherwise
     */
    public boolean movePlayer(Direction dir) {
        if (isGameOver()) {
            return false;
        }
        int x = player.getX();
        int y = player.getY();
        int newX = x, newY = y;
        switch (dir) {
            case UP:    newX = x - 1; break;
            case DOWN:  newX = x + 1; break;
            case LEFT:  newY = y - 1; break;
            case RIGHT: newY = y + 1; break;
        }
        // Check bounds
        if (newX < 0 || newX >= height || newY < 0 || newY >= width) {
            addStatus("You tried to move out of bounds.");
            return false;
        }
        // Check wall
        if (!map[newX][newY].isPassable()) {
            addStatus("You tried to move but it is a wall.");
            return false;
        }
        // Move player
        player.setX(newX);
        player.setY(newY);
        player.incrementSteps();
        steps++;
        addStatus("You moved " + dir.toString().toLowerCase() + ".");
        // Interact with cell
        map[newX][newY].interact(player, this);
        // Check for ranged mutant attacks
        checkRangedMutantAttack(newX, newY);
        // Check win/lose
        if (player.getHealth() <= 0) {
            addStatus("You lost! HP dropped to 0.");
            return false;
        }
        if (steps >= maxSteps) {
            addStatus("You lost! Maximum steps reached.");
            return false;
        }
        return true;
    }

    private void checkRangedMutantAttack(int px, int py) {
        // Check for RangedMutantCell within 2 tiles (horizontal/vertical)
        for (int d = -2; d <= 2; d++) {
            if (d == 0) continue;
            // Horizontal
            int nx = px + d;
            if (nx >= 0 && nx < height && map[nx][py] instanceof RangedMutantCell) {
                if (random.nextBoolean()) {
                    player.decreaseHP(2);
                    addStatus("A ranged mutant attacked and you lost 2 HP.");
                } else {
                    addStatus("A ranged mutant attacked, but missed.");
                }
            }
            // Vertical
            int ny = py + d;
            if (ny >= 0 && ny < width && map[px][ny] instanceof RangedMutantCell) {
                if (random.nextBoolean()) {
                    player.decreaseHP(2);
                    addStatus("A ranged mutant attacked and you lost 2 HP.");
                } else {
                    addStatus("A ranged mutant attacked, but missed.");
                }
            }
        }
    }
    /**
     * Plays a text-based game
     */
    public void printMap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == player.getX() && j == player.getY()) {
                    System.out.print('P');
                } else {
                    System.out.print(map[i][j].getSymbol());
                }
            }
            System.out.println();
        }
    }

    public void printStatus() {
        System.out.println("HP: " + player.getHealth() + ", Gold: " + player.getGold() + ", Steps: " + steps);
    }

    public static void main(String[] args) {
        GameEngine engine = new GameEngine(10);
        Scanner scanner = new Scanner(System.in);
        boolean gameOver = false;
        System.out.println("Welcome to MiniDungeon! Use u, d, l, r to move. Press 'q' to quit.");
        while (!gameOver) {
            engine.printMap();
            engine.printStatus();
            System.out.print("Enter move (u/d/l/r/q): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("q")) {
                System.out.println("Game over.");
                break;
            }
            Direction dir = null;
            switch (input) {
                case "u": dir = Direction.UP; break;
                case "d": dir = Direction.DOWN; break;
                case "l": dir = Direction.LEFT; break;
                case "r": dir = Direction.RIGHT; break;
                default: System.out.println("Invalid input. Use u, d, l, r, or q.");
            }
            if (dir != null) {
                if (!engine.movePlayer(dir)) {
                    gameOver = true;
                }
            }
        }
        scanner.close();
    }

    public boolean isGameOver() {
        return gameOver || player.isDead();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean hasWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public Cell getCell(int row, int col) {
        return map[row][col];
    }

    /**
     * Returns the current level number.
     * @return the level
     */
    public int getLevel() { return level; }
    /**
     * Returns the current difficulty.
     * @return the difficulty
     */
    public int getDifficulty() { return difficulty; }
    /**
     * Returns the player's current score (gold).
     * @return the score
     */
    public int getScore() {
        return player.getGold();
    }
}
