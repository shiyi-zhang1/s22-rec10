package edu.cmu.cs.cs214.rec10.games;

import java.util.concurrent.ThreadLocalRandom;

/**
 * An instance of this class represents a move (rock, paper, or scissors) in a rock-paper-scissors game.
 *
 * This class also contains several convenience methods for determining the winner of two moves and for
 * playing rock-paper-scissors against a computer opponent.
 *
 * Instances of this class are immutable, and all features are thread-safe.
 */
public enum RockPaperScissors {
    ROCK, PAPER, SCISSORS;

    /** Represents a result of a rock-paper-scissors game between two players. */
    public enum Result {WIN, LOSE, TIE};

    /**
     * Returns the winner of this rock-paper-scissors move played against the given opponent's move.
     *
     * @throws NullPointerException if {@code opponent} is null.
     */
    public Result vs(RockPaperScissors opponent) {
        if (this == opponent) return Result.TIE;
        if (((this.ordinal() + 1) % values.length) == opponent.ordinal()) return Result.LOSE;
        return Result.WIN;
    }

    /**
     *  Returns a uniformly random rock-paper-scissors move, for use in simulating a game against a
     * computer opponent.
     */
    public static RockPaperScissors random() {
        return fromOrdinal(ThreadLocalRandom.current().nextInt(values.length));
    }

    /**
     * Returns the result of the given move played against a uniformly random rock-paper-scissors move,
     * for use in playing a game against a computer opponent.
     */
    public static Result play(RockPaperScissors move) {
        return move.vs(random());
    }

    private final static RockPaperScissors[] values = values();

    /**
     * Returns the RockPaperScissors instance corresponding to the given ordinal index.
     *
     * @throws IndexOutOfBoundsException if index < 0 or index >= 3
     */
    public static RockPaperScissors fromOrdinal(int index) {
        return values[index];
    }
}
