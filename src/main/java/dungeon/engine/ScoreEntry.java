package dungeon.engine;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a score entry (a score and its date) for the MiniDungeon game.
 */
public class ScoreEntry implements Serializable, Comparable<ScoreEntry> {
    private final int score;
    private final LocalDate date;

    /**
     * Creates a new ScoreEntry with the given score and date.
     * @param score the score (gold) value
     * @param date the date (e.g. LocalDate.now())
     */
    public ScoreEntry(int score, LocalDate date) {
        this.score = score;
        this.date = date;
    }

    /**
     * Returns the score (gold) value.
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the date of this score entry.
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Compares this ScoreEntry with another (higher score is "less" so that sorting in ascending order yields descending scores).
     * @param other the other ScoreEntry
     * @return a negative integer if this score is higher, a positive integer if lower, or zero if equal
     */
    @Override
    public int compareTo(ScoreEntry other) {
        // Descending order by score
        return Integer.compare(other.score, this.score);
    }

    /**
     * Returns a string representation of this ScoreEntry (e.g. "Score: 10, Date: 2023-01-01").
     * @return a string
     */
    @Override
    public String toString() {
        return score + " " + date;
    }
}