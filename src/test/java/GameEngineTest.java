import dungeon.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {
    private GameEngine engine;

    @BeforeEach
    void setUp() {
        engine = new GameEngine(10);
    }

    @Test
    void testMapInitialization() {
        assertNotNull(engine.getMap());
        assertEquals(10, engine.getHeight());
        assertEquals(10, engine.getWidth());
        assertNotNull(engine.getPlayer());
        assertEquals(1, engine.getPlayer().getX());
        assertEquals(1, engine.getPlayer().getY());
    }

    @Test
    void testPlayerMovement() {
        // Start at (1,1)
        assertTrue(engine.movePlayer(GameEngine.Direction.RIGHT)); // (1,2)
        assertEquals(1, engine.getPlayer().getX());
        assertEquals(2, engine.getPlayer().getY());

        // Move back to (1,1)
        assertTrue(engine.movePlayer(GameEngine.Direction.LEFT)); // (1,1)
        assertEquals(1, engine.getPlayer().getX());
        assertEquals(1, engine.getPlayer().getY());

        // Test wall collisions from starting position
        assertFalse(engine.movePlayer(GameEngine.Direction.LEFT)); // Wall at (1,0)
        assertFalse(engine.movePlayer(GameEngine.Direction.UP));   // Wall at (0,1)
    }

    @Test
    void testCellInteraction() {
        // Place a GoldCell at [1][2]
        engine.replaceCell(1, 2, new GoldCell(2));
        engine.movePlayer(GameEngine.Direction.RIGHT);
        assertEquals(2, engine.getPlayer().getGold());
        assertTrue(engine.getMap()[1][2] instanceof EmptyCell);
    }

    @Test
    void testLoseCondition() {
        // Place traps in a row to ensure HP drops to 0
        for (int i = 2; i <= 6; i++) {
            engine.replaceCell(1, i, new TrapCell());
        }
        for (int i = 0; i < 5; i++) {
            engine.movePlayer(GameEngine.Direction.RIGHT);
        }
        assertEquals(0, engine.getPlayer().getHealth());
        assertFalse(engine.movePlayer(GameEngine.Direction.RIGHT)); // HP should be 0, game over
    }

    @Test
    void testMaxSteps() {
        for (int i = 0; i < 100; i++) {
            engine.movePlayer(GameEngine.Direction.RIGHT);
        }
        assertFalse(engine.movePlayer(GameEngine.Direction.RIGHT)); // Max steps reached
    }

    @Test
    void testLevelAdvancementAndWin() {
        // Place Ladder at [1][2] for Level 1
        engine.replaceCell(1, 2, new LadderCell());
        assertEquals(1, engine.getLevel());
        engine.movePlayer(GameEngine.Direction.RIGHT); // Step on ladder
        assertEquals(2, engine.getLevel());
        assertEquals(engine.getPlayer().getX(), 1);
        assertEquals(engine.getPlayer().getY(), 1);
        // Place Ladder at [1][2] for Level 2
        engine.replaceCell(1, 2, new LadderCell());
        engine.movePlayer(GameEngine.Direction.RIGHT); // Step on ladder again
        assertTrue(engine.isGameOver());
        assertTrue(engine.hasWon());
    }

    @Test
    void testTopScoresLogic() {
        // Simulate top scores logic
        dungeon.gui.Controller controller = new dungeon.gui.Controller();
        // Use reflection to access and test updateTopScores (if needed)
        // Here, we simulate adding scores directly
        java.util.List<dungeon.engine.ScoreEntry> scores = new java.util.ArrayList<>();
        scores.add(new ScoreEntry(10, java.time.LocalDate.of(2025, 4, 20)));
        scores.add(new ScoreEntry(25, java.time.LocalDate.of(2025, 4, 21)));
        scores.add(new ScoreEntry(15, java.time.LocalDate.of(2025, 4, 22)));
        scores.add(new ScoreEntry(5, java.time.LocalDate.of(2025, 4, 23)));
        scores.add(new ScoreEntry(30, java.time.LocalDate.of(2025, 4, 24)));
        scores.add(new ScoreEntry(20, java.time.LocalDate.of(2025, 4, 25)));
        java.util.Collections.sort(scores);
        assertEquals(30, scores.get(0).getScore());
        assertEquals(25, scores.get(1).getScore());
        assertEquals(20, scores.get(2).getScore());
        assertEquals(15, scores.get(3).getScore());
        assertEquals(10, scores.get(4).getScore());
    }

    @Test
    void testHealthPotionInteraction() {
        engine.replaceCell(1, 2, new HealthPotionCell());
        engine.getPlayer().decreaseHP(4); // HP = 6
        engine.movePlayer(GameEngine.Direction.RIGHT);
        assertEquals(10, engine.getPlayer().getHealth()); // Should restore to max 10
        assertTrue(engine.getMap()[1][2] instanceof EmptyCell);
    }

    @Test
    void testTrapInteraction() {
        engine.replaceCell(1, 2, new TrapCell());
        int hpBefore = engine.getPlayer().getHealth();
        engine.movePlayer(GameEngine.Direction.RIGHT);
        assertEquals(hpBefore - 2, engine.getPlayer().getHealth());
        // Trap remains active
        assertTrue(engine.getMap()[1][2] instanceof TrapCell);
    }

    @Test
    void testMeleeMutantInteraction() {
        engine.replaceCell(1, 2, new MeleeMutantCell());
        int hpBefore = engine.getPlayer().getHealth();
        int goldBefore = engine.getPlayer().getGold();
        engine.movePlayer(GameEngine.Direction.RIGHT);
        assertEquals(hpBefore - 2, engine.getPlayer().getHealth());
        assertEquals(goldBefore + 2, engine.getPlayer().getGold());
        assertTrue(engine.getMap()[1][2] instanceof EmptyCell);
    }

    @Test
    void testRangedMutantInteraction() {
        engine.replaceCell(1, 2, new RangedMutantCell());
        int goldBefore = engine.getPlayer().getGold();
        engine.movePlayer(GameEngine.Direction.RIGHT);
        assertEquals(goldBefore + 2, engine.getPlayer().getGold());
        assertTrue(engine.getMap()[1][2] instanceof EmptyCell);
    }

    @Test
    void testSaveAndLoadGame() throws Exception {
        engine.replaceCell(1, 2, new GoldCell(2));
        engine.movePlayer(GameEngine.Direction.RIGHT);
        int goldBefore = engine.getPlayer().getGold();
        // Save
        java.io.File file = new java.io.File("test_save.dat");
        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(new java.io.FileOutputStream(file))) {
            oos.writeObject(engine);
        }
        // Load
        GameEngine loadedEngine;
        try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.FileInputStream(file))) {
            loadedEngine = (GameEngine) ois.readObject();
        }
        assertEquals(goldBefore, loadedEngine.getPlayer().getGold());
        assertEquals(engine.getPlayer().getX(), loadedEngine.getPlayer().getX());
        assertEquals(engine.getPlayer().getY(), loadedEngine.getPlayer().getY());
        file.delete();
    }
}
