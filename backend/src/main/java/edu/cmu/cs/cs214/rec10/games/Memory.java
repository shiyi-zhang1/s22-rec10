package edu.cmu.cs.cs214.rec10.games;

import java.util.*;

/**
 * An instance of this class represents a memory game, where multiple players attempt to match items
 * on a game board.  On a player's turn, the player selects two items from the game board; if they match,
 * the player gets a point and continues their turn.  If they don't match, play advances to the next
 * player's turn.
 *
 * The implementation of this game allows the board items to be an arbitrary Java object, specified by
 * the type parameter {@code T}.
 *
 * This class is not thread-safe.
 *
 * @param <T> The type of object on the memory game board; this can be any Java object
 */
public final class Memory<T> {
    private final List<T> board;
    private final int numberOfPlayers;
    private final int[] scores;
    private int currentPlayer;

    /**
     * Constructs a new memory game with the given number of players and list of board items.  The game board
     * will consist of two copies of each item in the given list, accessed with 0-indexed positions.  A typical
     * client will construct a new game and then call {@code selectMatch} repeatedly until the game is over.
     *
     * @throws IllegalArgumentException if numberOfPlayers < 1.
     * @throws NullPointerException if the given list of board items is null.
     */
    public Memory(int numberOfPlayers, List<T> items) {
        if (numberOfPlayers < 1) {
            throw new IllegalArgumentException(String.format("Number of players must be positive: %d", numberOfPlayers));
        }
        Objects.requireNonNull(items);

        int size = items.size();
        this.board = new ArrayList<>(items);
        this.board.addAll(items);
        Collections.shuffle(board);
        this.numberOfPlayers = numberOfPlayers;
        this.scores = new int[numberOfPlayers];
        currentPlayer = 0;
    }

    /**
     * Constructs a new memory game with two players and the given list of board items.
     *
     * @throws NullPointerException if the given list of board items is null.
     * */
    public Memory(List<T> items) {
        this(2, items);
    }

    /** Returns the number of players in the game. */
    public int numberOfPlayers() { return numberOfPlayers; }

    /** Returns the 0-indexed number of the player whose turn it is to play. */
    public int currentPlayer() { return currentPlayer; }

    /**
     * Returns the item at the given position, or null if the item at that position has already been matched.
     *
     * @throws IndexOutOfBoundsException if index < 0 or index >= the initial board size.
     */

    public T itemAt(int index) { return board.get(index); }

    /**
     * Returns true if the board has an item at the given position, and false otherwise.
     *
     * @throws IndexOutOfBoundsException if index < 0 or index >= the initial board size.
     */
    public boolean hasItemAt(int index) { return itemAt(index) != null; }

    /**
     * Returns the current score for a given player number.
     *
     * @throws IndexOutOfBoundsException if playerNumber < 0 or >= numberOfPlayers()
     */
    public int scoreForPlayer(int playerNumber) {
        return scores[playerNumber];
    }

    /**
     * Returns true if the game is over, and false otherwise.  The game is over if all items have been matched
     * from the board.
     */
    public boolean isOver() {
        return board.stream().allMatch(Objects::isNull);
    }

    /**
     * Returns the current leaders of the game, as a list of their player numbers.  If the game is over, the
     * returned leaders are the game winners (possibly a tie).
     */
    public List<Integer> leaders() {
        int maxScore = Arrays.stream(scores).max().getAsInt();
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            if (scores[i] == maxScore) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * Implements a move in a memory game, with the current player selecting two positions ({@code first} and
     * {@code second}) on the board as a proposed match.
     *
     * If the selected items are a match, removes the items from the board, increments the current player's score,
     * and the current player continues their turn.  If the selected items are not a match, simply advances to the
     * next player's turn.
     *
     * @param first The 0-indexed position of the first item selected on the board
     * @param second The 0-indexed position of the second item selected on the board
     * @return true if the selected positions are a match, and false otherwise
     */
    public boolean selectMatch(int first, int second) {
        T firstItem = validateItemAt(first);
        T secondItem = validateItemAt(second);
        if (!firstItem.equals(secondItem)) { // Player selected mis-matched items; advance to next player
            currentPlayer = (currentPlayer + 1) % numberOfPlayers;
            return false;
        }

        board.set(first, null);   // Player selected matched items; remove items from board,
        board.set(second, null);  // increment score, and same player continues their turn
        scores[currentPlayer]++;
        return true;
    }

    private T validateItemAt(int index) {
        T result = itemAt(index);
        if (result == null) {
            throw new IllegalArgumentException(String.format("Player selected empty position: %d", index));
        }
        return result;
    }


}
