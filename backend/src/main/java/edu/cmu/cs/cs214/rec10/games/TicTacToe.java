package edu.cmu.cs.cs214.rec10.games;

/**
 * Lets two humans play a single game of tic-tac-toe.  A typical client will construct a new TicTacToe game,
 * then call {@link #play} repeatedly (with players alternating) until the game is over.
 *
 * This class is not thread-safe.
 */
public final class TicTacToe {
    /** The standard size of a tic-tac-toe board. */
    public static final int SIZE = 3;

    /** Represents a player (X or O) in a tic-tac-toe game. */
    public enum Player {
        X, O;

        /** Returns this player's opponent. */
        public Player opponent() {
            return this == X ? O : X;
        }
    };

    private Player currentPlayer;
    private Player[][] grid;

    /** Creates a new tic-tac-toe game with the board initially empty and player X starting the game. */
    public TicTacToe() {
        grid = new Player[SIZE][SIZE];
        currentPlayer = Player.X;
    }

    /** Implements a move in a tic-tac-toe game, with the current player playing at the given {@code (x, y)}
     * position on the game board and advancing to the next player's turn.
     *
     * @param x The x-coordinate of the played position
     * @param y The y-coordinate of the played position
     *
     * @throws NullPointerException if {@code x} or {@code y} is negative or >= {@code SIZE}.
     * @throws IllegalArgumentException if the given position has previously been played.
     * @throws IllegalStateException if the game is already over.
     */
    public void play(int x, int y) {
        if (isOver()) {
            throw new IllegalStateException("Game is already over.");
        }
        if (grid[x][y] != null) {
            throw new IllegalArgumentException(
                    String.format("TicTacToe position already played: %d, %d", x, y));
        }

        grid[x][y] = currentPlayer;
        currentPlayer = currentPlayer.opponent();
    }

    /** Returns whether the given {@code (x, y)} position is currently a valid play in the game. */
    public boolean isValidPlay(int x, int y) {
        return grid[x][y] == null && !isOver();
    }

    /** Returns the current player (next to play) in the game. */
    public Player currentPlayer() { return currentPlayer; }

    /** Returns the Player who previously played the given {@code (x, y)} position, or {@code null} if the position
     * has not been previously played.
     *
     * @throws NullPointerException if {@code x} or {@code y} is negative or >= {@code SIZE}.
     */
    public Player getSquare(int x, int y) { return grid[x][y]; }

    /** Returns whether the game is over. */
    public boolean isOver() {
        return winner() != null || allCellsAreFull();
    }

    /** Returns the winner of the game, or {@code null} if the game is tied or not over. */
    public Player winner() {
        for (int i = 0; i < SIZE; i++) { // Checks for horizontal wins
            Player possibleWinner = checkWin(0, i, 1, 0);
            if (possibleWinner != null)
                return possibleWinner;
        }
        for (int i = 0; i < SIZE; i++) { // Checks for vertical wins
            Player possibleWinner = checkWin(i, 0, 0, 1);
            if (possibleWinner != null)
                return possibleWinner;
        }
        { // Checks for diagonal-downward wins
            Player possibleWinner = checkWin(0, 0, 1, 1);
            if (possibleWinner != null)
                return possibleWinner;
        }
        { // Checks for diagonal-upward wins
            Player possibleWinner = checkWin(0, SIZE-1, 1, -1);
            if (possibleWinner != null)
                return possibleWinner;
        }
        return null; // No winner was found
    }

    private Player checkWin(int x, int y, int dx, int dy) {
        Player p = grid[x][y];
        for (int i = 0; i < SIZE; i++) {
            if (grid[x][y] != p) {
                return null;
            }
            x += dx;
            y += dy;
        }
        return p;
    }

    private boolean allCellsAreFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == null)
                    return false;
            }
        }
        return true;
    }
}
